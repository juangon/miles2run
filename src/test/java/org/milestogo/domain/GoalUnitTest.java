package org.milestogo.domain;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by shekhargulati on 07/03/14.
 */
public class GoalUnitTest {
    @Test
    public void testFromStringToGoalUnitForMiles() throws Exception {
        Assert.assertEquals(GoalUnit.MILES, GoalUnit.fromStringToGoalUnit("miles"));
    }

    @Test
    public void testFromStringToGoalUnitsForKms() throws Exception {
        Assert.assertEquals(GoalUnit.KMS, GoalUnit.fromStringToGoalUnit("kms"));
    }

    @Test
    public void testCheckForInvalidValue() throws Exception {
        Assert.assertEquals(null, GoalUnit.fromStringToGoalUnit("kmsz"));
    }
}