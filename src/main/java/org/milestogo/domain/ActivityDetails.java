package org.milestogo.domain;

import java.util.Date;

/**
 * Created by shekhargulati on 11/03/14.
 */
public class ActivityDetails {

    private Long id;

    private String status;

    private long distanceCovered;

    private GoalUnit goalUnit;

    private Date activityDate;

    private boolean share;

    private String fullname;

    public ActivityDetails(Long id, String status, long distanceCovered, GoalUnit goalUnit, Date activityDate, boolean share, String fullname) {
        this.id = id;
        this.status = status;
        this.distanceCovered = distanceCovered;
        this.goalUnit = goalUnit;
        this.activityDate = activityDate;
        this.share = share;
        this.fullname = fullname;
    }

    public Long getId() {
        return id;
    }

    public String getStatus() {
        return status;
    }

    public long getDistanceCovered() {
        return distanceCovered;
    }

    public GoalUnit getGoalUnit() {
        return goalUnit;
    }

    public Date getActivityDate() {
        return activityDate;
    }

    public boolean isShare() {
        return share;
    }

    public String getFullname() {
        return fullname;
    }
}
