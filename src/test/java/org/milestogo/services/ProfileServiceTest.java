package org.milestogo.services;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.milestogo.domain.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.logging.Logger;

/**
 * Created by shekhargulati on 12/03/14.
 */
public class ProfileServiceTest {

    private ProfileService profileService;
    private EntityManager em;

    @Before
    public void setup() {
        String unitName = "confsaysPU";
        EntityManagerFactory emf =
                Persistence.createEntityManagerFactory(unitName);
        this.em = emf.createEntityManager();
        profileService = new ProfileService();
        profileService.entityManager = this.em;
        profileService.logger = Logger.getLogger(this.getClass().getName());
    }

    @Test
    public void shouldSaveProfile() throws Exception {
        em.getTransaction().begin();
        Profile profile = profileService.save(newProfile());
        em.getTransaction().commit();
        Assert.assertNotNull(profile);
        Assert.assertNotNull(profile.getId());
    }

    @Test
    public void testFindProfileWithSocialConnections() throws Exception {
        setupData();
        ProfileSocialConnectionDetails profileWithSocialConnections = profileService.findProfileWithSocialConnections("test");
        Assert.assertNotNull(profileWithSocialConnections);
        Assert.assertEquals(2, profileWithSocialConnections.getProviders().size());

    }

    private void setupData() {
        em.getTransaction().begin();
        Profile profile = newProfile();
        em.persist(profile);
        SocialConnection socialConnection1 = new SocialConnection("test", "test", SocialProvider.TWITTER, "test_user", "test_conn_1");
        socialConnection1.setProfile(profile);
        em.persist(socialConnection1);
        SocialConnection socialConnection2 = new SocialConnection("test", "test", SocialProvider.FACEBOOK, "test_user", "test_conn_2");
        socialConnection2.setProfile(profile);
        em.persist(socialConnection2);
        em.getTransaction().commit();
    }


    private Profile newProfile(){
        return new Profile("test@test.com", "test", "test", "test", "test", "test", 100, Gender.MALE, GoalUnit.KMS);
    }

}
