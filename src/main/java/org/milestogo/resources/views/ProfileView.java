package org.milestogo.resources.views;

import facebook4j.Facebook;
import facebook4j.FacebookException;
import facebook4j.FacebookFactory;
import facebook4j.IdNameEntity;
import org.jboss.resteasy.annotations.Form;
import org.milestogo.dao.ProfileMongoDao;
import org.milestogo.dao.UserProfile;
import org.milestogo.domain.*;
import org.milestogo.exceptions.ViewException;
import org.milestogo.exceptions.ViewResourceNotFoundException;
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
import javax.persistence.PersistenceException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.RollbackException;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;
import java.util.logging.Level;
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
    private FacebookFactory facebookFactory;

    @Inject
    private ProfileService profileService;

    @Inject
    private CounterService counterService;

    @Inject
    ProfileMongoDao profileMongoDao;

    @Inject
    private FriendRecommender friendRecommender;

    @Inject
    private ActivityService activityService;

    @GET
    @Produces("text/html")
    @Path("/new")
    public View profileForm(@QueryParam("connectionId") String connectionId) {
        try {
            SocialConnection socialConnection = socialConnectionService.findByConnectionId(connectionId);
            if (socialConnection == null) {
                return new View("/signin", true);
            }
            if (socialConnection.getProvider() == SocialProvider.TWITTER) {
                return twitterProfile(connectionId, socialConnection);
            } else if (socialConnection.getProvider() == SocialProvider.FACEBOOK) {
                return facebookProfile(connectionId, socialConnection);
            }

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Unable to load profile form page.", e);
            throw new ViewException(e.getMessage(), e);
        }
        return new View("/signin", true);
    }

    private View facebookProfile(String connectionId, SocialConnection socialConnection) {
        Facebook facebook = facebookFactory.getInstance(new facebook4j.auth.AccessToken(socialConnection.getAccessToken(), null));
        try {

            facebook4j.User user = facebook.users().getMe();
            String facebookProfilePic = null;
            URL picture = facebook.getPictureURL(user.getId());
            System.out.println("Picture: " + picture);
            if (picture != null) {
                try {
                    facebookProfilePic = picture.toURI().toString();
                    System.out.println("Facebook Picture URL" + facebookProfilePic);
                    facebookProfilePic = UrlUtils.removeProtocol(facebookProfilePic);
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }

            }

            String email = user.getEmail();
            String gender = user.getGender();
            IdNameEntity location = user.getLocation();
            CityAndCountry cityAndCountry = new CityAndCountry();
            if (location != null) {
                cityAndCountry = GeocoderUtils.parseLocation(location.getName());
            }

            ProfileVo profile = new ProfileVo(user.getUsername(), user.getName(), user.getBio(), connectionId, facebookProfilePic, cityAndCountry.getCity(), cityAndCountry.getCountry());
            profile.setEmail(email);
            profile.setGender(gender);
            logger.info("Profile: " + profile);
            return new View("/createProfile", profile, "profile");
        } catch (FacebookException e) {
            throw new RuntimeException(e);
        }
    }

    private View twitterProfile(String connectionId, SocialConnection socialConnection) {
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
    @Path("/new")
    public View createProfile(@Form ProfileForm profileForm) {
        try {
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
            try {
                profileService.save(profile);
            } catch (Exception e) {
                logger.info(e.getClass().getCanonicalName());
                RollbackException rollbackException = (RollbackException) e;
                Throwable rollbackCause = rollbackException.getCause();
                if (rollbackCause instanceof PersistenceException) {
                    PersistenceException persistenceException = (PersistenceException) rollbackCause;
                    if (persistenceException.getCause() instanceof ConstraintViolationException) {
                        ConstraintViolationException constraintViolationException = (ConstraintViolationException) persistenceException.getCause();
                        Set<ConstraintViolation<?>> constraintViolations = constraintViolationException.getConstraintViolations();
                        for (ConstraintViolation<?> constraintViolation : constraintViolations) {
                            errors.add(String.format("Field '%s' with value '%s' is invalid. %s", constraintViolation.getPropertyPath(), constraintViolation.getInvalidValue(), constraintViolation.getMessage()));
                        }
                        return new View("/createProfile", profileForm, "profile", errors);
                    }
                }
                errors.add(e.getMessage());
                return new View("/createProfile", profileForm, "profile", errors);

            }

            socialConnectionService.update(profile, profileForm.getConnectionId());
            profileMongoDao.save(profile);
            counterService.updateDeveloperCounter();
            counterService.updateCountryCounter(profile.getCountry());
            request.getSession().setAttribute("username", profile.getUsername());
            request.getSession().setAttribute("connectionId", profileForm.getConnectionId());
            return new View("/home", true);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Unable to load create profile.", e);
            throw new ViewException(e.getMessage(), e);

        }
    }

    @GET
    @Path("/{username}")
    @Produces("text/html")
    public View viewUserProfile(@PathParam("username") String username) {
        try {
            Profile profile = profileService.findProfileByUsername(username);
            if (profile == null) {
                throw new ViewResourceNotFoundException(String.format("No user exists with username %s", username));
            }
            Map<String, Object> model = new HashMap<>();
            model.put("profile", profile);
            String currentLoggedInUser = getCurrentLoggedInUser();
            if (currentLoggedInUser != null) {
                Profile loggedInUser = profileService.findProfileByUsername(currentLoggedInUser);
                model.put("loggedInUser", loggedInUser);
            }

            Progress progress = activityService.findTotalDistanceCovered(username);
            if (progress != null) {
                model.put("progress", progress);
            }
            List<ActivityDetails> timeline = activityService.findAll(username);
            model.put("timeline", timeline);
            UserProfile userProfile = profileMongoDao.findProfile(username);
            model.put("username", username);
            model.put("followers", userProfile.getFollowers().size());
            model.put("following", userProfile.getFollowing().size());
            model.put("activities", timeline.size());
            return new View("/profile", model, "model");
        } catch (Exception e) {
            if (e instanceof ViewResourceNotFoundException) {
                throw e;
            }
            logger.log(Level.SEVERE, String.format("Unable to load %s page.", username), e);
            throw new ViewException(e.getMessage(), e);
        }
    }


    @GET
    @Path("/{username}/following")
    @Produces("text/html")
    public View following(@PathParam("username") String username) {
        try {
            Profile profile = profileService.findProfileByUsername(username);
            if (profile == null) {
                throw new ViewResourceNotFoundException(String.format("No user exists with username %s", username));
            }
            Map<String, Object> model = new HashMap<>();
            model.put("profile", profile);
            String currentLoggedInUser = getCurrentLoggedInUser();
            if (currentLoggedInUser != null) {
                Profile loggedInUser = profileService.findProfileByUsername(currentLoggedInUser);
                model.put("loggedInUser", loggedInUser);
            }
            UserProfile userProfile = profileMongoDao.findProfile(username);
            List<String> following = userProfile.getFollowing();
            if (!following.isEmpty()) {
                List<ProfileDetails> profiles = profileService.findAllProfiles(following);
                model.put("followingProfiles", profiles);
            }
            model.put("username", username);
            model.put("followers", userProfile.getFollowers().size());
            model.put("following", userProfile.getFollowing().size());
            model.put("activities", activityService.count(username));
            return new View("/following", model, "model");
        } catch (Exception e) {
            if (e instanceof ViewResourceNotFoundException) {
                throw e;
            }
            logger.log(Level.SEVERE, String.format("Unable to load %s page.", username), e);
            throw new ViewException(e.getMessage(), e);
        }

    }

    @GET
    @Path("/{username}/followers")
    @Produces("text/html")
    public View followers(@PathParam("username") String username) {
        try {
            Profile profile = profileService.findProfileByUsername(username);
            if (profile == null) {
                throw new ViewResourceNotFoundException(String.format("No user exists with username %s", username));
            }
            Map<String, Object> model = new HashMap<>();
            model.put("profile", profile);
            String currentLoggedInUser = getCurrentLoggedInUser();
            if (currentLoggedInUser != null) {
                Profile loggedInUser = profileService.findProfileByUsername(currentLoggedInUser);
                model.put("loggedInUser", loggedInUser);
            }
            UserProfile userProfile = profileMongoDao.findProfile(username);
            List<String> followers = userProfile.getFollowers();
            if (!followers.isEmpty()) {
                List<ProfileDetails> profiles = profileService.findAllProfiles(followers);
                model.put("followerProfiles", profiles);
            }
            model.put("username", username);
            model.put("followers", userProfile.getFollowers().size());
            model.put("following", userProfile.getFollowing().size());
            model.put("activities", activityService.count(username));
            return new View("/followers", model, "model");
        } catch (Exception e) {
            if (e instanceof ViewResourceNotFoundException) {
                throw e;
            }
            logger.log(Level.SEVERE, String.format("Unable to load %s page.", username), e);
            throw new ViewException(e.getMessage(), e);
        }
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
