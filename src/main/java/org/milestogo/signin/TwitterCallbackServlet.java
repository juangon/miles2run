package org.milestogo.signin;

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
import java.io.IOException;

/**
 * Created by shekhargulati on 03/03/14.
 */
@WebServlet("/callback/twitter")
public class TwitterCallbackServlet extends HttpServlet {

    @Inject
    private TwitterFactory twitterFactory;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Twitter twitter = twitterFactory.getInstance();
        RequestToken requestToken = (RequestToken) request.getSession().getAttribute("requestToken");
        String oauthVerifier = request.getParameter("oauth_verifier");
        try {
            AccessToken oAuthAccessToken = twitter.getOAuthAccessToken(requestToken, oauthVerifier);
            request.getSession().setAttribute("username", oAuthAccessToken.getScreenName());
            request.getSession().removeAttribute("requestToken");
        } catch (TwitterException e) {
            e.printStackTrace();
        }
        response.sendRedirect(request.getContextPath() + "/");
    }
}
