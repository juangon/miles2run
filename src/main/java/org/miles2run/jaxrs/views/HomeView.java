package org.miles2run.jaxrs.views;

import org.jug.filters.LoggedIn;
import org.jug.view.View;
import org.jug.view.ViewException;
import org.miles2run.business.domain.Profile;
import org.miles2run.business.domain.ProfileSocialConnectionDetails;
import org.miles2run.business.services.ProfileService;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by shekhargulati on 10/03/14.
 */
@Path("/home")
public class HomeView {

    @Context
    private HttpServletRequest request;

    @Inject
    private ProfileService profileService;

    @Inject
    private Logger logger;

    @GET
    @LoggedIn
    public View home() {
        try {
            Map<String, Object> model = new HashMap<>();
            String username = (String) request.getSession(false).getAttribute("principal");
            logger.info(String.format("Rendering home page for user %s ", username));
            Profile loggedInUser = profileService.findProfileByUsername(username);
            logger.info("User profile : " + loggedInUser);
            model.put("profile", loggedInUser);
            ProfileSocialConnectionDetails activeProfileWithSocialConnections = profileService.findProfileWithSocialConnections(username);
            model.put("activeProfile", activeProfileWithSocialConnections);
            return new View("/home", model, "model");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Unable to load home page.", e);
            throw new ViewException(e.getMessage(), e);
        }
    }
}
