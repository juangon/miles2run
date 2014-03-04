package org.milestogo.signin;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
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
@WebServlet("/signin/twitter")
public class TwitterSigninServlet extends HttpServlet {

    @Inject
    private TwitterFactory twitterFactory;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Twitter twitter = twitterFactory.getInstance();
        try {
            StringBuffer callbackUrl = request.getRequestURL();
            int index = callbackUrl.lastIndexOf("/signin/");
            callbackUrl.replace(index, callbackUrl.length(), "").append("/callback/twitter");
            RequestToken requestToken = twitter.getOAuthRequestToken(callbackUrl.toString());
            request.getSession().setAttribute("requestToken", requestToken);
            response.sendRedirect(requestToken.getAuthenticationURL());
        } catch (TwitterException e) {
            throw new RuntimeException("Unable to get Twitter Authentication Url. Exception is: "+e);
        }

    }

}