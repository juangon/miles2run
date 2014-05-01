package org.miles2run.jaxrs.config;

import org.miles2run.framework.NotFoundExceptionMapper;
import org.miles2run.framework.ViewExceptionMapper;
import org.miles2run.framework.ViewResourceNotFoundExceptionMapper;
import org.miles2run.framework.ViewWriter;
import org.miles2run.jaxrs.api.v2.*;
import org.miles2run.jaxrs.views.*;

import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

public class RestConfig extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> classes = new HashSet<>();
        classes.add(NotFoundExceptionMapper.class);
        classes.add(HomeView.class);
        classes.add(ProfileView.class);
        classes.add(ActivityResource.class);
        classes.add(ProfileResource.class);
        classes.add(ProgressResource.class);
        classes.add(CounterResource.class);
        classes.add(IndexView.class);
        classes.add(ViewWriter.class);
        classes.add(AboutView.class);
        classes.add(ProfileSuggestionResource.class);
        classes.add(ViewExceptionMapper.class);
        classes.add(ViewResourceNotFoundExceptionMapper.class);
        classes.add(SigninView.class);
        classes.add(FriendshipResource.class);
        classes.add(NotificationResource.class);
        classes.add(SponsorView.class);
        return classes;
    }
}
