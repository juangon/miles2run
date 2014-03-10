package org.milestogo.resources.view;

import org.milestogo.domain.Gender;
import org.milestogo.domain.GoalUnit;
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
import java.util.ArrayList;
import java.util.List;
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
        Profile profile = parseRequest(request);
        List<String> errors = new ArrayList<>();
        Profile profileByEmail = profileService.findProfileByEmail(profile.getEmail());
        String connectionId = request.getParameter("connectionId");
        if (profileByEmail != null) {
            errors.add(String.format("Profile already exists with email %s", profile.getEmail()));
        }
        Profile profileByUsername = profileService.findProfileByUsername(profile.getUsername());
        if (profileByUsername != null) {
            errors.add(String.format("Profile already exists with username %s", profile.getUsername()));
        }
        if (!errors.isEmpty()) {
            request.setAttribute("error", errors);
            request.getRequestDispatcher("/app/profiles/new?connectionId=" + connectionId).forward(request, response);
        }else{
            profileService.save(profile);
            socialConnectionService.update(profile, connectionId);
            request.getSession().setAttribute("profile", profile);
            response.sendRedirect(request.getContextPath() + "/");
        }

    }

    private Profile parseRequest(HttpServletRequest request) {
        String email = request.getParameter("email");
        String city = request.getParameter("city");
        String country = request.getParameter("country");
        String bio = request.getParameter("bio");
        String fullname = request.getParameter("fullname");
        long goal = Long.valueOf(request.getParameter("goal"));
        String username = request.getParameter("username");
        String profilePic = request.getParameter("profilePic");
        String gender = request.getParameter("gender");
        String goalUnit = request.getParameter("goalUnit");
        logger.info(String.format("email %s, gender %s, goalUnit %s", email, gender, goalUnit));
        Profile profile = new Profile(email, username, bio, city, country, fullname, goal, Gender.fromStringToGender(gender), GoalUnit.fromStringToGoalUnit(goalUnit));
        logger.info("GoalUnit: " + GoalUnit.fromStringToGoalUnit(goalUnit));
        profile.setProfilePic(profilePic);
        return profile;
    }
}
