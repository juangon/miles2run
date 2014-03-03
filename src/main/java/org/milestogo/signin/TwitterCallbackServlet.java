package org.milestogo.signin;

import org.milestogo.domain.UserConnection;
import org.milestogo.services.UserConnectionService;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
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
    private UserConnectionService userConnectionService;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Twitter twitter = twitterFactory.getInstance();
        HttpSession session = request.getSession();
        RequestToken requestToken = (RequestToken) session.getAttribute("requestToken");
        String oauthVerifier = request.getParameter("oauth_verifier");
        String userId = null;
        try {
            AccessToken oAuthAccessToken = twitter.getOAuthAccessToken(requestToken, oauthVerifier);
            userId = String.valueOf(twitter.getId());
            UserConnection userConnection = new UserConnection(oAuthAccessToken.getToken(), oAuthAccessToken.getTokenSecret(), "twitter", oAuthAccessToken.getScreenName(), userId);
            userConnectionService.save(userConnection);
            session.removeAttribute("requestToken");
        } catch (TwitterException e) {
            e.printStackTrace();
            return;
        }
        response.sendRedirect(request.getContextPath() + "/profile?userId=" + userId);
    }
}
