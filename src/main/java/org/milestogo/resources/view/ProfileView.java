package org.milestogo.resources.view;

import org.milestogo.domain.Profile;
import org.milestogo.domain.SocialConnection;
import org.milestogo.resources.vo.ProfileVo;
import org.milestogo.services.ProfileService;
import org.milestogo.services.SocialConnectionService;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.auth.AccessToken;

import javax.inject.Inject;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
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
    public String profileForm(@QueryParam("connectionId") String connectionId) {
        SocialConnection socialConnection = socialConnectionService.findByConnectionId(connectionId);
        try {
            Twitter twitter = twitterFactory.getInstance();
            twitter.setOAuthAccessToken(new AccessToken(socialConnection.getAccessToken(), socialConnection.getAccessSecret()));
            User user = twitter.showUser(Long.valueOf(connectionId));
            ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver();
            templateResolver.setTemplateMode("LEGACYHTML5");
            TemplateEngine templateEngine = new TemplateEngine();
            templateEngine.setTemplateResolver(templateResolver);
            ProfileVo profile = new ProfileVo(user.getScreenName(), user.getName(), user.getDescription(), connectionId, user.getMiniProfileImageURL());
            WebContext context = new WebContext(request, response, request.getServletContext());
            context.setVariable("profile", profile);
            return templateEngine.process("/createProfile.html", context);
        } catch (TwitterException e) {
            throw new RuntimeException(e);
        }
    }


    @GET
    @Path("/{username}")
    @Produces("text/html")
    public String viewUserProfile(@PathParam("username") String username) {
        ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver();
        templateResolver.setTemplateMode("HTML5");
        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);

        WebContext context = new WebContext(request, response, request.getServletContext());
        Profile profile = profileService.findProfileByUsername(username);
        System.out.println("Profile is " + profile);
        logger.info(String.format("Profile with %s : %s", username, profile.toString()));
        context.setVariable("profile", profile);
        return templateEngine.process("/profile.html", context);
    }
}
