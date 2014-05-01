package org.miles2run.business.domain;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by shekhargulati on 21/03/14.
 */
public class ProgressTest {

    @Test
    public void testGetGoal() throws Exception {
        Progress progress = new Progress(100000L, GoalUnit.KMS, 29000L, 2L);
        Assert.assertEquals(29L, progress.getPercentage());
    }
}
