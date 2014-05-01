package org.miles2run.jaxrs.views;

import org.jug.View;
import org.jug.ViewException;
import org.miles2run.business.domain.Profile;
import org.miles2run.business.services.ProfileService;

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
 * Created by shekhargulati on 11/03/14.
 */
@Path("/signin")
public class SigninView {

    @Context
    private HttpServletRequest request;

    @Inject
    private ProfileService profileService;

    @Inject
    private Logger logger;

    @GET
    @Produces("text/html")
    public View about() throws ViewException {
        try {
            Map<String, Object> model = new HashMap<>();
            HttpSession session = request.getSession(false);
            if (session != null && session.getAttribute("username") != null) {
                String username = (String) session.getAttribute("username");
                Profile profile = profileService.findProfileByUsername(username);
                model.put("profile", profile);
                return new View("/home", model);
            }

            return new View("/signin");

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Unable to load about page.", e);
            throw new ViewException(e.getMessage(), e);
        }
    }
}
