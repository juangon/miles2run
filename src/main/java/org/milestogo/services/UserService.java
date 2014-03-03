package org.milestogo.services;

import org.milestogo.domain.User;

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

    public void save(User user){
        entityManager.persist(user);
    }

}
