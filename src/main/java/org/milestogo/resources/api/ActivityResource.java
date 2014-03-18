package org.milestogo.resources.api;

import org.milestogo.domain.Activity;
import org.milestogo.domain.ActivityDetails;
import org.milestogo.domain.Profile;
import org.milestogo.services.ActivityService;
import org.milestogo.services.CounterService;
import org.milestogo.services.ProfileService;
import org.milestogo.services.TwitterService;

import java.util.*;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
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
    @Inject
    private CounterService counterService;

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
        long distanceCovered = activity.getDistanceCovered() * activity.getGoalUnit().getConversion();
        activity.setDistanceCovered(distanceCovered);
        activityService.save(activity, profile);
        counterService.updateRunCounter(distanceCovered);
        if (activity.getShare().isTwitter()) {
            String connectionId = (String) session.getAttribute("connectionId");
            twitterService.postStatus(activity.getStatus(), connectionId);
        }
        return Response.status(Response.Status.CREATED).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public ActivityDetails get(@NotNull @PathParam("id") Long id) {
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

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public Response updateActivity(@PathParam("id") Long id, @Valid Activity activity) {
        activity.setDistanceCovered(activity.getDistanceCovered() * activity.getGoalUnit().getConversion());
        ActivityDetails updatedActivity = activityService.update(id, activity);
        return Response.status(Response.Status.OK).entity(updatedActivity).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteActivity(@PathParam("id") Long id) {
        activityService.delete(id);
        return Response.noContent().build();
    }

    @Path("/list")
    @GET
    @Produces("application/json")
    public Map<String, Long> getActiviesWithTimestamp() {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("username") == null) {
            return null;
        }
        String username = (String) session.getAttribute("username");
        Profile loggedInUser = profileService.findProfile(username);
        List<Activity> activities = activityService.findActivitiesWithTimeStamp(loggedInUser);
        Map<String, Long> map = new LinkedHashMap<>();
        for (Activity activity : activities) {
            map.put(String.valueOf(activity.getActivityDate().getTime() / 1000), activity.getDistanceCovered() / activity.getGoalUnit().getConversion());
        }
        return map;
    }

}
