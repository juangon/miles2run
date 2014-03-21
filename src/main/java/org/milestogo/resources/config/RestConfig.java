package org.milestogo.resources.config;

import org.milestogo.api.v2.CounterResource;
import org.milestogo.api.v2.FriendRecommendationResource;
import org.milestogo.framework.NotFoundExceptionMapper;
import org.milestogo.framework.ViewExceptionMapper;
import org.milestogo.framework.ViewResourceNotFoundExceptionMapper;
import org.milestogo.framework.ViewWriter;
import org.milestogo.resources.api.ActivityResource;
import org.milestogo.resources.api.ProfileResource;
import org.milestogo.resources.api.ProgressResource;
import org.milestogo.resources.views.*;

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
        classes.add(ProgressResource.class);
        classes.add(org.milestogo.api.v2.ActivityResource.class);
        classes.add(org.milestogo.api.v2.ProfileResource.class);
        classes.add(org.milestogo.api.v2.ProgressResource.class);
        classes.add(CounterResource.class);
        classes.add(IndexView.class);
        classes.add(ViewWriter.class);
        classes.add(AboutView.class);
        classes.add(ProfileResource.class);
        classes.add(FriendRecommendationResource.class);
        classes.add(ViewExceptionMapper.class);
        classes.add(ViewResourceNotFoundExceptionMapper.class);
        classes.add(SigninView.class);
        classes.add(NotFoundView.class);
        return classes;
    }
}
