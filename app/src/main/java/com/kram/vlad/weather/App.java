package com.kram.vlad.weather;

import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

/**
 * Created by vlad on 25.07.17.
 * Custom Application class for app
 */

public class App extends MultiDexApplication {

    @Override
    public void onCreate() {
        MultiDex.install(this);
        super.onCreate();
    }
}
