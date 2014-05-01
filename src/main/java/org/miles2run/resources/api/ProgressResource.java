package org.miles2run.resources.api;

import org.miles2run.domain.Profile;
import org.miles2run.domain.Progress;
import org.miles2run.services.ActivityService;
import org.miles2run.services.ProfileService;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;

/**
 * Created by shekhargulati on 06/03/14.
 */
@Path("/api/v1/progress")
public class ProgressResource {

    @Inject
    private ActivityService activityService;
    @Inject
    private ProfileService profileService;

    @GET
    public Progress progress(@Context HttpServletRequest request){
        HttpSession session = request.getSession(false);
        if(session == null || session.getAttribute("username") == null){
            return null;
        }
        String username = (String) session.getAttribute("username");
        Profile loggedInUser = profileService.findProfile(username);
        return activityService.findTotalDistanceCovered(loggedInUser);
    }
}
