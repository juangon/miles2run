package org.milestogo.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

/**
 * Created by shekhargulati on 04/03/14.
 */
@Entity
@NamedQueries({
        @NamedQuery(name = "Activity.findAll", query = "SELECT NEW org.milestogo.domain.ActivityDetails(a.id,a.status,a.distanceCovered,a.goalUnit,a.activityDate,a.share,a.postedBy.fullname) FROM Activity a WHERE a.postedBy =:postedBy ORDER BY a.activityDate DESC")
})
public class Activity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
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

    private boolean share;

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

    public boolean isShare() {
        return share;
    }

    public void setShare(boolean share) {
        this.share = share;
    }

}
