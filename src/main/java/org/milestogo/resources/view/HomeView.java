package org.milestogo.resources.view;

import org.milestogo.framework.View;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

/**
 * Created by shekhargulati on 10/03/14.
 */
@Path("/home")
public class HomeView {

    @GET
    public View home(){
        return new View("/home.html",null);
    }
}
