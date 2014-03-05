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
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

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

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Twitter twitter = twitterFactory.getInstance();
        HttpSession session = request.getSession();
        RequestToken requestToken = (RequestToken) session.getAttribute("requestToken");
        String oauthVerifier = request.getParameter("oauth_verifier");
        String connectionId = null;
        try {
            AccessToken oAuthAccessToken = twitter.getOAuthAccessToken(requestToken, oauthVerifier);
            session.removeAttribute("requestToken");
            connectionId = String.valueOf(twitter.getId());
            SocialConnection existingSocialConnection = socialConnectionService.findByConnectionId(connectionId);
            if (existingSocialConnection != null) {
                request.getSession().setAttribute("profile", existingSocialConnection.getProfile());
                response.sendRedirect(request.getContextPath() + "/");
            } else {
                SocialConnection socialConnection = new SocialConnection(oAuthAccessToken.getToken(), oAuthAccessToken.getTokenSecret(), SocialProvider.TWITTER, oAuthAccessToken.getScreenName(), connectionId);
                socialConnectionService.save(socialConnection);
                response.sendRedirect(request.getContextPath() + "/app/profiles/new?connectionId=" + connectionId);
            }

        } catch (TwitterException e) {
            throw new RuntimeException("Not able to make handshake with Twitter. Reason is: ", e);
        }

    }
}
