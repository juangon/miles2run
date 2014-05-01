package org.miles2run.api.v2;

import org.miles2run.domain.ProfileDetails;
import org.miles2run.domain.ProfileSocialConnectionDetails;
import org.miles2run.services.DataLoader;
import org.miles2run.services.ProfileService;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Created by shekhargulati on 12/03/14.
 */
@Path("/api/v2/profiles")
public class ProfileResource {

    @Context
    private HttpServletRequest request;

    @Inject
    private ProfileService profileService;

    @Inject
    private DataLoader dataLoader;

    @Path("/me")
    @GET
    @Produces("application/json")
    public Response currentLoggedInUser() {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("username") == null) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        String username = (String) session.getAttribute("username");
        ProfileSocialConnectionDetails profileWithSocialConnections = profileService.findProfileWithSocialConnections(username);
        return Response.ok(profileWithSocialConnections).build();
    }

    @GET
    @Produces("application/json")
    public List<ProfileDetails> profiles(@QueryParam("name") String name) {
        return profileService.findProfileWithFullnameLike(name);
    }

    @Path("/load")
    @GET
    @Produces("text/plain")
    public String loadData() {
        dataLoader.loadData();
        return "Done loading data!!";
    }

}

