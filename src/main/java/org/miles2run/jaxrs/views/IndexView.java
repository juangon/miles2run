package org.miles2run.jaxrs.views;

import org.miles2run.business.domain.Profile;
import org.miles2run.business.exceptions.ViewException;
import org.miles2run.business.services.CounterService;
import org.miles2run.business.services.ProfileService;
import org.miles2run.framework.View;
import org.miles2run.jaxrs.api.v2.Counter;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

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
    @Inject
    private Logger logger;

    @GET
    public View index() {
        try {
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
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Unable to load index page.", e);
            throw new ViewException(e.getMessage(), e);
        }
    }
}
