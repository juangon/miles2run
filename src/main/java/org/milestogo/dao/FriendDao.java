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


}
