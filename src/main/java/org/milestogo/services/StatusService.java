package org.milestogo.services;

import org.milestogo.domain.Status;
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
public class StatusService {

    @Inject
    private EntityManager entityManager;

    public Status save(Status status) {
        entityManager.persist(status);
        return status;
    }

    public Status read(@NotNull Long id) {
        return entityManager.find(Status.class, id);
    }

    public List<Status> findAll(int start, int max) {
        TypedQuery<Status> query = entityManager.createQuery(
                "SELECT c from Status c ORDER BY c.postedAt DESC", Status.class);
        query.setFirstResult(start);
        query.setMaxResults(max);
        return query.getResultList();
    }

    public Status update(Long id, Status status) {
        Status existingStatus = this.read(id);
        if (existingStatus == null) {
            throw new StatusNotFoundException("No status found for id: " + id);
        }
        existingStatus = copy(status, existingStatus);
        entityManager.persist(existingStatus);
        return status;
    }

    private Status copy(Status status, Status existingStatus) {
        existingStatus.setStatus(status.getStatus());
        existingStatus.setDistance(status.getDistance());
        existingStatus.setPostedAt(status.getPostedAt());
        return existingStatus;
    }

    public void delete(Long id) {
        Status status = this.read(id);
        if (status == null) {
            throw new StatusNotFoundException("No status found for id: " + id);
        }
        entityManager.remove(status);
    }

}