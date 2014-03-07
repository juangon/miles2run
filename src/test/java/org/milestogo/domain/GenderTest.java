package org.milestogo.domain;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by shekhargulati on 07/03/14.
 */
public class GenderTest {
    
    @Test
    public void testFromStringToGenderForMale() throws Exception {
        Assert.assertEquals(Gender.MALE, Gender.fromStringToGender("male"));
    }

    @Test
    public void testFromStringToGendersForFemale() throws Exception {
        Assert.assertEquals(Gender.FEMALE, Gender.fromStringToGender("female"));
    }

    @Test
    public void testCheckForInvalidValue() throws Exception {
        Assert.assertEquals(null, Gender.fromStringToGender("none"));
    }
}
