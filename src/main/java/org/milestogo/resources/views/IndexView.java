package org.milestogo.resources.views;

import org.milestogo.api.v2.Counter;
import org.milestogo.domain.Profile;
import org.milestogo.framework.View;
import org.milestogo.services.CounterService;
import org.milestogo.services.ProfileService;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by shekhargulati on 19/03/14.
 */
@Path("/")
public class IndexView {


    @Context
    private HttpServletRequest request;

    @Inject
    private ProfileService profileService;

    @Inject
    private CounterService counterService;

    @GET
    public View index() {
        Map<String, Object> model = new HashMap<>();
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("username") != null) {
            String username = (String) session.getAttribute("username");
            Profile profile = profileService.findProfileByUsername(username);
            model.put("profile", profile);
        }
        // TODO: Fix me .. currently directly converting to KMS
        Long runCounter = counterService.getRunCounter() / 1000;
        Long countryCounter = counterService.getCountryCounter();
        Long developerCounter = counterService.getDeveloperCounter();
        Counter counter = new Counter(developerCounter, countryCounter, runCounter);
        model.put("counter", counter);
        return new View("/index", model);
    }
}
