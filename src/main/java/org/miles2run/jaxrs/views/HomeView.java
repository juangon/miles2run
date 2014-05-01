package org.miles2run.jaxrs.views;

import org.miles2run.business.domain.Profile;
import org.miles2run.business.domain.ProfileSocialConnectionDetails;
import org.miles2run.business.exceptions.ViewException;
import org.miles2run.business.services.ProfileService;
import org.miles2run.framework.View;

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
    public View home() {
        try {
            Map<String, Object> model = new HashMap<>();
            HttpSession session = request.getSession(false);
            if (session == null || session.getAttribute("username") == null) {
                logger.info(String.format("No user existed in session %s . So redirecting to Index view", session));
                return new View("/signin", true);
            }
            logger.info(String.format("session with id %s", session.getId()));
            String username = (String) session.getAttribute("username");
            Profile loggedInUser = profileService.findProfileByUsername(username);
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
