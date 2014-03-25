package org.milestogo.dao;

import com.mongodb.*;
import org.milestogo.domain.Profile;
import org.milestogo.utils.GeocoderUtils;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by shekhargulati on 20/03/14.
 */
@ApplicationScoped
public class ProfileMongoDao {

    @Inject
    private DB db;

    public void save(Profile profile) {
        DBCollection profiles = db.getCollection("profiles");
        double[] lngLat = GeocoderUtils.lngLat(profile.getCity(), profile.getCountry());
        profiles.save(new BasicDBObject().append("username", profile.getUsername()).append("lngLat", lngLat));
    }


    public void createFriendship(String username, String userToFollow) {
        DBCollection profiles = db.getCollection("profiles");
        BasicDBObject findQuery = new BasicDBObject("username", username);
        BasicDBObject updateQuery = new BasicDBObject();
        updateQuery.put("$push", new BasicDBObject("following", userToFollow));
        profiles.update(findQuery, updateQuery);
        profiles.update(new BasicDBObject("username", userToFollow), new BasicDBObject("$push", new BasicDBObject("followers", username)));
    }

    public UserProfile findProfile(String username) {
        DBCollection profiles = db.getCollection("profiles");
        BasicDBObject findQuery = new BasicDBObject("username", username);
        DBObject dbObject = profiles.findOne(findQuery);
        UserProfile userProfile = new UserProfile();
        if (dbObject == null) {
            return userProfile;
        }
        BasicDBObject basicDBObject = (BasicDBObject) dbObject;
        String profileUsername = basicDBObject.getString("username");
        BasicDBList followingDbList = (BasicDBList) basicDBObject.get("following");
        BasicDBList followersDbList = (BasicDBList) basicDBObject.get("followers");
        userProfile.setUsername(profileUsername);
        userProfile.getFollowers().addAll(toList(followersDbList));
        userProfile.getFollowing().addAll(toList(followingDbList));
        return userProfile;
    }

    private List<String> toList(BasicDBList basicDBList) {
        List<String> list = new ArrayList<>();
        if (basicDBList == null) {
            return list;
        }
        for (Object o : basicDBList) {
            list.add((String) o);
        }
        return list;
    }
}
