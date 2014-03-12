package org.milestogo.utils;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by shekhargulati on 12/03/14.
 */
public class GeocoderUtilsTest {
    @Test
    public void testParseLocation() throws Exception {
        String location = "San Jose, CA";
        CityAndCountry cityAndCountry = GeocoderUtils.parseLocation(location);
        Assert.assertEquals("San Jose", cityAndCountry.getCity());
        Assert.assertEquals("US", cityAndCountry.getCountry());
    }

    @Test
    public void testCityAndCountryShouldBeNullWhenLocationIsNull() throws Exception {
        String location = null;
        CityAndCountry cityAndCountry = GeocoderUtils.parseLocation(location);
        Assert.assertEquals(null, cityAndCountry.getCity());
        Assert.assertEquals(null, cityAndCountry.getCountry());
    }

    @Test
    public void testCityAndCountryShouldBeNullWhenLocationIsInvalid() throws Exception {
        String location = "xncnefngnk";
        CityAndCountry cityAndCountry = GeocoderUtils.parseLocation(location);
        Assert.assertEquals(null, cityAndCountry.getCity());
        Assert.assertEquals(null, cityAndCountry.getCountry());
    }
}