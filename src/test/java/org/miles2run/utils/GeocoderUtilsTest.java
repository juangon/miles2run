package org.miles2run.utils;

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

    @Test
    public void testFindLngLat() throws Exception {
        String city = "Gurgaon";
        String country = "IN";
        double[] lngLat = GeocoderUtils.lngLat(city, country);
        Assert.assertNotNull(lngLat);
        Assert.assertArrayEquals(new double[]{77.0266383, 28.4594965}, lngLat, 0.0d);

    }
}
