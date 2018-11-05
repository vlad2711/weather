package com.kram.vlad.weather.models;

public class Time_zone {
    private String localtime;

    private String utcOffset;

    public String getLocaltime() {
        return localtime;
    }

    public void setLocaltime(String localtime) {
        this.localtime = localtime;
    }

    public String getUtcOffset() {
        return utcOffset;
    }

    public void setUtcOffset(String utcOffset) {
        this.utcOffset = utcOffset;
    }

    @Override
    public String toString() {
        return "ClassPojo [localtime = " + localtime + ", utcOffset = " + utcOffset + "]";
    }
}

			