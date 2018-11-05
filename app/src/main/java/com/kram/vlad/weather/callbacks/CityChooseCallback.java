package com.kram.vlad.weather.callbacks;

import com.kram.vlad.weather.recycler_view_models.City;

/**
 * Created by vlad on 16.08.17.
 * Callback, call when user set city forecast
 */

public interface CityChooseCallback {
    void cityChooseCallback(City city, int position);
}
