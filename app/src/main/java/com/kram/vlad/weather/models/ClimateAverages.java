package com.kram.vlad.weather.models;

public class ClimateAverages {
    private Month[] month;

    public Month[] getMonth() {
        return month;
    }

    public void setMonth(Month[] month) {
        this.month = month;
    }

    @Override
    public String toString() {
        return "ClassPojo [month = " + month + "]";
    }
}
