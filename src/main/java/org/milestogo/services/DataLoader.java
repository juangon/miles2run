package org.milestogo.services;

import org.milestogo.domain.Gender;
import org.milestogo.domain.GoalUnit;
import org.milestogo.domain.Profile;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * Created by shekhargulati on 20/03/14.
 */
@ApplicationScoped
public class DataLoader {

    @Inject
    private ProfileService profileService;

    public void loadData() {
        Profile profile = newProfile("Rahul Sharma", "rahulsharma", "//abs.twimg.com/sticky/default_profile_images/default_profile_3.png");
        profileService.save(profile);
        profile = newProfile("Sameer Arora", "sameerarora", "//abs.twimg.com/sticky/default_profile_images/default_profile_3.png");
        profileService.save(profile);
        profile = newProfile("Guneet", "guneet", "//abs.twimg.com/sticky/default_profile_images/default_profile_3.png");
        profileService.save(profile);
        for (int i = 0; i < 100; i++) {
            profileService.save(newProfile(i));
        }
    }

    private static Profile newProfile(String fullname, String username, String pic) {
        Profile profile = new Profile(username + "@test.com", username, "test", "test", "test", fullname, 100, Gender.MALE, GoalUnit.KMS);
        profile.setProfilePic(pic);
        return profile;
    }

    private static Profile newProfile(int i) {
        return new Profile("test" + i + "@test.com", "test" + i, "test", "test", "test", "test", 100, Gender.MALE, GoalUnit.KMS);
    }
}

