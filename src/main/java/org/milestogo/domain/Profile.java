package org.milestogo.domain;

import org.hibernate.validator.constraints.Email;
import org.milestogo.bean_validation.ImageUrl;
import org.milestogo.resources.views.forms.ProfileForm;

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
@NamedQueries({
        @NamedQuery(name = "Profile.findByUsername",query = "select new Profile(p.username,p.bio,p.city,p.country,p.fullname,p.profilePic) from Profile p where p.username =:username"),
        @NamedQuery(name = "Profile.findByEmail",query = "select new Profile(p.username,p.bio,p.city,p.country,p.fullname,p.profilePic) from Profile p where p.email =:email")
})
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
    private String fullname;

    @NotNull
    @Size(max = 140)
    private String bio;

    @NotNull
    private String city;

    @NotNull
    private String country;

    private Gender gender;

    @NotNull
    private long goal;

    @NotNull
    private GoalUnit goalUnit;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "profile")
    private final List<SocialConnection> socialConnections = new ArrayList<>();

    @OneToMany(mappedBy = "postedBy", fetch = FetchType.LAZY)
    private final List<GoalStatus> statuses = new ArrayList<>();

    @Temporal(TemporalType.DATE)
    @NotNull
    private final Date registeredOn = new Date();

    @ImageUrl
    private String profilePic;

    @Transient
    private String miniProfilePic;

    @Transient
    private String biggerProfilePic;

    public Profile() {
    }

    public Profile(String email, String username, String bio, String city, String country, String fullname, long goal, Gender gender, GoalUnit goalUnit) {
        this.email = email;
        this.username = username;
        this.bio = bio;
        this.city = city;
        this.country = country;
        this.fullname = fullname;
        this.gender = gender;
        this.goalUnit = goalUnit;
        this.goal = goal * goalUnit.getConversion();


    }

    public Profile(String username, String bio, String city, String country, String fullname, String profilePic) {
        this.username = username;
        this.bio = bio;
        this.city = city;
        this.country = country;
        this.fullname = fullname;
        this.profilePic = profilePic;
    }

    public Profile(ProfileForm profileForm) {
        this.email = profileForm.getEmail();
        this.username = profileForm.getUsername();
        this.bio = profileForm.getBio();
        this.city = profileForm.getCity();
        this.country = profileForm.getCountry();
        this.fullname = profileForm.getFullname();
        this.gender = Gender.fromStringToGender(profileForm.getGender());
        this.goalUnit = GoalUnit.fromStringToGoalUnit(profileForm.getGoalUnit());
        this.goal = profileForm.getGoal() * this.goalUnit.getConversion();
        this.profilePic = profileForm.getProfilePic();
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullName) {
        this.fullname = fullName;
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

    public String getMiniProfilePic() {
        return getImageWithSize("mini");
    }

    public String getBiggerProfilePic() {
        return getImageWithSize("bigger");
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public GoalUnit getGoalUnit() {
        return goalUnit;
    }

    public void setGoalUnit(GoalUnit goalUnit) {
        this.goalUnit = goalUnit;
    }

    private String getImageWithSize(String size) {
        if (this.profilePic != null) {
            int index = this.profilePic.lastIndexOf(".");
            String imgPrefix = this.profilePic.substring(0, index);
            String picExtension = this.profilePic.substring(index);
            return new StringBuilder(imgPrefix).append("_").append(size).append(picExtension).toString();
        }
        return this.profilePic;
    }

    @Override
    public String toString() {
        return "Profile{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", fullName='" + fullname + '\'' +
                ", bio='" + bio + '\'' +
                ", city='" + city + '\'' +
                ", goal=" + goal +
                ", country='" + country + '\'' +
                ", profilePic='" + profilePic + '\'' +
                ", registeredOn=" + registeredOn +
                '}';
    }
}
