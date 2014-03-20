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

    private Share share;

    private String fullname;

    private String username;

    private String profilePic;

    public ActivityDetails(Long id, String status, long distanceCovered, GoalUnit goalUnit, Date activityDate, Share share, String fullname) {
        this.id = id;
        this.status = status;
        this.goalUnit = goalUnit;
        this.distanceCovered = distanceCovered/goalUnit.getConversion();
        this.activityDate = activityDate;
        this.share = share;
        this.fullname = fullname;
    }

    public ActivityDetails(Long id, String status, long distanceCovered, GoalUnit goalUnit, Date activityDate, Share share, String fullname, String username, String profilePic) {
        this.id = id;
        this.status = status;
        this.goalUnit = goalUnit;
        this.distanceCovered = distanceCovered/goalUnit.getConversion();
        this.activityDate = activityDate;
        this.share = share;
        this.fullname = fullname;
        this.username = username;
        this.profilePic = profilePic;
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

    public Share getShare() {
        return share;
    }

    public String getFullname() {
        return fullname;
    }

    public String getUsername() {
        return username;
    }

    public String getProfilePic() {
        return profilePic;
    }
}
