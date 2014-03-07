package org.milestogo.services;

import org.milestogo.domain.Profile;

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
public class ProfileService {

    @Inject
    private EntityManager entityManager;

    public void save(Profile profile) {
        entityManager.persist(profile);
    }

    public Profile findProfileByUsername(String username) {
        System.out.println("Username " + username);
        TypedQuery<Profile> query = entityManager.createQuery("select new Profile(p.username,p.bio,p.city,p.country,p.fullname,p.profilePic) from Profile p where p.username =:username", Profile.class);
        query.setParameter("username", username);
        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            e.printStackTrace();
            return null;
        }
    }
}
