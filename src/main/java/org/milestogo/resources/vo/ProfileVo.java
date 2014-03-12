package org.milestogo.resources.vo;

/**
 * Created by shekhargulati on 06/03/14.
 */
public class ProfileVo {

    private final String username;
    private final String fullname;
    private final String bio;
    private final String connectionId;
    private final String profilePic;
    private final String city;
    private final String country;

    public ProfileVo(String username, String fullname, String bio, String connectionId, String profilePic,String city, String country) {
        this.username = username;
        this.fullname = fullname;
        this.bio = bio;
        this.connectionId = connectionId;
        this.profilePic = profilePic;
        this.city = city;
        this.country = country;
    }

    public String getUsername() {
        return username;
    }

    public String getFullname() {
        return fullname;
    }

    public String getBio() {
        return bio;
    }

    public String getConnectionId() {
        return connectionId;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }
}
