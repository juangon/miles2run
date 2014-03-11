package org.milestogo.resources.vo;

/**
 * Created by shekhargulati on 06/03/14.
 */
public class Progress {
    private long goal;
    private long totalDistanceCovered;
    private double percentage;

    public Progress(long goal, long totalDistanceCovered) {
        this.goal = goal;
        this.totalDistanceCovered = totalDistanceCovered;
        this.percentage = ((double)totalDistanceCovered / goal) * 100 ;
        this.percentage = this.percentage > 100 ? 100 : this.percentage;
    }

    public Progress() {
    }

    public long getGoal() {
        return goal;
    }

    public void setGoal(long goal) {
        this.goal = goal;
    }

    public long getTotalDistanceCovered() {
        return totalDistanceCovered;
    }

    public void setTotalDistanceCovered(long totalDistanceCovered) {
        this.totalDistanceCovered = totalDistanceCovered;
    }

    public double getPercentage() {
        return percentage;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }
}

