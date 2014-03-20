package org.milestogo.recommendation;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.List;

/**
 * Created by shekhargulati on 20/03/14.
 */
@Ignore
public class LocationBasedFriendRecommenderTest {


    private LocationBasedFriendRecommender locationBasedFriendRecommender;

    @Before
    public void setUp() throws Exception {
        MongoClient mongoClient = new MongoClient("localhost",27017);
        DB milestogo = mongoClient.getDB("milestogo");
        locationBasedFriendRecommender = new LocationBasedFriendRecommender();
        locationBasedFriendRecommender.db = milestogo;
    }

    @Test
    public void testRecommend() throws Exception {
        List<String> friends = locationBasedFriendRecommender.recommend("shekhargulati");
        System.out.println(friends);
        Assert.assertNotNull(friends);
    }
}
