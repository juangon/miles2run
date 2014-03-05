package org.milestogo.registration;

import org.milestogo.domain.Profile;
import org.milestogo.domain.SocialConnection;
import org.milestogo.services.SocialConnectionService;
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
    private SocialConnectionService socialConnectionService;

    @Inject
    private Logger logger;
    @Inject
    private TwitterFactory twitterFactory;


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        SocialConnection socialConnection = socialConnectionService.findByConnectionId(request.getParameter("connectionId"));
        twitter4j.User user = null;
        try {
            Twitter twitter = twitterFactory.getInstance();
            Long connectionId = Long.valueOf(request.getParameter("connectionId"));
            twitter.setOAuthAccessToken(new AccessToken(socialConnection.getAccessToken(), socialConnection.getAccessSecret()));
            user = twitter.showUser(connectionId);
            String fullName = user.getName();
            String bio = user.getDescription();
            long userId = user.getId();
            request.setAttribute("connectionId", userId);
            request.setAttribute("fullName", fullName);
            request.setAttribute("bio", bio);
            request.setAttribute("username", user.getScreenName());
            request.setAttribute("profilePic", user.getOriginalProfileImageURL());
            request.getRequestDispatcher("/profile.jsp").forward(request, response);
        } catch (TwitterException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        String city = request.getParameter("city");
        String country = request.getParameter("country");
        String bio = request.getParameter("bio");
        String fullName = request.getParameter("fullName");
        long goal = Long.valueOf(request.getParameter("goal"));
        String username = request.getParameter("username");
        String profilePic = request.getParameter("profilePic");
        logger.info(String.format("email %s, city %s, country %s, bio %s, fullName %s,goal %d, profilePic %s", email, city, country, bio, fullName, goal, profilePic));
        String connectionId = request.getParameter("connectionId");
        Profile profile = new Profile(email, username, bio, city, country, fullName, goal);
        profile.setProfilePic(profilePic);
        SocialConnection socialConnection = socialConnectionService.findByConnectionId(connectionId);
        profile.getSocialConnections().add(socialConnection);
        userService.save(profile);
        request.getSession().setAttribute("profile", profile);
        response.sendRedirect(request.getContextPath() + "/");
    }
}
