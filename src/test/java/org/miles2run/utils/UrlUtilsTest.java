package org.miles2run.utils;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by shekhargulati on 10/03/14.
 */
public class UrlUtilsTest {

    @Test
    public void testRemoveProtocol() throws Exception {
        String url = "http://pbs.twimg.com/profile_images/378800000254412405/e4adcf8fb7800c3e5f8141c561cb57e4.jpeg";
        Assert.assertEquals("//pbs.twimg.com/profile_images/378800000254412405/e4adcf8fb7800c3e5f8141c561cb57e4.jpeg", UrlUtils.removeProtocol(url));
    }

}
