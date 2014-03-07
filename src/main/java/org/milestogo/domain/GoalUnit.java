package org.milestogo.domain;

/**
 * Created by shekhargulati on 07/03/14.
 */
public enum GoalUnit {
    KMS("kms", 1000), MILES("miles", 1609);

    private final String unit;
    private final long conversion;


    GoalUnit(String unit, long conversion) {
        this.unit = unit;
        this.conversion = conversion;
    }

    public String getUnit() {
        return unit;
    }

    public static GoalUnit fromStringToGoalUnit(String goalUnit) {
        GoalUnit[] values = GoalUnit.values();
        for (GoalUnit goal : values) {
            if (goal.unit.equals(goalUnit)) {
                return goal;
            }
        }
        return null;
    }

    public long getConversion() {
        return conversion;
    }
}