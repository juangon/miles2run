package org.milestogo.resources.views;

import org.milestogo.framework.View;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

/**
 * Created by shekhargulati on 11/03/14.
 */
@Path("/about")
public class AboutView {

    @GET
    @Produces("text/html")
    public View about(){
        return new View("/about.html");
    }
}
