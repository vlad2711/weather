package com.kram.vlad.weather;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.widget.TextView;
import com.kram.vlad.weather.models.Model;

import java.util.HashMap;
import java.util.Objects;

/**
 * Created by vlad on 10.08.17.
 * Utils for app
 */

public class Utils {

    public static HashMap<String, Bitmap> WEATHERIMAGES = new HashMap<>();
    public static HashMap<String, String> WEATHERICONS = new HashMap<>();
    public static Model sModel;

    private static Typeface sQontra;
    private static Typeface sWeatherIcon;


    public void setTypeface(Context context, String fontName, TextView... textViews) {

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
    }

    public void setWeatherImage(Context context) {
        Resources res = context.getResources();
        WEATHERIMAGES.put("113", BitmapFactory.decodeResource(res, R.drawable.sunny));
        WEATHERIMAGES.put("116", BitmapFactory.decodeResource(res, R.drawable.party_cloudy));
        WEATHERIMAGES.put("119", BitmapFactory.decodeResource(res, R.drawable.cloudy));
        WEATHERIMAGES.put("122", BitmapFactory.decodeResource(res, R.drawable.overcast));
        WEATHERIMAGES.put("143", BitmapFactory.decodeResource(res, R.drawable.mist));
        WEATHERIMAGES.put("176", BitmapFactory.decodeResource(res, R.drawable.patchy_rain));
        WEATHERIMAGES.put("179", BitmapFactory.decodeResource(res, R.drawable.snow));
        WEATHERIMAGES.put("183", BitmapFactory.decodeResource(res, R.drawable.sleet));
        WEATHERIMAGES.put("185", BitmapFactory.decodeResource(res, R.drawable.freezy_drizle));
        WEATHERIMAGES.put("200", BitmapFactory.decodeResource(res, R.drawable.lightining));
        WEATHERIMAGES.put("227", BitmapFactory.decodeResource(res, R.drawable.blowing_snow));
        WEATHERIMAGES.put("230", BitmapFactory.decodeResource(res, R.drawable.blizzard));
        WEATHERIMAGES.put("248", BitmapFactory.decodeResource(res, R.drawable.fog));
        WEATHERIMAGES.put("260", BitmapFactory.decodeResource(res, R.drawable.freezing_fog));
        WEATHERIMAGES.put("263", BitmapFactory.decodeResource(res, R.drawable.patchy_rain));
        WEATHERIMAGES.put("266", BitmapFactory.decodeResource(res, R.drawable.patchy_rain));
        WEATHERIMAGES.put("281", BitmapFactory.decodeResource(res, R.drawable.freezy_drizle));
        WEATHERIMAGES.put("284", BitmapFactory.decodeResource(res, R.drawable.heavy_freazing_drezzle));
        WEATHERIMAGES.put("293", BitmapFactory.decodeResource(res, R.drawable.rain));
        WEATHERIMAGES.put("296", BitmapFactory.decodeResource(res, R.drawable.rain));
        WEATHERIMAGES.put("299", BitmapFactory.decodeResource(res, R.drawable.moderate_rain));
        WEATHERIMAGES.put("302", BitmapFactory.decodeResource(res, R.drawable.moderate_rain));
        WEATHERIMAGES.put("305", BitmapFactory.decodeResource(res, R.drawable.heavy_rain));
        WEATHERIMAGES.put("308", BitmapFactory.decodeResource(res, R.drawable.heavy_rain));
        WEATHERIMAGES.put("311", BitmapFactory.decodeResource(res, R.drawable.freezy_drizle));

    }

    public static void getIcons() {
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
    }

    public double getScreenSize(AppCompatActivity activity) {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        double wi = (double) width / (double) dm.xdpi;
        double hi = (double) height / (double) dm.ydpi;

        double x = Math.pow(wi, 2);
        double y = Math.pow(hi, 2);

        return Math.sqrt(x + y);
    }

}