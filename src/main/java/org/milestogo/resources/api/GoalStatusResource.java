package org.milestogo.resources.api;

import org.milestogo.domain.GoalStatus;
import org.milestogo.domain.Profile;
import org.milestogo.resources.vo.Progress;
import org.milestogo.services.GoalStatusService;

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


@Path("/api/status")
public class GoalStatusResource {

    @Inject
    private GoalStatusService goalStatusService;

    @Inject
    private Logger logger;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createNewStatus(@Context HttpServletRequest request, @Valid final GoalStatus goalStatus) {
        HttpSession session = request.getSession(false);
        if(session == null || session.getAttribute("profile") == null){
            return null;
        }
        Profile loggedInUser = (Profile)session.getAttribute("profile");
        goalStatus.setPostedBy(loggedInUser);
        GoalStatus persitedGoalStatus = goalStatusService.save(goalStatus);
        return Response.status(Response.Status.CREATED).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public GoalStatus get(@NotNull @PathParam("id") Long id) {
        return goalStatusService.readById(id);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<GoalStatus> list(@QueryParam("start") int start, @QueryParam("max") int max) {
        max = max == 0 || max > 10 ? 10 : max;
        return goalStatusService.findAll(start, max);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public Response updateStatus(@PathParam("id") Long id, @Valid GoalStatus goalStatus) {
        GoalStatus updatedGoalStatus = goalStatusService.update(id, goalStatus);
        return Response.status(Response.Status.OK).entity(updatedGoalStatus).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteStatus(@PathParam("id") Long id) {
        goalStatusService.delete(id);
        return Response.noContent().build();

    }


    @GET
    @Path("/progress")
    public Progress progress(@Context HttpServletRequest request){
        HttpSession session = request.getSession(false);
        if(session == null || session.getAttribute("profile") == null){
            return null;
        }
        Profile loggedInUser = (Profile)session.getAttribute("profile");
        long totalDistanceCovered = goalStatusService.findTotalDistanceCovered(loggedInUser);
        Progress progress = new Progress(loggedInUser.getGoal(),totalDistanceCovered);
        return progress;
    }
}
