package org.milestogo.resources.views;

import org.jboss.resteasy.annotations.Form;
import org.milestogo.domain.Profile;
import org.milestogo.domain.SocialConnection;
import org.milestogo.resources.views.forms.ProfileForm;
import org.milestogo.framework.View;
import org.milestogo.resources.vo.ProfileVo;
import org.milestogo.services.ProfileService;
import org.milestogo.services.SocialConnectionService;
import org.milestogo.utils.CityAndCountry;
import org.milestogo.utils.GeocoderUtils;
import org.milestogo.utils.UrlUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.auth.AccessToken;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import java.util.ArrayList;
import java.util.List;
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
        request.getSession().setAttribute("username", profile.getUsername());
        request.getSession().setAttribute("connectionId", profileForm.getConnectionId());
        return new View("/home", true);
    }


    @GET
    @Path("/{username}")
    @Produces("text/html")
    public View viewUserProfile(@PathParam("username") String username) {
        Profile profile = profileService.findProfileByUsername(username);
        logger.info(String.format("Profile with %s : %s", username, profile.toString()));
        return new View("/profile", profile, "profile");
    }
}
