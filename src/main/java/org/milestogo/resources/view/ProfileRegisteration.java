package org.milestogo.resources.view;

import org.milestogo.domain.Profile;
import org.milestogo.domain.SocialConnection;
import org.milestogo.services.ProfileService;
import org.milestogo.services.SocialConnectionService;
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
public class ProfileRegisteration extends HttpServlet {

    @Inject
    private ProfileService profileService;

    @Inject
    private SocialConnectionService socialConnectionService;

    @Inject
    private Logger logger;
    @Inject
    private TwitterFactory twitterFactory;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        String city = request.getParameter("city");
        String country = request.getParameter("country");
        String bio = request.getParameter("bio");
        String fullName = request.getParameter("fullname");
        long goal = Long.valueOf(request.getParameter("goal"));
        String username = request.getParameter("username");
        String profilePic = request.getParameter("profilePic");
        logger.info(String.format("email %s, city %s, country %s, bio %s, fullName %s,goal %d, profilePic %s", email, city, country, bio, fullName, goal, profilePic));
        String connectionId = request.getParameter("connectionId");
        Profile profile = new Profile(email, username, bio, city, country, fullName, goal);
        profile.setProfilePic(profilePic);
        profileService.save(profile);
        socialConnectionService.update(profile, connectionId);
        request.getSession().setAttribute("profile", profile);
        response.sendRedirect(request.getContextPath() + "/");
    }
}
