package org.milestogo.api.v2;

import org.milestogo.domain.Activity;
import org.milestogo.domain.Profile;
import org.milestogo.domain.Progress;
import org.milestogo.services.ActivityService;
import org.milestogo.services.ProfileService;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by shekhargulati on 15/03/14.
 */
@Path("/api/v2/profiles/{username}/progress")
public class ProgressResource {

    @Inject
    private ActivityService activityService;
    @Inject
    private ProfileService profileService;

    @GET
    @Produces("application/json")
    public Response progress(@PathParam("username") String username) {
        Profile loggedInUser = profileService.findProfile(username);
        if (loggedInUser == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("No user exist with username " + username).build();
        }
        Progress progress = activityService.findTotalDistanceCovered(loggedInUser);
        return Response.status(Response.Status.OK).entity(progress).build();
    }

    @Path("/timeline")
    @GET
    @Produces("application/json")
    public Response progressTimeline(@PathParam("username") String username) {
        Profile loggedInUser = profileService.findProfile(username);
        if (loggedInUser == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("No user exist with username " + username).build();
        }
        List<Activity> activities = activityService.findActivitiesWithTimeStamp(loggedInUser);
        Map<String, Long> progressTimeline = new LinkedHashMap<>();
        for (Activity activity : activities) {
            progressTimeline.put(String.valueOf(activity.getActivityDate().getTime() / 1000), activity.getDistanceCovered() / activity.getGoalUnit().getConversion());
        }
        return Response.status(Response.Status.OK).entity(progressTimeline).build();
    }

}
