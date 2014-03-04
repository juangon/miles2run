package org.milestogo.registration;

import org.milestogo.domain.Profile;
import org.milestogo.domain.UserConnection;
import org.milestogo.services.UserConnectionService;
import org.milestogo.services.UserService;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * Created by shekhargulati on 04/03/14.
 */
@WebServlet("/profile")
public class UserProfileServlet extends HttpServlet {

    @Inject
    private UserService userService;

    @Inject
    private UserConnectionService userConnectionService;

    @Inject
    private Logger logger;
    @Inject
    private TwitterFactory twitterFactory;


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UserConnection userConnection = userConnectionService.findBySocialNetworkId(request.getParameter("userId"));
        twitter4j.User user = null;
        try {
            Twitter twitter = twitterFactory.getInstance();
            Long userId = Long.valueOf(request.getParameter("userId"));
            twitter.setOAuthAccessToken(new AccessToken(userConnection.getAccessToken(), userConnection.getAccessSecret()));
            user = twitter.showUser(userId);
        } catch (TwitterException e) {
            throw new RuntimeException(e);
        }
        String location = user.getLocation();
        String fullName = user.getName();
        String bio = user.getDescription();
        long userId = user.getId();
        request.setAttribute("socialNetworkId", userId);
        request.setAttribute("location", location);
        request.setAttribute("fullName", fullName);
        request.setAttribute("bio", bio);
        request.setAttribute("username",user.getScreenName());
        request.getRequestDispatcher("/profile.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        String city = request.getParameter("city");
        String country = request.getParameter("country");
        String bio = request.getParameter("bio");
        String fullName = request.getParameter("fullName");
        long distance = Long.valueOf(request.getParameter("distance"));
        String username = request.getParameter("username");
        logger.info(String.format("email %s, city %s, country %s, bio %s, fullName %s,distance %d", email, city, country, bio, fullName, distance));
        String socialNetworkId = request.getParameter("socialNetworkId");
        UserConnection userConnection = userConnectionService.findBySocialNetworkId(socialNetworkId);
        Profile profile = new Profile(email, username, bio, city, country, fullName, distance);
        profile.getUserConnections().add(userConnection);
        userService.save(profile);
        request.getSession().setAttribute("profile", profile);
        response.sendRedirect(request.getContextPath() + "/");
    }
}
