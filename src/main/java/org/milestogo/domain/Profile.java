package org.milestogo.domain;

import org.hibernate.validator.constraints.Email;
import org.milestogo.bean_validation.ImageUrl;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by shekhargulati on 04/03/14.
 */
@Entity
public class Profile implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @NotNull
    private Long id;

    @NotNull
    @Column(unique = true)
    @Email
    private String email;

    @NotNull
    @Column(unique = true, updatable = false)
    @Size(max = 20)
    private String username;

    @NotNull
    @Size(max = 50)
    private String fullName;

    @NotNull
    @Size(max = 140)
    private String bio;

    @NotNull
    private String city;

    @NotNull
    private String country;

    @NotNull
    private long goal;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "profile")
    private final List<SocialConnection> socialConnections = new ArrayList<>();

    @OneToMany(mappedBy = "postedBy", fetch = FetchType.LAZY)
    private final List<GoalStatus> statuses = new ArrayList<>();

    @Temporal(TemporalType.DATE)
    @NotNull
    private final Date registeredOn = new Date();

    @ImageUrl
    private String profilePic;

    public Profile() {
    }

    public Profile(String email, String username, String bio, String city, String country, String fullName, long goal) {
        this.email = email;
        this.username = username;
        this.bio = bio;
        this.city = city;
        this.country = country;
        this.fullName = fullName;
        this.goal = goal;
    }


    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public long getGoal() {
        return goal;
    }

    public void setGoal(long goal) {
        this.goal = goal;
    }

    public List<SocialConnection> getSocialConnections() {
        return socialConnections;
    }

    public List<GoalStatus> getStatuses() {
        return statuses;
    }

    public Date getRegisteredOn() {
        return registeredOn;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }
}
