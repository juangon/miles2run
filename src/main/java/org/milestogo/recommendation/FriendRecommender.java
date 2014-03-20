package org.milestogo.recommendation;

import org.milestogo.domain.Profile;

import java.util.List;

/**
 * Created by shekhargulati on 20/03/14.
 */
public interface FriendRecommender {

    public List<String> recommend(String username);
}
