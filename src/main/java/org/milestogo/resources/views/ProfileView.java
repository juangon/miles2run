package org.milestogo.resources.views;

import org.jboss.resteasy.annotations.Form;
import org.milestogo.dao.FriendDao;
import org.milestogo.domain.Profile;
import org.milestogo.domain.ProfileDetails;
import org.milestogo.domain.Progress;
import org.milestogo.domain.SocialConnection;
import org.milestogo.framework.View;
import org.milestogo.recommendation.FriendRecommender;
import org.milestogo.resources.views.forms.ProfileForm;
import org.milestogo.resources.vo.ProfileVo;
import org.milestogo.services.ActivityService;
import org.milestogo.services.CounterService;
import org.milestogo.services.ProfileService;
import org.milestogo.services.SocialConnectionService;
import org.milestogo.utils.CityAndCountry;
import org.milestogo.utils.GeocoderUtils;
import org.milestogo.utils.UrlUtils;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.auth.AccessToken;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.util.*;
import java.util.logging.Logger;

/**
 * Created by shekhargulati on 05/03/14.
 */
@Path("/profiles")
public class ProfileView {

    @Context
    private HttpServletRequest request;

    @Context
    private HttpServletResponse response;

    @Inject
    private SocialConnectionService socialConnectionService;

    @Inject
    private Logger logger;

    @Inject
    private TwitterFactory twitterFactory;

    @Inject
    private ProfileService profileService;

    @Inject
    private CounterService counterService;

    @Inject
    FriendDao friendDao;

    @Inject
    private FriendRecommender friendRecommender;

    @Inject
    private ActivityService activityService;

    @GET
    @Produces("text/html")
    @Path("/new")
    public View profileForm(@QueryParam("connectionId") String connectionId) {
        SocialConnection socialConnection = socialConnectionService.findByConnectionId(connectionId);
        try {
            Twitter twitter = twitterFactory.getInstance();
            twitter.setOAuthAccessToken(new AccessToken(socialConnection.getAccessToken(), socialConnection.getAccessSecret()));
            User user = twitter.showUser(Long.valueOf(connectionId));
            String twitterProfilePic = user.getOriginalProfileImageURL();
            twitterProfilePic = UrlUtils.removeProtocol(twitterProfilePic);
            CityAndCountry cityAndCountry = GeocoderUtils.parseLocation(user.getLocation());
            ProfileVo profile = new ProfileVo(user.getScreenName(), user.getName(), user.getDescription(), connectionId, twitterProfilePic, cityAndCountry.getCity(), cityAndCountry.getCountry());
            return new View("/createProfile", profile, "profile");
        } catch (TwitterException e) {
            throw new RuntimeException(e);
        }
    }

    @POST
    public View createProfile(@Form ProfileForm profileForm) {
        logger.info(profileForm.toString());
        List<String> errors = new ArrayList<>();
        if (profileService.findProfileByEmail(profileForm.getEmail()) != null) {
            errors.add(String.format("User already exist with email %s", profileForm.getEmail()));
        }
        if (profileService.findProfileByUsername(profileForm.getUsername()) != null) {
            errors.add(String.format("User already exist with username %s", profileForm.getUsername()));
        }
        if (!errors.isEmpty()) {
            return new View("/createProfile", profileForm, "profile", errors);
        }
        Profile profile = new Profile(profileForm);
        profileService.save(profile);
        socialConnectionService.update(profile, profileForm.getConnectionId());
        friendDao.save(profile);
        counterService.updateDeveloperCounter();
        counterService.updateCountryCounter(profile.getCountry());
        request.getSession().setAttribute("username", profile.getUsername());
        request.getSession().setAttribute("connectionId", profileForm.getConnectionId());
        return new View("/home", true);
    }


    @GET
    @Path("/{username}")
    @Produces("text/html")
    public View viewUserProfile(@PathParam("username") String username) {
        Map<String, Object> model = new HashMap<>();
        String currentLoggedInUser = getCurrentLoggedInUser();
        if (currentLoggedInUser != null) {
            Profile loggedInUser = profileService.findProfileByUsername(currentLoggedInUser);
            model.put("loggedInUser", loggedInUser);
            List<String> recommendFriends = friendRecommender.recommend(currentLoggedInUser);
            if (!recommendFriends.isEmpty()) {
                List<ProfileDetails> recommendations = profileService.findAllProfiles(recommendFriends);
                model.put("recommendations", recommendations);
            }
        }
        Progress progress = activityService.findTotalDistanceCovered(username);
        if (progress != null) {
            model.put("progress", progress);
        }
        Profile profile = profileService.findProfileByUsername(username);
        model.put("profile", profile);
        return new View("/profile", model, "model");
    }

    private String getCurrentLoggedInUser() {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("username") == null) {
            return null;
        }
        String username = (String) session.getAttribute("username");
        return username;
    }
}
