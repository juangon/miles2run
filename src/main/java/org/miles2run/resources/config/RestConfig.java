package org.miles2run.resources.config;

import org.miles2run.api.v2.CounterResource;
import org.miles2run.api.v2.FriendshipResource;
import org.miles2run.api.v2.NotificationResource;
import org.miles2run.api.v2.ProfileSuggestionResource;
import org.miles2run.framework.NotFoundExceptionMapper;
import org.miles2run.framework.ViewExceptionMapper;
import org.miles2run.framework.ViewResourceNotFoundExceptionMapper;
import org.miles2run.framework.ViewWriter;
import org.miles2run.resources.api.ProfileResource;
import org.miles2run.resources.api.ProgressResource;
import org.miles2run.resources.views.*;

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
        classes.add(ProgressResource.class);
        classes.add(org.miles2run.api.v2.ActivityResource.class);
        classes.add(org.miles2run.api.v2.ProfileResource.class);
        classes.add(org.miles2run.api.v2.ProgressResource.class);
        classes.add(CounterResource.class);
        classes.add(IndexView.class);
        classes.add(ViewWriter.class);
        classes.add(AboutView.class);
        classes.add(ProfileResource.class);
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
