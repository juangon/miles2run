package org.milestogo.resources.view;

import org.milestogo.domain.Profile;
import org.milestogo.framework.View;
import org.milestogo.services.ProfileService;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;

/**
 * Created by shekhargulati on 10/03/14.
 */
@Path("/home")
public class HomeView {

    @Context
    private HttpServletRequest request;

    @Inject
    private ProfileService profileService;

    @GET
    public View home() {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("username") == null) {
            return new View("/", true);
        }
        String username = (String) session.getAttribute("username");
        Profile profile = profileService.findProfileByUsername(username);
        return new View("/home.html", profile, "profile");
    }
}
