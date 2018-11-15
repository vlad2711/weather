package com.kram.vlad.weather;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.widget.TextView;
import com.kram.vlad.weather.models.WeatherModel;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Objects;

/**
 * Created by vlad on 10.08.17.
 * Utils for app
 */

public class Utils {

    public static HashMap<String, Integer> WEATHERIMAGES = new HashMap<>();
   // public static HashMap<String, String> WEATHERICONS = new HashMap<>();
    public static HashMap<String, WeatherModel> sWeatherModels = new HashMap<>();


    /*public void setTypeface(Context context, String fontName, TextView... textViews) {

        boolean isQontra = false;
        boolean isIcons = false;
        if (sQontra == null && Objects.equals(fontName, "qontra")) {
            sQontra = Typeface.createFromAsset(context.getAssets(), "fonts/Qontra.otf");
            isQontra = true;
        }

        if (sWeatherIcon == null && Objects.equals(fontName, "icons")) {
            sWeatherIcon = Typeface.createFromAsset(context.getAssets(), "fonts/artill_clean_icons.otf");
            isIcons = true;
        }

        for (TextView view : textViews) {
            if (isQontra)
                view.setTypeface(sQontra);

            if (isIcons)
                view.setTypeface(sWeatherIcon);
        }
    }*/

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

    }

    /*public static void getIcons() {
        WEATHERICONS.put("113", "1");
        WEATHERICONS.put("116", "A");
        WEATHERICONS.put("119", "a");
        WEATHERICONS.put("122", "3");
        WEATHERICONS.put("143", "z");
        WEATHERICONS.put("176", "J");
        WEATHERICONS.put("179", "I");
        WEATHERICONS.put("183", "V");
        WEATHERICONS.put("185", "U");
        WEATHERICONS.put("200", "Q");
        WEATHERICONS.put("227", ",");
        WEATHERICONS.put("230", "I");
        WEATHERICONS.put("248", "…");
        WEATHERICONS.put("260", "E");
        WEATHERICONS.put("263", "M");
        WEATHERICONS.put("266", "M");
        WEATHERICONS.put("281", "O");
        WEATHERICONS.put("284", "O");
        WEATHERICONS.put("293", "M");
        WEATHERICONS.put("296", "M");
        WEATHERICONS.put("299", "K");
        WEATHERICONS.put("302", "K");
        WEATHERICONS.put("305", "K");
        WEATHERICONS.put("308", "K");
        WEATHERICONS.put("311", "O");
    }*/

    public static int getTime(int hmm){
        int time = Calendar.getInstance().get(Calendar.HOUR_OF_DAY) + hmm/100;
        if(time > 24) time -= 24;

        return time;
    }
}