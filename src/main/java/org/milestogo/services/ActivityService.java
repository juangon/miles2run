package org.milestogo.services;

import org.milestogo.domain.Activity;
import org.milestogo.domain.ActivityDetails;
import org.milestogo.domain.Profile;
import org.milestogo.exceptions.ActivityNotFoundException;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Created by shekhargulati on 04/03/14.
 */
@ApplicationScoped
@Transactional
public class ActivityService {

    @Inject
    private EntityManager entityManager;
    @Inject
    private ProfileService profileService;

    public Activity save(Activity activity, Profile profile) {
        activity.setPostedBy(profile);
        entityManager.persist(activity);
        return activity;
    }

    public ActivityDetails readById(@NotNull Long id) {
        TypedQuery<ActivityDetails> query = entityManager.createNamedQuery("Activity.findById", ActivityDetails.class).setParameter("id", id);
        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public Activity read(@NotNull Long id) {
        return entityManager.find(Activity.class, id);
    }


    public List<ActivityDetails> findAll(Profile postedBy, int start, int max) {
        TypedQuery<ActivityDetails> query = entityManager.createNamedQuery("Activity.findAll", ActivityDetails.class);
        query.setFirstResult(start);
        query.setMaxResults(max);
        query.setParameter("postedBy", postedBy);
        return query.getResultList();
    }

    public ActivityDetails update(@NotNull Long id, Activity activity) {
        Activity existingActivity = this.read(id);
        if (existingActivity == null) {
            throw new ActivityNotFoundException("No activity found for id: " + id);
        }
        existingActivity.setStatus(activity.getStatus());
        existingActivity.setDistanceCovered(activity.getDistanceCovered());
        entityManager.persist(existingActivity);
        return this.readById(id);
    }

    public void delete(@NotNull Long id) {
        Activity activity = this.read(id);
        if (activity == null) {
            throw new ActivityNotFoundException("No activity found for id: " + id);
        }
        entityManager.remove(activity);
    }

    public long findTotalDistanceCovered(Profile profile) {
        long count = entityManager.createNamedQuery("Activity.countByProfile", Long.class).setParameter("profile", profile).getSingleResult();
        if (count == 0) {
            return 0;
        }
        TypedQuery<Long> query = entityManager.createQuery("SELECT SUM(a.distanceCovered) from Activity a WHERE a.postedBy =:postedBy", Long.class).setParameter("postedBy", profile);
        return query.getSingleResult();
    }

    public List<Activity> findActivitiesWithTimeStamp() {
        return entityManager.createQuery("SELECT NEW Activity(a.activityDate,a.distanceCovered,a.goalUnit) from Activity a", Activity.class).getResultList();
    }
}