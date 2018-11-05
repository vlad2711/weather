package com.kram.vlad.weather.activitys;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.gson.Gson;
import com.kram.vlad.weather.R;
import com.kram.vlad.weather.Utils;
import com.kram.vlad.weather.models.Hourly;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by vlad on 19.08.17.
 * Activity, used when user tap in one of button in MainActivity
 */

public class MoreActivity extends AppCompatActivity {

    public static final String TAG = MoreActivity.class.getSimpleName();

    @BindView(R.id.wind) TextView wind;
    @BindView(R.id.precip) TextView precip;
    @BindView(R.id.pressure) TextView pressure;
    @BindView(R.id.temperatureMore) TextView temperature;
    @BindView(R.id.humidity) TextView humidity;
    @BindView(R.id.value) TextView value;
    @BindView(R.id.imageView2) ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.more);
        ButterKnife.bind(this);

        Hourly hourly = new Gson().fromJson(getIntent().getStringExtra("Hourly"), Hourly.class);

        if(hourly != null)
        initInterface(hourly);
    }



    private void initInterface(Hourly hourly) {

        Resources res = getResources();

        Calendar calendar = GregorianCalendar.getInstance();
        calendar.set(Calendar.HOUR, 0);

        wind.setText(String.format(res.getString(R.string.wind), hourly.getWindspeedKmph()));
        precip.setText(String.format(res.getString(R.string.precip), hourly.getPrecipMM()));
        pressure.setText(String.format(res.getString(R.string.pressure), hourly.getPressure()));
        temperature.setText(String.format(res.getString(R.string.degrees), hourly.getTempC()));
        humidity.setText(String.format(res.getString(R.string.humidity), hourly.getHumidity()));
        value.setText(hourly.getLang_ru()[0].getValue());
        image.setImageBitmap(Utils.WEATHERIMAGES.get(hourly.getWeatherCode()));

        new Utils().setTypeface(this, "qontra",
                wind,
                precip,
                pressure,
                temperature,
                humidity,
                value);
    }
}
