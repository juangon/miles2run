package org.miles2run.jaxrs.views;

import facebook4j.Facebook;
import facebook4j.FacebookException;
import facebook4j.FacebookFactory;
import facebook4j.IdNameEntity;
import org.apache.commons.lang3.StringUtils;
import org.jboss.resteasy.annotations.Form;
import org.jug.filters.LoggedIn;
import org.jug.view.View;
import org.jug.view.ViewException;
import org.jug.view.ViewResourceNotFoundException;
import org.miles2run.business.dao.ProfileMongoDao;
import org.miles2run.business.dao.UserProfile;
import org.miles2run.business.domain.*;
import org.miles2run.business.recommendation.FriendRecommender;
import org.miles2run.business.services.ActivityService;
import org.miles2run.business.services.CounterService;
import org.miles2run.business.services.ProfileService;
import org.miles2run.business.services.SocialConnectionService;
import org.miles2run.business.utils.CityAndCountry;
import org.miles2run.business.utils.GeocoderUtils;
import org.miles2run.business.utils.UrlUtils;
import org.miles2run.jaxrs.views.forms.ProfileForm;
import org.miles2run.jaxrs.vo.ProfileVo;
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

    @GET
    @Produces("text/html")
    @Path("/edit")
    @LoggedIn
    public View editForm() {
        String currentLoggedInUser = getCurrentLoggedInUser();
        if (currentLoggedInUser == null) {
            return new View("/signin", true);
        }
        Profile loggedInUser = profileService.findFullProfileByUsername(currentLoggedInUser);
        return new View("/editProfile", loggedInUser, "profile");
    }

    @POST
    @Produces("text/html")
    @Path("/edit")
    @LoggedIn
    public View editProfile(@Form ProfileForm profileForm) {
        try {
            logger.info(profileForm.toString());
            List<String> errors = new ArrayList<>();
            Profile profile = new Profile(profileForm);
            try {
                profileService.update(profile);
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
                        return new View("/editProfile", profileForm, "profile", errors);
                    }
                }
                errors.add(e.getMessage());
                return new View("/editProfile", profileForm, "profile", errors);

            }
            profileMongoDao.update(profile);
            return new View("/home", true);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Unable to load create profile.", e);
            throw new ViewException(e.getMessage(), e);

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
            HttpSession session = request.getSession(true);
            session.setAttribute("principal", profile.getUsername());
            session.setAttribute("connectionId", profileForm.getConnectionId());
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
                boolean isMyProfile = StringUtils.equals(username, currentLoggedInUser);
                model.put("isMyProfile", isMyProfile);
                if (!isMyProfile) {
                    boolean isFollowing = isFollowing(currentLoggedInUser, username);
                    model.put("isFollowing", isFollowing);
                }
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

    private boolean isFollowing(String currentLoggedInUser, String username) {
        return profileMongoDao.isUserFollowing(currentLoggedInUser, username);
    }


    private String getCurrentLoggedInUser() {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("principal") == null) {
            return null;
        }
        String username = (String) session.getAttribute("principal");
        return username;
    }

    private View facebookProfile(String connectionId, SocialConnection socialConnection) {
        Facebook facebook = facebookFactory.getInstance(new facebook4j.auth.AccessToken(socialConnection.getAccessToken(), null));
        try {

            facebook4j.User user = facebook.users().getMe();
            String facebookProfilePic = null;
            URL picture = facebook.getPictureURL(user.getId());
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
}
