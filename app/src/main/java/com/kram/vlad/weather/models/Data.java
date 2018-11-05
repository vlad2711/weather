package com.kram.vlad.weather.models;

public class Data {
    private Time_zone[] time_zone;

    private Current_condition[] current_condition;

    private Request[] request;

    private Weather[] weather;

    private ClimateAverages[] ClimateAverages;

    public Time_zone[] getTime_zone() {
        return time_zone;
    }

    public void setTime_zone(Time_zone[] time_zone) {
        this.time_zone = time_zone;
    }

    public Current_condition[] getCurrent_condition() {
        return current_condition;
    }

    public void setCurrent_condition(Current_condition[] current_condition) {
        this.current_condition = current_condition;
    }

    public Request[] getRequest() {
        return request;
    }

    public void setRequest(Request[] request) {
        this.request = request;
    }

    public Weather[] getWeather() {
        return weather;
    }

    public void setWeather(Weather[] weather) {
        this.weather = weather;
    }

    public ClimateAverages[] getClimateAverages() {
        return ClimateAverages;
    }

    public void setClimateAverages(ClimateAverages[] ClimateAverages) {
        this.ClimateAverages = ClimateAverages;
    }

    @Override
    public String toString() {
        return "ClassPojo [time_zone = " + time_zone + ", current_condition = " + current_condition + ", request = " + request + ", weather = " + weather + ", ClimateAverages = " + ClimateAverages + "]";
    }
}
