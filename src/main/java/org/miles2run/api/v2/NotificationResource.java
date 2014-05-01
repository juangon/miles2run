package org.miles2run.api.v2;

import org.miles2run.services.Notification;
import org.miles2run.services.NotificationService;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import java.util.Set;

/**
 * Created by shekhargulati on 26/03/14.
 */
@Path("/api/v2/profiles/{username}/notifications")
public class NotificationResource {

    @Inject
    private NotificationService notificationService;

    @GET
    @Produces("application/json")
    public Set<Notification> userNotifications(@PathParam("username") String username) {
        return notificationService.notifications(username);
    }
}
