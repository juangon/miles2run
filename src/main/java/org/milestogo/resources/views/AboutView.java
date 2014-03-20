package org.milestogo.resources.views;

import org.milestogo.domain.Profile;
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

/**
 * Created by shekhargulati on 11/03/14.
 */
@Path("/about")
public class AboutView {

    @Context
    private HttpServletRequest request;

    @Inject
    private ProfileService profileService;

    @GET
    @Produces("text/html")
    public View about() {
        Map<String, Object> model = new HashMap<>();
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("username") != null) {
            String username = (String) session.getAttribute("username");
            Profile profile = profileService.findProfileByUsername(username);
            model.put("profile", profile);
        }
        return new View("/about", model);
    }
}
