package org.miles2run.business.recommendation;

import java.util.List;

/**
 * Created by shekhargulati on 20/03/14.
 */
public interface FriendRecommender {

    public List<String> recommend(String username);
}
