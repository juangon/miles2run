package org.miles2run.api.v2;

/**
 * Created by shekhargulati on 28/03/14.
 */
public class UnfollowRequest {

    private String userToUnfollow;

    public String getUserToUnfollow() {
        return userToUnfollow;
    }

    public void setUserToUnfollow(String userToUnfollow) {
        this.userToUnfollow = userToUnfollow;
    }
}
