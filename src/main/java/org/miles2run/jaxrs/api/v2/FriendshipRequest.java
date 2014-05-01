package org.miles2run.jaxrs.api.v2;

/**
 * Created by shekhargulati on 25/03/14.
 */
public class FriendshipRequest {

    private String userToFollow;

    public String getUserToFollow() {
        return userToFollow;
    }

    public void setUserToFollow(String userToFollow) {
        this.userToFollow = userToFollow;
    }
}
