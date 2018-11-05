package com.kram.vlad.weather.models;

/**
 * Created by vlad on 17.02.17.
 */
public class Current_condition {
    private String cloudcover;

    private String observation_time;

    private String pressure;

    private String visibility;

    private String temp_C;

    private String FeelsLikeC;

    private String temp_F;

    private String windspeedMiles;

    private String precipMM;

    private String winddirDegree;

    private String winddir16Point;

    private WeatherIconUrl[] weatherIconUrl;

    private Lang_ru[] lang_ru;

    private String humidity;

    private String FeelsLikeF;

    private String windspeedKmph;

    private String weatherCode;

    private WeatherDesc[] weatherDesc;

    public String getCloudcover() {
        return cloudcover;
    }

    public void setCloudcover(String cloudcover) {
        this.cloudcover = cloudcover;
    }

    public String getObservation_time() {
        return observation_time;
    }

    public void setObservation_time(String observation_time) {
        this.observation_time = observation_time;
    }

    public String getPressure() {
        return pressure;
    }

    public void setPressure(String pressure) {
        this.pressure = pressure;
    }

    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

    public String getTemp_C() {
        return temp_C;
    }

    public void setTemp_C(String temp_C) {
        this.temp_C = temp_C;
    }

    public String getFeelsLikeC() {
        return FeelsLikeC;
    }

    public void setFeelsLikeC(String FeelsLikeC) {
        this.FeelsLikeC = FeelsLikeC;
    }

    public String getTemp_F() {
        return temp_F;
    }

    public void setTemp_F(String temp_F) {
        this.temp_F = temp_F;
    }

    public String getWindspeedMiles() {
        return windspeedMiles;
    }

    public void setWindspeedMiles(String windspeedMiles) {
        this.windspeedMiles = windspeedMiles;
    }

    public String getPrecipMM() {
        return precipMM;
    }

    public void setPrecipMM(String precipMM) {
        this.precipMM = precipMM;
    }

    public String getWinddirDegree() {
        return winddirDegree;
    }

    public void setWinddirDegree(String winddirDegree) {
        this.winddirDegree = winddirDegree;
    }

    public String getWinddir16Point() {
        return winddir16Point;
    }

    public void setWinddir16Point(String winddir16Point) {
        this.winddir16Point = winddir16Point;
    }

    public WeatherIconUrl[] getWeatherIconUrl() {
        return weatherIconUrl;
    }

    public void setWeatherIconUrl(WeatherIconUrl[] weatherIconUrl) {
        this.weatherIconUrl = weatherIconUrl;
    }

    public Lang_ru[] getLang_ru() {
        return lang_ru;
    }

    public void setLang_ru(Lang_ru[] lang_ru) {
        this.lang_ru = lang_ru;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getFeelsLikeF() {
        return FeelsLikeF;
    }

    public void setFeelsLikeF(String FeelsLikeF) {
        this.FeelsLikeF = FeelsLikeF;
    }

    public String getWindspeedKmph() {
        return windspeedKmph;
    }

    public void setWindspeedKmph(String windspeedKmph) {
        this.windspeedKmph = windspeedKmph;
    }

    public String getWeatherCode() {
        return weatherCode;
    }

    public void setWeatherCode(String weatherCode) {
        this.weatherCode = weatherCode;
    }

    public WeatherDesc[] getWeatherDesc() {
        return weatherDesc;
    }

    public void setWeatherDesc(WeatherDesc[] weatherDesc) {
        this.weatherDesc = weatherDesc;
    }

    @Override
    public String toString() {
        return "ClassPojo [cloudcover = " + cloudcover + ", observation_time = " + observation_time + ", pressure = " + pressure + ", visibility = " + visibility + ", temp_C = " + temp_C + ", FeelsLikeC = " + FeelsLikeC + ", temp_F = " + temp_F + ", windspeedMiles = " + windspeedMiles + ", precipMM = " + precipMM + ", winddirDegree = " + winddirDegree + ", winddir16Point = " + winddir16Point + ", weatherIconUrl = " + weatherIconUrl + ", lang_ru = " + lang_ru + ", humidity = " + humidity + ", FeelsLikeF = " + FeelsLikeF + ", windspeedKmph = " + windspeedKmph + ", weatherCode = " + weatherCode + ", weatherDesc = " + weatherDesc + "]";
    }
}
