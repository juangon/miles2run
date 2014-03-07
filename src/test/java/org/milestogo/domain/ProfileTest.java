package org.milestogo.domain;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by shekhargulati on 07/03/14.
 */
public class ProfileTest {

    @Test
    public void testGetMiniProfilePic() throws Exception {
        Profile profile = new Profile();
        profile.setProfilePic("http://pbs.twimg.com/profile_images/378800000254412405/e4adcf8fb7800c3e5f8141c561cb57e4.jpeg");
        Assert.assertEquals("http://pbs.twimg.com/profile_images/378800000254412405/e4adcf8fb7800c3e5f8141c561cb57e4_mini.jpeg", profile.getMiniProfilePic());
    }

    @Test
    public void testGetBiggerProfilePic() throws Exception {
        Profile profile = new Profile();
        profile.setProfilePic("http://pbs.twimg.com/profile_images/378800000254412405/e4adcf8fb7800c3e5f8141c561cb57e4.jpeg");
        Assert.assertEquals("http://pbs.twimg.com/profile_images/378800000254412405/e4adcf8fb7800c3e5f8141c561cb57e4_bigger.jpeg", profile.getBiggerProfilePic());
    }
}
