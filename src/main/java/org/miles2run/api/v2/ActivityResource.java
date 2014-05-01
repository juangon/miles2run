package org.miles2run.api.v2;

import org.miles2run.domain.*;
import org.miles2run.services.*;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by shekhargulati on 15/03/14.
 */
@Path("/api/v2/profiles/{username}/activities")
public class ActivityResource {

    @Inject
    private Logger logger;

    @Inject
    private ActivityService activityService;
    @Inject
    private ProfileService profileService;
    @Inject
    private TwitterService twitterService;
    @Inject
    private CounterService counterService;
    @Inject
    private FacebookService facebookService;


    @POST
    @Consumes("application/json")
    @Produces("application/json")
    public Response postActivity(@PathParam("username") String username, @Valid final Activity activity) {
        Profile profile = profileService.findProfile(username);
        if (profile == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("No user exists with username " + username).build();
        }
        long distanceCovered = activity.getDistanceCovered() * activity.getGoalUnit().getConversion();
        activity.setDistanceCovered(distanceCovered);
        activityService.save(activity, profile);
        counterService.updateRunCounter(distanceCovered);
        //TODO : Ideally it should iterate over all the providers and post status on all the checked ones
        Share share = activity.getShare();
        shareActivity(activity, profile, share);
        return Response.status(Response.Status.CREATED).build();
    }


    @GET
    @Produces("application/json")
    public List<ActivityDetails> list(@PathParam("username") String username, @QueryParam("start") int start, @QueryParam("max") int max) {
        Profile profile = profileService.findProfile(username);
        if (profile == null) {
            return Collections.emptyList();
        }
        max = max == 0 || max > 100 ? 100 : max;
        return activityService.findAll(profile, start, max);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public ActivityDetails get(@NotNull @PathParam("id") Long id) {
        return activityService.readById(id);
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public Response updateActivity(@PathParam("id") Long id, @Valid Activity activity) {
        Activity existingActivity = activityService.read(id);
        if (existingActivity == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        long distanceCovered = activity.getDistanceCovered() * activity.getGoalUnit().getConversion();
        long activityPreviousDistanceCovered = existingActivity.getDistanceCovered();
        long updatedRunCounter = distanceCovered - activityPreviousDistanceCovered;
        logger.info(String.format("distanceCovered %d activityPreviousDistanceCovered %d updatedRunCounter %d", distanceCovered, activityPreviousDistanceCovered, updatedRunCounter));

        counterService.updateRunCounter(updatedRunCounter);
        activity.setDistanceCovered(distanceCovered);
        ActivityDetails updatedActivity = activityService.update(existingActivity, activity);
        return Response.status(Response.Status.OK).entity(updatedActivity).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteActivity(@PathParam("id") Long id) {
        activityService.delete(id);
        return Response.noContent().build();
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}/share")
    public Response shareActivity(@PathParam("username") String username, @PathParam("id") Long id, Activity activity) {
        Profile profile = profileService.findProfile(username);
        if (profile == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("No user exists with username " + username).build();
        }
        Share share = activity.getShare();
        shareActivity(activity, profile, share);
        return Response.ok().build();
    }

    private void shareActivity(Activity activity, Profile profile, Share share) {
        if (share != null) {
            for (SocialConnection socialConnection : profile.getSocialConnections()) {
                if (share.isTwitter() && socialConnection.getProvider() == SocialProvider.TWITTER) {
                    twitterService.postStatus(activity.getStatus(), socialConnection);
                }
                if (share.isFacebook() && socialConnection.getProvider() == SocialProvider.FACEBOOK) {
                    facebookService.postStatus(activity.getStatus(), socialConnection);
                }
            }

        }
    }
}
