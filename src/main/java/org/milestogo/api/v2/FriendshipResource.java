package org.milestogo.api.v2;

import org.milestogo.dao.FriendDao;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

/**
 * Created by shekhargulati on 25/03/14.
 */
@Path("/api/v2/profiles/{username}/friendships")
public class FriendshipResource {

    @Inject
    private FriendDao friendDao;

    @Path("/create")
    @POST
    @Consumes("application/json")
    public Response create(@PathParam("username") String username, FriendshipRequest friendshipRequest) {
        friendDao.createFriendship(username, friendshipRequest.getUserToFollow());
        return Response.status(Response.Status.CREATED).build();
    }
}
