package org.miles2run.jaxrs.config;

import org.jug.JugFilterDispatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebListener;
import java.util.EnumSet;

/**
 * Created by shekhargulati on 01/05/14.
 */
@WebListener
public class AppContextListener implements ServletContextListener{

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("Inside AppContextListener ...");
        ServletContext ctx = sce.getServletContext();
        FilterRegistration.Dynamic filter = ctx.addFilter("JUGFilter", JugFilterDispatcher.class);
        filter.setInitParameter("javax.ws.rs.Application", RestConfig.class.getName());
        filter.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), true, "/*");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
}
