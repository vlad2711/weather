package com.kram.vlad.weather.components;

import com.kram.vlad.weather.activitys.MainActivity;
import com.kram.vlad.weather.modules.WeatherInfoTaskModule;

import dagger.Component;

/**
 * Created by vlad on 25.07.17.
 * Dagger Component
 */

@Component(modules = {WeatherInfoTaskModule.class})
public interface RetrofitComponent {
    void inject(MainActivity activity);
}
