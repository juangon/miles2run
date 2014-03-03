package org.milestogo.services;

import org.milestogo.domain.UserConnection;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

/**
 * Created by shekhargulati on 04/03/14.
 */
@ApplicationScoped
@Transactional
public class UserConnectionService {

    @Inject
    private EntityManager entityManager;

    public void save(UserConnection userConnection) {
        entityManager.persist(userConnection);
    }

    public UserConnection findBySocialNetworkId(String socialNetworkId) {
        TypedQuery<UserConnection> query = entityManager.createQuery("select u from UserConnection u where u.socialNetworkId =:socialNetworkId", UserConnection.class);
        query.setParameter("socialNetworkId",socialNetworkId);
        return query.getSingleResult();
    }
}
