package org.milestogo.signin;

import org.milestogo.domain.SocialConnection;
import org.milestogo.domain.SocialProvider;
import org.milestogo.services.ProfileService;
import org.milestogo.services.SocialConnectionService;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * Created by shekhargulati on 03/03/14.
 */
@WebServlet("/callback/twitter")
public class TwitterCallbackServlet extends HttpServlet {

    @Inject
    private TwitterFactory twitterFactory;
    @Inject
    private SocialConnectionService socialConnectionService;
    @Inject
    private ProfileService profileService;
    @Inject
    private Logger logger;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Twitter twitter = twitterFactory.getInstance();
        HttpSession session = request.getSession();
        RequestToken requestToken = (RequestToken) session.getAttribute("requestToken");
        String oauthVerifier = request.getParameter("oauth_verifier");
        String connectionId = null;
        ServletContext servletContext = request.getServletContext();
        try {
            if (oauthVerifier != null && requestToken == null) {
                logger.info(String.format("Request token was %s. So fetching it from ServletContext", requestToken));
                String twitterSentRequestToken = request.getParameter("oauth_token");
                if (twitterSentRequestToken != null) {
                    requestToken = (RequestToken) servletContext.getAttribute(twitterSentRequestToken);
                }
                if (requestToken == null) {
                    throw new IllegalStateException("Verifier present but request token null");
                }
                //Discard the stored request tokens
                servletContext.removeAttribute(twitterSentRequestToken);
            }
            logger.info("Request Token " + requestToken);
            AccessToken oAuthAccessToken = twitter.getOAuthAccessToken(requestToken, oauthVerifier);
            session.removeAttribute("requestToken");
            connectionId = String.valueOf(twitter.getId());
            SocialConnection existingSocialConnection = socialConnectionService.findByConnectionId(connectionId);
            logger.info("SocialConnection " + existingSocialConnection);
            if (existingSocialConnection != null) {
                if (existingSocialConnection.getProfile() == null) {
                    logger.info("Profile was null. So redirecting to new profile creation.");
                    response.sendRedirect(request.getContextPath() + "/profiles/new?connectionId=" + connectionId);
                } else {
                    String username = existingSocialConnection.getProfile().getUsername();
                    logger.info(String.format("User %s already had authenticated with twitter. So redirecting to home.", username));
                    HttpSession newSession = request.getSession(true);
                    logger.info(String.format("New session created with id %s", newSession.getId()));
                    newSession.setAttribute("username", username);
                    newSession.setAttribute("connectionId", connectionId);
                    response.addCookie(new Cookie("JSESSIONID", newSession.getId()));
                    response.sendRedirect(request.getContextPath() + "/home");
                }
            } else {
                SocialConnection socialConnection = new SocialConnection(oAuthAccessToken.getToken(), oAuthAccessToken.getTokenSecret(), SocialProvider.TWITTER, oAuthAccessToken.getScreenName(), connectionId);
                socialConnectionService.save(socialConnection);
                response.sendRedirect(request.getContextPath() + "/profiles/new?connectionId=" + connectionId);
            }

        } catch (TwitterException e) {
            throw new RuntimeException("Not able to make handshake with Twitter. Reason is: ", e);
        }

    }
}
