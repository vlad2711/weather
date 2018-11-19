package com.kram.vlad.weather.recycler_view_models;

/**
 * Created by vlad on 14.08.17.
 * WeatherModel for recyclerView
 */

public class City {

    public String latitude;
    public String longitude;
    public String name;
    public String state;

    public City(String latitude, String longitude, String name, String state) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.name = name;
        this.state = state;
    }
}
