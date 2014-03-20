package org.milestogo.recommendation;

import com.mongodb.*;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by shekhargulati on 20/03/14.
 */
@ApplicationScoped
public class LocationBasedFriendRecommender implements FriendRecommender {

    @Inject
    DB db;

    private Logger logger = Logger.getLogger(this.getClass().getName());

    @Override
    public List<String> recommend(String username) {
        DBCollection friends = db.getCollection("friends");
        DBObject user = friends.findOne(new BasicDBObject("username", username), new BasicDBObject("lngLat", 1));
        BasicDBObject nearQuery = new BasicDBObject();
        nearQuery.put("username", new BasicDBObject("$ne", username));
        nearQuery.put("lngLat", new BasicDBObject("$near", user.get("lngLat")));
        logger.info(String.format("Near Query %s", nearQuery.toString()));
        DBCursor cursor = friends.find(nearQuery, new BasicDBObject("username", 1)).limit(3);
        List<String> userFriends = new ArrayList<>();
        while (cursor.hasNext()) {
            userFriends.add((String) cursor.next().get("username"));
        }
        return userFriends;
    }
}
