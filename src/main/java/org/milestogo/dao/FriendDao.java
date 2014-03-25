package org.milestogo.dao;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import org.milestogo.domain.Profile;
import org.milestogo.utils.GeocoderUtils;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

/**
 * Created by shekhargulati on 20/03/14.
 */
@ApplicationScoped
public class FriendDao {

    @Inject
    private DB db;

    public void save(Profile profile) {
        DBCollection friends = db.getCollection("friends");
        double[] lngLat = GeocoderUtils.lngLat(profile.getCity(), profile.getCountry());
        friends.save(new BasicDBObject().append("username", profile.getUsername()).append("lngLat", lngLat));
    }


    public void createFriendship(String username, String userToFollow) {
        DBCollection friends = db.getCollection("friends");
        BasicDBObject findQuery = new BasicDBObject("username", username);
        BasicDBObject updateQuery = new BasicDBObject();
        updateQuery.put("$push", new BasicDBObject("following", userToFollow));
        friends.update(findQuery, updateQuery);
        friends.update(new BasicDBObject("username", userToFollow), new BasicDBObject("$push", new BasicDBObject("followers", username)));
    }
}
