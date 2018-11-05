package com.kram.vlad.weather.models;

public class Astronomy {
    private String moonset;

    private String sunset;

    private String sunrise;

    private String moonrise;

    public String getMoonset() {
        return moonset;
    }

    public void setMoonset(String moonset) {
        this.moonset = moonset;
    }

    public String getSunset() {
        return sunset;
    }

    public void setSunset(String sunset) {
        this.sunset = sunset;
    }

    public String getSunrise() {
        return sunrise;
    }

    public void setSunrise(String sunrise) {
        this.sunrise = sunrise;
    }

    public String getMoonrise() {
        return moonrise;
    }

    public void setMoonrise(String moonrise) {
        this.moonrise = moonrise;
    }

    @Override
    public String toString() {
        return "ClassPojo [moonset = " + moonset + ", sunset = " + sunset + ", sunrise = " + sunrise + ", moonrise = " + moonrise + "]";
    }
}