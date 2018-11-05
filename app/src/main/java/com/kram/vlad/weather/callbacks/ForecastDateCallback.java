package com.kram.vlad.weather.callbacks;


import com.kram.vlad.weather.models.Weather;

/**
 * Created by vlad on 18.08.17.
 * Callback, call when user set date of forecast
 */

public interface ForecastDateCallback {
    void forecastDateCallback(Weather[] weather, int position);
}
