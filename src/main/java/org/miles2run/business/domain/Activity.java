package org.miles2run.business.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

/**
 * Created by shekhargulati on 04/03/14.
 */
@Entity
@NamedQueries({
        @NamedQuery(name = "Activity.findAll", query = "SELECT NEW org.miles2run.business.domain.ActivityDetails(a.id,a.status,a.distanceCovered,a.goalUnit,a.activityDate,a.share,a.postedBy.fullname,a.postedBy.username,a.postedBy.profilePic) FROM Activity a WHERE a.postedBy =:postedBy ORDER BY a.activityDate DESC"),
        @NamedQuery(name = "Activity.findById", query = "SELECT new org.miles2run.business.domain.ActivityDetails(a.id,a.status,a.distanceCovered,a.goalUnit,a.activityDate,a.share,a.postedBy.fullname,a.duration) from Activity a where a.id =:id"),
        @NamedQuery(name = "Activity.countByProfile", query = "SELECT COUNT(a) FROM Activity a WHERE a.postedBy =:profile")

})
public class Activity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Size(max = 140)
    private String status;

    @NotNull
    @Enumerated(EnumType.STRING)
    private GoalUnit goalUnit;

    @NotNull
    private long distanceCovered;

    @Column(updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date postedAt = new Date();

    @Temporal(TemporalType.DATE)
    private Date activityDate;

    @ManyToOne
    private Profile postedBy;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Share share;

    private long duration;

    public Activity() {
    }


    public Activity(String status, long distanceCovered, Profile postedBy) {
        this.status = status;
        this.distanceCovered = distanceCovered;
        this.postedBy = postedBy;
    }

    public Activity(Long id, String status, long distanceCovered) {
        this.id = id;
        this.status = status;
        this.distanceCovered = distanceCovered;
    }

    public Activity(Long id, String status, long distanceCovered, Date postedAt) {
        this.id = id;
        this.status = status;
        this.distanceCovered = distanceCovered;
        this.postedAt = postedAt;
    }

    public Activity(Date activityDate, long distanceCovered, GoalUnit goalUnit) {
        this.activityDate = activityDate;
        this.distanceCovered = distanceCovered;
        this.goalUnit = goalUnit;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getDistanceCovered() {
        return distanceCovered;
    }

    public void setDistanceCovered(long distanceCovered) {
        this.distanceCovered = distanceCovered;
    }

    public GoalUnit getGoalUnit() {
        return goalUnit;
    }

    public void setGoalUnit(GoalUnit goalUnit) {
        this.goalUnit = goalUnit;
    }

    public Date getPostedAt() {
        return postedAt;
    }

    public void setPostedAt(Date postedAt) {
        this.postedAt = postedAt;
    }

    public Date getActivityDate() {
        return activityDate;
    }

    public void setActivityDate(Date activityDate) {
        this.activityDate = activityDate;
    }

    public Profile getPostedBy() {
        return postedBy;
    }

    public void setPostedBy(Profile postedBy) {
        this.postedBy = postedBy;
    }

    public Share getShare() {
        return share;
    }

    public void setShare(Share share) {
        this.share = share;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }
}
