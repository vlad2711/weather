package com.kram.vlad.weather.geolocation;

/**
 * Created by vlad on 14.08.17.
 * Callback, call when app get user location
 */

public interface GeoLocationCallback {
    void geolocation(double latitude, double longitude);
}
