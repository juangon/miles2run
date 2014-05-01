package org.miles2run.resources.api;

import org.miles2run.domain.ProfileSocialConnectionDetails;
import org.miles2run.services.ProfileService;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

/**
 * Created by shekhargulati on 12/03/14.
 */
@Path("/api/v1/profiles")
public class ProfileResource {

    @Context
    private HttpServletRequest request;

    @Inject
    private ProfileService profileService;

    @Path("/me")
    @GET
    @Produces("application/json")
    public Response currentLoggedInUser(){
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("username") == null) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        String username = (String) session.getAttribute("username");
        ProfileSocialConnectionDetails profileWithSocialConnections = profileService.findProfileWithSocialConnections(username);
        return Response.ok(profileWithSocialConnections).build();
    }
}
