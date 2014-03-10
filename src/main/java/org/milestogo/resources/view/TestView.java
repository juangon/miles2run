package org.milestogo.resources.view;

import org.milestogo.framework.View;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

/**
 * Created by shekhargulati on 10/03/14.
 */
@Path("/test")
public class TestView {

    @GET
    public View test(){
        return new View("/test.html",null);
    }
}
