package org.milestogo.domain;

/**
 * Created by shekhargulati on 20/03/14.
 */
public class ProfileDetails {

    private String username;
    private String fullname;
    private String pic;
    private String city;
    private String country;

    public ProfileDetails(String username, String fullname, String pic, String city, String country) {
        this.username = username;
        this.fullname = fullname;
        this.pic = pic;
        this.city = city;
        this.country = country;
    }

    private String getImageWithSize(String picUrl, String size) {
        if (picUrl != null) {
            int index = picUrl.lastIndexOf(".");
            String imgPrefix = picUrl.substring(0, index);
            String picExtension = picUrl.substring(index);
            return new StringBuilder(imgPrefix).append("_").append(size).append(picExtension).toString();
        }
        return picUrl;
    }

    public String getUsername() {
        return username;
    }

    public String getFullname() {
        return fullname;
    }

    public String getPic() {
        return pic;
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }
}
