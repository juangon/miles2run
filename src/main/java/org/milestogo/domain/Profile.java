package org.milestogo.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by shekhargulati on 04/03/14.
 */
@Entity
public class Profile implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @NotNull
    private Long id;

    @NotNull
    @Column(unique = true)
    private String email;

    private String bio;

    @NotNull
    private String city;

    @NotNull
    private String country;

    @NotNull
    private String fullName;

    @NotNull
    private long distance;

    @OneToMany(fetch = FetchType.EAGER)
    private List<UserConnection> userConnections = new ArrayList<>();

    public Profile() {
    }

    public Profile(String email, String bio, String city, String country, String fullName,long distance) {
        this.email = email;
        this.bio = bio;
        this.city = city;
        this.country = country;
        this.fullName = fullName;
        this.distance = distance;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public List<UserConnection> getUserConnections() {
        return userConnections;
    }

    public long getDistance() {
        return distance;
    }

    public void setDistance(long distance) {
        this.distance = distance;
    }
}
