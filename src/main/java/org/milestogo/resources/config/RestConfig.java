package org.milestogo.resources.config;

import org.milestogo.framework.ViewWriter;
import org.milestogo.resources.views.HomeView;
import org.milestogo.resources.views.ProfileView;

import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

public class RestConfig extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> classes = new HashSet<>();
        classes.add(HomeView.class);
        classes.add(ProfileView.class);
        classes.add(ViewWriter.class);
        return classes;
    }
}
