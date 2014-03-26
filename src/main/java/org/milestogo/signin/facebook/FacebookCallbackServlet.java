package org.milestogo.signin.facebook;

import facebook4j.Facebook;
import facebook4j.FacebookException;
import facebook4j.auth.AccessToken;
import org.milestogo.domain.SocialConnection;
import org.milestogo.domain.SocialProvider;
import org.milestogo.services.ProfileService;
import org.milestogo.services.SocialConnectionService;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.logging.Logger;

@WebServlet("/callback/facebook")
public class FacebookCallbackServlet extends HttpServlet {

    @Inject
    private Logger logger;
    @Inject
    private SocialConnectionService socialConnectionService;
    @Inject
    private ProfileService profileService;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String contextPath = request.getContextPath();
        Facebook facebook = (Facebook) request.getSession().getAttribute("facebook");
        String oauthCode = request.getParameter("code");
        try {
            AccessToken oAuthAccessToken = facebook.getOAuthAccessToken(oauthCode);
            String connectionId = facebook.getId();
            logger.info(oAuthAccessToken.getToken());
            SocialConnection existingSocialConnection = socialConnectionService.findByConnectionId(connectionId);
            logger.info("SocialConnection " + existingSocialConnection);
            if (existingSocialConnection != null) {
                if (existingSocialConnection.getProfile() == null) {
                    logger.info("Profile was null. So redirecting to new profile creation.");
                    response.sendRedirect(contextPath + "/profiles/new?connectionId=" + connectionId);
                } else {
                    String username = existingSocialConnection.getProfile().getUsername();
                    logger.info(String.format("User %s already had authenticated with twitter. So redirecting to home.", username));
                    HttpSession newSession = request.getSession(true);
                    logger.info(String.format("New session created with id %s", newSession.getId()));
                    newSession.setAttribute("username", username);
                    newSession.setAttribute("connectionId", connectionId);
                    response.sendRedirect(contextPath + "/home");
                }
            } else {
                SocialConnection socialConnection = new SocialConnection(oAuthAccessToken.getToken(), null, SocialProvider.FACEBOOK, facebook.users().getMe().getUsername(), connectionId);
                socialConnectionService.save(socialConnection);
                response.sendRedirect(contextPath + "/profiles/new?connectionId=" + connectionId);
            }

        } catch (FacebookException e) {
            throw new ServletException(e);
        }

    }
}
