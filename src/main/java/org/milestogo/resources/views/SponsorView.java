package org.milestogo.resources.views;

import org.milestogo.domain.Profile;
import org.milestogo.exceptions.ViewException;
import org.milestogo.framework.View;
import org.milestogo.services.ProfileService;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by shekhargulati on 31/03/14.
 */
@Path("/sponsors")
public class SponsorView {

    @Context
    private HttpServletRequest request;

    @Inject
    private ProfileService profileService;

    @Inject
    private Logger logger;

    @GET
    @Produces("text/html")
    public View sponsors() throws ViewException {
        try {
            Map<String, Object> model = new HashMap<>();
            HttpSession session = request.getSession(false);
            if (session != null && session.getAttribute("username") != null) {
                String username = (String) session.getAttribute("username");
                Profile profile = profileService.findProfileByUsername(username);
                model.put("profile", profile);
            }
            return new View("/sponsors", model);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Unable to load about page.", e);
            throw new ViewException(e.getMessage(), e);
        }
    }
}
