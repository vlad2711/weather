package com.kram.vlad.weather.models;

public class Month {
    private String index;

    private String avgMinTemp;

    private String name;

    private String absMaxTemp;

    private String avgMinTemp_F;

    private String absMaxTemp_F;

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getAvgMinTemp() {
        return avgMinTemp;
    }

    public void setAvgMinTemp(String avgMinTemp) {
        this.avgMinTemp = avgMinTemp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAbsMaxTemp() {
        return absMaxTemp;
    }

    public void setAbsMaxTemp(String absMaxTemp) {
        this.absMaxTemp = absMaxTemp;
    }

    public String getAvgMinTemp_F() {
        return avgMinTemp_F;
    }

    public void setAvgMinTemp_F(String avgMinTemp_F) {
        this.avgMinTemp_F = avgMinTemp_F;
    }

    public String getAbsMaxTemp_F() {
        return absMaxTemp_F;
    }

    public void setAbsMaxTemp_F(String absMaxTemp_F) {
        this.absMaxTemp_F = absMaxTemp_F;
    }

    @Override
    public String toString() {
        return "ClassPojo [index = " + index + ", avgMinTemp = " + avgMinTemp + ", name = " + name + ", absMaxTemp = " + absMaxTemp + ", avgMinTemp_F = " + avgMinTemp_F + ", absMaxTemp_F = " + absMaxTemp_F + "]";
    }
}