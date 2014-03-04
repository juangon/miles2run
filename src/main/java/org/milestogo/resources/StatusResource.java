package org.milestogo.resources;

import org.milestogo.domain.Profile;
import org.milestogo.domain.Status;
import org.milestogo.services.StatusService;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.enterprise.concurrent.ManagedExecutorService;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.servlet.http.HttpServlet;
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
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


@Path("/status")
public class StatusResource {

    @Inject
    private StatusService statusService;

    @Inject
    private Logger logger;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Status createNewStatus(@Context HttpServletRequest request, @Valid final Status status) {
        HttpSession session = request.getSession(false);
        if(session == null || session.getAttribute("profile") == null){
            return null;
        }
        Profile profile = (Profile)session.getAttribute("profile");
        status.setProfile(profile);
        Status persitedStatus = statusService.save(status);
        return persitedStatus;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public Status get(@NotNull @PathParam("id") Long id) {
        return statusService.read(id);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Status> list(@QueryParam("start") int start, @QueryParam("max") int max) {
        max = max == 0 || max > 10 ? 10 : max;
        return statusService.findAll(start, max);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public Response updateStatus(@PathParam("id") Long id, @Valid Status status) {
        Status updatedStatus = statusService.update(id, status);
        return Response.status(Response.Status.OK).entity(updatedStatus).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteStatus(@PathParam("id") Long id) {
        statusService.delete(id);
        return Response.noContent().build();

    }
}
