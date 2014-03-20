package org.milestogo.services;

import org.milestogo.dao.FriendDao;
import org.milestogo.domain.Gender;
import org.milestogo.domain.GoalUnit;
import org.milestogo.domain.Profile;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

/**
 * Created by shekhargulati on 20/03/14.
 */
@ApplicationScoped
public class DataLoader {

    @Inject
    private ProfileService profileService;
    @Inject
    private FriendDao friendDao;

    private static String[][] cityAndCountries = {
            {"Kurukshetra", "India"},
            {"Boston", "USA"},
            {"London", "UK"},
            {"San Jose", "USA"},
            {"Bangalore", "India"},
            {"Karachi", "Pakistan"},
            {"Kanpur", "India"},
            {"Basel", "Switzerland"},
            {"Tokyo", "Japan"},
            {"Perth", "Australia"}
    };

    public void loadData() {
        Profile profile = newProfile("Rahul Sharma", "rahulsharma", "//abs.twimg.com/sticky/default_profile_images/default_profile_3.png", "Delhi", "India");
        profileService.save(profile);
        friendDao.save(profile);
        profile = newProfile("Sameer Arora", "sameerarora", "//abs.twimg.com/sticky/default_profile_images/default_profile_0.png", "Jaipur", "India");
        profileService.save(profile);
        friendDao.save(profile);
        profile = newProfile("Guneet", "guneet", "//pbs.twimg.com/profile_images/55968278/guneet.JPG", "Gurgaon", "India");
        profileService.save(profile);
        friendDao.save(profile);
        for (int i = 0; i < 100; i++) {
            Profile newProfile = newProfile(i);
            profileService.save(newProfile);
            friendDao.save(newProfile);
        }
    }

    private static Profile newProfile(String fullname, String username, String pic, String city, String country) {
        Profile profile = new Profile(username + "@test.com", username, "test", city, country, fullname, 100, Gender.MALE, GoalUnit.KMS);
        profile.setProfilePic(pic);
        return profile;
    }

    private static Profile newProfile(int i) {
        String[] cityAndCountry = cityAndCountry(i);
        Profile profile = new Profile("test" + i + "@test.com", "test" + i, "test", cityAndCountry[0], cityAndCountry[1], "test " + i, 100, Gender.MALE, GoalUnit.KMS);
        profile.setProfilePic("//pbs.twimg.com/profile_images/3359814086/1fa591007c60afbdeb396bf4205e4a8a.jpeg");
        return profile;
    }

    private static String[] cityAndCountry(int i) {
        return cityAndCountries[i % 10];
    }
}

