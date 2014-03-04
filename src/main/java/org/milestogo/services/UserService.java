package org.milestogo.services;

import org.milestogo.domain.Profile;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;

/**
 * Created by shekhargulati on 04/03/14.
 */
@ApplicationScoped
@Transactional
public class UserService {

    @Inject
    private EntityManager entityManager;

    public void save(Profile profile){
        entityManager.persist(profile);
    }

}
