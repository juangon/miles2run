package org.milestogo.services;

import org.milestogo.domain.Profile;
import org.milestogo.domain.ProfileSocialConnectionDetails;
import org.milestogo.domain.SocialProvider;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by shekhargulati on 04/03/14.
 */
@ApplicationScoped
@Transactional
public class ProfileService {

    @Inject
    EntityManager entityManager;
    @Inject
    Logger logger;

    public Profile save(Profile profile) {
        entityManager.persist(profile);
        return profile;
    }

    public Profile findProfile(String username) {
        try {
            return entityManager.createQuery("select p from Profile p where p.username =:username", Profile.class).setParameter("username", username).getSingleResult();
        } catch (NoResultException e) {
            logger.fine(String.format("No user found with username: %s", username));
            return null;
        }
    }

    public Profile findProfileByUsername(String username) {
        TypedQuery<Profile> query = entityManager.createNamedQuery("Profile.findByUsername", Profile.class);
        query.setParameter("username", username);
        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            logger.fine(String.format("No user found with username: %s", username));
            return null;
        }
    }

    public Profile findProfileByEmail(String email) {
        TypedQuery<Profile> query = entityManager.createNamedQuery("Profile.findByEmail", Profile.class);
        query.setParameter("email", email);
        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            logger.fine(String.format("No user found with email: %s", email));
            return null;
        }
    }

    public ProfileSocialConnectionDetails findProfileWithSocialConnections(String username){
        Query query = entityManager.createNamedQuery("Profile.findProfileWithSocialNetworks").setParameter("username", username);
        List result = query.getResultList();
        if(result == null || result.isEmpty()){
            return null;
        }
        ProfileSocialConnectionDetails profile = new ProfileSocialConnectionDetails();
        for(Object object : result){
            if(object instanceof Object[]){
                Object[] row = (Object[])object;
                profile.setId((Long)row[0]);
                profile.setUsername((String)row[1]);
                profile.getProviders().add(((SocialProvider)row[2]).getProvider());
            }
        }
        return profile;
    }
}
