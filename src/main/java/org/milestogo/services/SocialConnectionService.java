package org.milestogo.services;

import org.milestogo.domain.SocialConnection;

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
public class SocialConnectionService {

    @Inject
    private EntityManager entityManager;

    public void save(SocialConnection socialConnection) {
        entityManager.persist(socialConnection);
    }

    public SocialConnection findByConnectionId(String connectionId) {
        TypedQuery<SocialConnection> query = entityManager.createQuery("select u from SocialConnection u where u.connectionId =:connectionId", SocialConnection.class);
        query.setParameter("connectionId",connectionId);
        return query.getSingleResult();
    }
}
