package com.kram.vlad.weather;

import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.kram.vlad.weather.components.DaggerRetrofitComponent;
import com.kram.vlad.weather.components.RetrofitComponent;

/**
 * Created by vlad on 25.07.17.
 * Custom Application class for app
 */

public class App extends MultiDexApplication {
    private static RetrofitComponent component;

    @Override
    public void onCreate() {
        MultiDex.install(this);
        super.onCreate();
        component = DaggerRetrofitComponent.create();

    }

    public RetrofitComponent getComponent() {
        return component;
    }
}
