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

    public Activity readById(@NotNull Long id) {
        TypedQuery<Activity> query = entityManager.createQuery("SELECT new Activity(c.id,c.status,c.distanceCovered,c.postedAt) from Activity c where id =:id", Activity.class);
        query.setParameter("id", id);
        return query.getSingleResult();
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

    public Activity update(@NotNull Long id, Activity activity) {
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
        Activity activity = this.readById(id);
        if (activity == null) {
            throw new ActivityNotFoundException("No activity found for id: " + id);
        }
        entityManager.remove(activity);
    }

    public long findTotalDistanceCovered(Profile profile) {
        TypedQuery<Long> query = entityManager.createQuery("SELECT SUM(g.distanceCovered) from Activity g WHERE g.postedBy =:postedBy", Long.class);
        query.setParameter("postedBy", profile);
        return query.getSingleResult();
    }
}