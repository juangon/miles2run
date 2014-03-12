package org.milestogo.utils;

import com.google.code.geocoder.Geocoder;
import com.google.code.geocoder.GeocoderRequestBuilder;
import com.google.code.geocoder.model.GeocodeResponse;
import com.google.code.geocoder.model.GeocoderAddressComponent;
import com.google.code.geocoder.model.GeocoderRequest;
import com.google.code.geocoder.model.GeocoderResult;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * Created by shekhargulati on 12/03/14.
 */
public abstract class GeocoderUtils {

    public static CityAndCountry parseLocation(String location) {
        if (StringUtils.isBlank(location)) {
            return new CityAndCountry(null, null);
        }
        try {
            String city = null;
            String country = null;
            Geocoder geocoder = new Geocoder();
            GeocoderRequest geocoderRequest = new GeocoderRequestBuilder().setAddress(location).setLanguage("en").getGeocoderRequest();
            GeocodeResponse geocodeResponse = geocoder.geocode(geocoderRequest);
            List<GeocoderResult> results = geocodeResponse.getResults();
            for (GeocoderResult result : results) {
                List<GeocoderAddressComponent> addressComponents = result.getAddressComponents();
                for (GeocoderAddressComponent addressComponent : addressComponents) {
                    if (addressComponent.getTypes().contains("locality")) {
                        city = addressComponent.getShortName();
                    }
                    if (addressComponent.getTypes().contains("country")) {
                        country = addressComponent.getShortName();
                    }
                }
            }
            return new CityAndCountry(city, country);
        } catch (Exception e) {
            return new CityAndCountry(null, null);
        }

    }
}
