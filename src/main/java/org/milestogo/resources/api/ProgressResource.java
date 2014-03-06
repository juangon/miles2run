package org.milestogo.resources.api;

import org.milestogo.domain.Profile;
import org.milestogo.resources.vo.Progress;
import org.milestogo.services.GoalStatusService;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;

/**
 * Created by shekhargulati on 06/03/14.
 */
@Path("/api/progress")
public class ProgressResource {

    @Inject
    private GoalStatusService goalStatusService;

    @GET
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
