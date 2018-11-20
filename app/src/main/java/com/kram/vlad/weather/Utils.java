package com.kram.vlad.weather;

import com.kram.vlad.weather.models.WeatherModel;

import java.util.Calendar;
import java.util.HashMap;

/**
 * Created by vlad on 10.08.17.
 * Utils for app
 */

public class Utils {

    public static HashMap<String, Integer> WEATHERIMAGES = new HashMap<>();
    public static HashMap<String, WeatherModel> sWeatherModels = new HashMap<>();

    public static void setWeatherImage() {
        WEATHERIMAGES.put("113", R.drawable.sunny);
        WEATHERIMAGES.put("116", R.drawable.party_cloudy);
        WEATHERIMAGES.put("119", R.drawable.overcast);
        WEATHERIMAGES.put("122", R.drawable.overcast);
        WEATHERIMAGES.put("143", R.drawable.mist);
        WEATHERIMAGES.put("176", R.drawable.rain);
        WEATHERIMAGES.put("179", R.drawable.snow);
        WEATHERIMAGES.put("183", R.drawable.sleet);
        WEATHERIMAGES.put("185", R.drawable.sleet);
        WEATHERIMAGES.put("200", R.drawable.lightinning);
        WEATHERIMAGES.put("227", R.drawable.blowing_snow);
        WEATHERIMAGES.put("230", R.drawable.blizzard);
        WEATHERIMAGES.put("248", R.drawable.fog);
        WEATHERIMAGES.put("260", R.drawable.fog);
        WEATHERIMAGES.put("263", R.drawable.rain);
        WEATHERIMAGES.put("266", R.drawable.rain);
        WEATHERIMAGES.put("281", R.drawable.rain);
        WEATHERIMAGES.put("284", R.drawable.rain);
        WEATHERIMAGES.put("293", R.drawable.rain);
        WEATHERIMAGES.put("296", R.drawable.rain);
        WEATHERIMAGES.put("299", R.drawable.moderate_rain);
        WEATHERIMAGES.put("302", R.drawable.moderate_rain);
        WEATHERIMAGES.put("305", R.drawable.heavy_rain);
        WEATHERIMAGES.put("308", R.drawable.heavy_rain);
        WEATHERIMAGES.put("311", R.drawable.rain);
        WEATHERIMAGES.put("314", R.drawable.moderate_rain);
        WEATHERIMAGES.put("317", R.drawable.sleet);
        WEATHERIMAGES.put("320", R.drawable.sleet);
        WEATHERIMAGES.put("323", R.drawable.snow);
        WEATHERIMAGES.put("326", R.drawable.snow);
        WEATHERIMAGES.put("329", R.drawable.snow);
        WEATHERIMAGES.put("332", R.drawable.snow);
        WEATHERIMAGES.put("335", R.drawable.snow);
        WEATHERIMAGES.put("338", R.drawable.snow);
        WEATHERIMAGES.put("350", R.drawable.snow);
        WEATHERIMAGES.put("353", R.drawable.rain);
        WEATHERIMAGES.put("356", R.drawable.heavy_rain);
        WEATHERIMAGES.put("359", R.drawable.rain);
        WEATHERIMAGES.put("362", R.drawable.sleet);
        WEATHERIMAGES.put("365", R.drawable.sleet);
        WEATHERIMAGES.put("368", R.drawable.snow);
        WEATHERIMAGES.put("371", R.drawable.snow);
        WEATHERIMAGES.put("374", R.drawable.snow);
        WEATHERIMAGES.put("377", R.drawable.snow);
        WEATHERIMAGES.put("386", R.drawable.lightinning);
        WEATHERIMAGES.put("389", R.drawable.lightinning);
        WEATHERIMAGES.put("392", R.drawable.snow);
        WEATHERIMAGES.put("395", R.drawable.snow);
    }

    public static int getTime(int hmm){
        int time = Calendar.getInstance().get(Calendar.HOUR_OF_DAY) + hmm/100;
        if(time > 24) time -= 24;

        return time;
    }
}