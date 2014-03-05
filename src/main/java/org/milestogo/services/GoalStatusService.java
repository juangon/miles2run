package org.milestogo.services;

import org.milestogo.domain.GoalStatus;
import org.milestogo.exceptions.StatusNotFoundException;

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
public class GoalStatusService {

    @Inject
    private EntityManager entityManager;

    public GoalStatus save(GoalStatus goalStatus) {
        entityManager.persist(goalStatus);
        return goalStatus;
    }

    public GoalStatus readById(@NotNull Long id) {
        TypedQuery<GoalStatus> query = entityManager.createQuery("SELECT new GoalStatus(c.id,c.status,c.distanceCovered,c.postedAt) from GoalStatus c where id =:id", GoalStatus.class);
        query.setParameter("id", id);
        return query.getSingleResult();
    }

    public GoalStatus read(@NotNull Long id) {
        return entityManager.find(GoalStatus.class, id);
    }


    public List<GoalStatus> findAll(int start, int max) {
        TypedQuery<GoalStatus> query = entityManager.createQuery(
                "SELECT new GoalStatus(c.id,c.status,c.distanceCovered) from GoalStatus c ORDER BY c.postedAt DESC", GoalStatus.class);
        query.setFirstResult(start);
        query.setMaxResults(max);
        return query.getResultList();
    }

    public GoalStatus update(@NotNull Long id, GoalStatus goalStatus) {
        GoalStatus existingGoalStatus = this.read(id);
        if (existingGoalStatus == null) {
            throw new StatusNotFoundException("No goalStatus found for id: " + id);
        }
        existingGoalStatus.setStatus(goalStatus.getStatus());
        existingGoalStatus.setDistanceCovered(goalStatus.getDistanceCovered());
        entityManager.persist(existingGoalStatus);
        return this.readById(id);
    }

    public void delete(@NotNull Long id) {
        GoalStatus goalStatus = this.readById(id);
        if (goalStatus == null) {
            throw new StatusNotFoundException("No goalStatus found for id: " + id);
        }
        entityManager.remove(goalStatus);
    }

}