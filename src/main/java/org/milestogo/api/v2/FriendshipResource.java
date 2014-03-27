package org.milestogo.api.v2;

import org.milestogo.dao.ProfileMongoDao;
import org.milestogo.services.Action;
import org.milestogo.services.Notification;
import org.milestogo.services.NotificationService;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import java.util.Date;

/**
 * Created by shekhargulati on 25/03/14.
 */
@Path("/api/v2/profiles/{username}/friendships")
public class FriendshipResource {

    @Inject
    private ProfileMongoDao profileMongoDao;

    @Inject
    private NotificationService notificationService;

    @Path("/create")
    @POST
    @Consumes("application/json")
    public Response create(@PathParam("username") String username, FriendshipRequest friendshipRequest) {
        profileMongoDao.createFriendship(username, friendshipRequest.getUserToFollow());
        notificationService.addNotification(new Notification(friendshipRequest.getUserToFollow(), username, Action.FOLLOW, new Date().getTime()));
        return Response.status(Response.Status.CREATED).build();
    }
}