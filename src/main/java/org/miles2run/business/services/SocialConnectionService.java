package org.miles2run.business.services;

import org.miles2run.business.domain.Profile;
import org.miles2run.business.domain.SocialConnection;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
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
        query.setParameter("connectionId", connectionId);
        try {
            SocialConnection socialConnection = query.getSingleResult();
            return socialConnection;
        } catch (NoResultException e) {
            return null;
        }
    }


    public void update(Profile profile, String connectionId) {
        SocialConnection socialConnection = this.findByConnectionId(connectionId);
        socialConnection.setProfile(profile);
        entityManager.persist(socialConnection);
    }
}
