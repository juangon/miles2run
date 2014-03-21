package org.milestogo.resources.views;

import org.milestogo.framework.View;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

/**
 * Created by shekhargulati on 21/03/14.
 */
@Path("/404")
public class NotFoundView {

    @GET
    @Produces("text/html")
    public View notFound() {
        return new View("/404");
    }
}
