package com.kram.vlad.weather.recycler_view_models;

/**
 * Created by vlad on 14.08.17.
 * Model for recyclerView
 */

public class City {

    public String latitude;
    public String longitude;
    public String name;

    public City(String latitude, String longitude, String name) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.name = name;
    }
}
