package org.milestogo.resources.api;

import org.milestogo.domain.Activity;
import org.milestogo.domain.ActivityDetails;
import org.milestogo.domain.Profile;
import org.milestogo.resources.vo.Progress;
import org.milestogo.services.ActivityService;
import org.milestogo.services.ProfileService;
import org.milestogo.services.TwitterService;

import java.util.List;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


@Path("/api/v1/activities")
public class ActivityResource {

    @Inject
    private ActivityService activityService;

    @Inject
    private Logger logger;
    @Inject
    private ProfileService profileService;
    @Context
    private HttpServletRequest request;
    @Inject
    private TwitterService twitterService;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response postNewActivity(@Valid final Activity activity) {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("username") == null) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        String username = (String) session.getAttribute("username");
        Profile profile = profileService.findProfile(username);
        activity.setDistanceCovered(activity.getDistanceCovered() * activity.getGoalUnit().getConversion());
        activityService.save(activity, profile);
        if(activity.getShare().isTwitter()){
            String connectionId = (String)session.getAttribute("connectionId");
            twitterService.postStatus(activity.getStatus(),connectionId);
        }
        return Response.status(Response.Status.CREATED).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public Activity get(@NotNull @PathParam("id") Long id) {
        return activityService.readById(id);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<ActivityDetails> list(@QueryParam("start") int start, @QueryParam("max") int max) {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("username") == null) {
            return null;
        }
        String username = (String) session.getAttribute("username");
        Profile loggedInUser = profileService.findProfile(username);
        max = max == 0 || max > 100 ? 100 : max;
        return activityService.findAll(loggedInUser, start, max);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public Response updateStatus(@PathParam("id") Long id, @Valid Activity activity) {
        Activity updatedActivity = activityService.update(id, activity);
        return Response.status(Response.Status.OK).entity(updatedActivity).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteStatus(@PathParam("id") Long id) {
        activityService.delete(id);
        return Response.noContent().build();
    }
}
