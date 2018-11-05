package com.kram.vlad.weather.activitys;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.google.gson.reflect.TypeToken;
import com.kram.vlad.weather.App;
import com.kram.vlad.weather.Constants;
import com.kram.vlad.weather.R;
import com.kram.vlad.weather.Utils;
import com.kram.vlad.weather.adapters.CityNamesAdapter;
import com.kram.vlad.weather.adapters.DateAdapter;
import com.kram.vlad.weather.api.IWeatherProvider;
import com.kram.vlad.weather.callbacks.CityChooseCallback;
import com.kram.vlad.weather.callbacks.ForecastDateCallback;
import com.kram.vlad.weather.callbacks.UpdateItemCallback;
import com.kram.vlad.weather.geolocation.GeoLocationCallback;
import com.kram.vlad.weather.geolocation.GeoLocationProvider;
import com.kram.vlad.weather.models.Current_condition;
import com.kram.vlad.weather.models.Hourly;
import com.kram.vlad.weather.models.Model;
import com.kram.vlad.weather.models.Weather;
import com.kram.vlad.weather.recycler_view_models.City;
import com.kram.vlad.weather.settings.Preferences;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * MainActivity of app
 */

public class MainActivity extends AppCompatActivity implements
        GeoLocationCallback, PlaceSelectionListener, UpdateItemCallback,
        CityChooseCallback, View.OnClickListener, ForecastDateCallback {

    public static final String TAG = MainActivity.class.getSimpleName();
    public static final String APP_PREFERENCES = "cityNames";

    public static int sRecyclerViewDateCurrentPosition = 0;
    public static int sRecyclerViewCityCurrentPosition = 0;

    @Inject IWeatherProvider mWeatherProvider;

    @BindView(R.id.drawerLayout) DrawerLayout drawer;
    @BindView(R.id.drawerRecyclerView) RecyclerView recyclerView;
    @BindView(R.id.drawerRecyclerViewDate) RecyclerView dateRecyclerView;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.progressBar) ProgressBar progressBar;
    @BindView(R.id.weatherLayout) RelativeLayout weatherLayout;
    @BindView(R.id.weatherImage) ImageView weatherImage;
    @BindView(R.id.temperature) TextView temperature;

    @BindView(R.id.one) RelativeLayout one;
    @BindView(R.id.two) RelativeLayout two;
    @BindView(R.id.three) RelativeLayout three;
    @BindView(R.id.four) RelativeLayout four;
    @BindView(R.id.five) RelativeLayout five;
    @BindView(R.id.six) RelativeLayout six;

    @BindView(R.id.iconOne) TextView iconOne;
    @BindView(R.id.iconTwo) TextView iconTwo;
    @BindView(R.id.iconThree) TextView iconThree;
    @BindView(R.id.iconFour) TextView iconFour;
    @BindView(R.id.iconFive) TextView iconFive;
    @BindView(R.id.iconSix) TextView iconSix;

    private SharedPreferences mSharedPreferences;
    private GeoLocationProvider mGeoLocationProvider;
    private PlaceAutocompleteFragment mAutocompleteFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ((App) getApplicationContext()).getComponent().inject(this);
        ButterKnife.bind(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkPermissions(Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION);
        }

        getOrientation();

        toolbarInit();
        initUserInterface();
        geolocation(48.510473, 32.251812);

        initGeolocation();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (Preferences.CITYS.size() > 0) {
            Preferences.CITYS.remove(0);
            createSharedPreferences();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cityChoose:
                drawerLayoutToggle(Gravity.LEFT, Gravity.RIGHT);
                break;

            case R.id.navigationRight:
                drawerLayoutToggle(Gravity.RIGHT, Gravity.LEFT);
                break;

            case R.id.one:
                startMoreActivity(1);
                break;

            case R.id.two:
                startMoreActivity(2);
                break;

            case R.id.three:
                startMoreActivity(3);
                break;

            case R.id.four:
                startMoreActivity(4);
                break;

            case R.id.five:
                startMoreActivity(5);
                break;

            case R.id.six:
                startMoreActivity(6);
                break;
            case R.id.weatherImage:
                startMoreActivity(0);
                break;
        }
    }

    public void handleResponse(Model model) {
        if (model != null) {
            runOnUiThread(() -> {
                Utils.sModel = model;
                initializeRecyclerViewDate(model.getData().getWeather());
                weatherLayout.setVisibility(View.VISIBLE);

                Current_condition current_condition = model.getData().getCurrent_condition()[0];

                weatherImage.setImageBitmap(Utils.WEATHERIMAGES.get(current_condition.getWeatherCode()));
                temperature.setText(String.format(getResources().getString(R.string.degrees), current_condition.getTemp_C()));

                Hourly[] hourly = model.getData().getWeather()[0].getHourly();
               setWeatherForecast(hourly);

                weatherLayout.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
            });


        } else {
            Log.d(TAG, "null");

          //  Toast toast = new Toast(getApplicationContext());
           // toast.setText("Failed to download data.Try later");
          //  toast.show();
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private void checkPermissions(String...permissions) {
        ActivityCompat.requestPermissions(this, permissions, Constants.PERMISSION_REQUEST_CODE);
    }



    @Override
    public void geolocation(double latitude, double longitude) {

        getWeather(createCallFromGeoPosition(mWeatherProvider, String.valueOf(latitude), String.valueOf(longitude)));

        try {

            Geocoder gcd = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = gcd.getFromLocation(latitude, longitude, 1);
            String cityName = addresses.get(0).getLocality();
            //String cityName = mGeoLocationProvider.getAddress(latitude, longitude, this);

            Preferences.CITYS.add(new City(String.valueOf(latitude),
                    String.valueOf(longitude),
                    cityName));
            if (mAutocompleteFragment != null && cityName != null)
                mAutocompleteFragment.setText(cityName);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateItemCallback() {
        initializeRecyclerView();
    }

    @Override
    public void cityChooseCallback(City city, int position) {

        sRecyclerViewCityCurrentPosition = position;

        if (mAutocompleteFragment != null) {
            try {
                mAutocompleteFragment.setText(mGeoLocationProvider.getAddress(Double.parseDouble(city.latitude),
                        Double.parseDouble(city.longitude),
                        this));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        getWeather(createCallFromGeoPosition(mWeatherProvider, String.valueOf(city.latitude), String.valueOf(city.longitude)));

    }

    @Override
    public void forecastDateCallback(Weather[] weather, int position) {
        weatherImage.setImageBitmap(Utils.WEATHERIMAGES.get(weather[position].getHourly()[0].getWeatherCode()));
        temperature.setText(String.format(getResources().getString(R.string.degrees), weather[position].getHourly()[0].getTempC()));
        setWeatherForecast(weather[position].getHourly());
        initializeRecyclerViewDate(weather);

    }

    @Override
    public void onPlaceSelected(Place place) {

        Preferences.CITYS.add(new City(
                String.valueOf(place.getLatLng().latitude),
                String.valueOf(place.getLatLng().longitude),
                (String) place.getName()));
        getWeather(createCallFromGeoPosition(mWeatherProvider, String.valueOf(place.getLatLng().latitude), String.valueOf(place.getLatLng().longitude)));


        this.updateItemCallback();

        Log.i(TAG, "Place: " + place.getName());
    }

    @Override
    public void onError(Status status) {
        Log.i(TAG, "An error occurred: " + status);
    }

    @Override
    public void onBackPressed() {

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (drawer.isDrawerOpen(GravityCompat.END)) {
                drawer.closeDrawer(GravityCompat.END);
            } else {
                super.onBackPressed();
            }
        }
    }

    private void setWeatherForecast(Hourly[] hourly) {

        iconOne.setText(Utils.WEATHERICONS.get(hourly[1].getWeatherCode()));
        iconTwo.setText(Utils.WEATHERICONS.get(hourly[2].getWeatherCode()));
        iconThree.setText(Utils.WEATHERICONS.get(hourly[3].getWeatherCode()));
        iconFour.setText(Utils.WEATHERICONS.get(hourly[4].getWeatherCode()));
        iconFive.setText(Utils.WEATHERICONS.get(hourly[5].getWeatherCode()));

    }

    private void initUserInterface() {
        setTypefacesAndIcons();
        initializeRecyclerView();
        initializeToolbarButton();
        setOnclickListeners(one,
                two,
                three,
                four,
                five,
                six,
                weatherImage);
        initializeAutoCompleteFragment();
        getCitysFromSharedPreferences();
    }

    private void initGeolocation() {
        mGeoLocationProvider = new GeoLocationProvider(this);
    }

    private void toolbarInit() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    private void getOrientation() {
        if (new Utils().getScreenSize(this) < Preferences.PORTRAITSCREENSIZE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
    }

    private void setTypefacesAndIcons() {

        Utils utils = new Utils();

        utils.setTypeface(getApplicationContext(),
                "icons",
                iconOne,
                iconTwo,
                iconThree,
                iconSix,
                iconFour,
                iconFive);

        utils.setTypeface(getApplicationContext(),
                "qontra",
                temperature);
        utils.setWeatherImage(getApplicationContext());
        utils.getIcons();
    }

    private Call<Model> createCallFromGeoPosition(IWeatherProvider weatherProvider, String latitude, String longitude) {
        Log.d(TAG, "call");

        HashMap<String, String> mapJson = new HashMap<>();
        mapJson.put("key", Constants.WEATHER_API_KEY);
        mapJson.put("q", latitude + "," + longitude);
        mapJson.put("num_of_days", "14");
        mapJson.put("date", "today");
        mapJson.put("format", "json");
        mapJson.put("show_comments", "yes");
        mapJson.put("showlocaltime", "yes");
        mapJson.put("lang", "ru");
        mapJson.put("tp", "1");

        weatherLayout.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);



        return weatherProvider.getWeather(Constants.WEATHER_API_KEY,
                latitude + "," + longitude,
                "14",
                "today",
                "json",
                "yes",
                "yes",
                "ru",
                "1");
    }

    private void initializeToolbarButton() {
        toolbar.findViewById(R.id.cityChoose).setOnClickListener(this);
        toolbar.findViewById(R.id.navigationRight).setOnClickListener(this);
    }

    private void initializeRecyclerView() {
        recyclerView.setAdapter(new CityNamesAdapter(this, getApplicationContext()));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    private void initializeRecyclerViewDate(Weather[] weather) {
        dateRecyclerView.setAdapter(new DateAdapter(weather, this, getApplicationContext()));
        dateRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void initializeAutoCompleteFragment() {
        mAutocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        mAutocompleteFragment.setOnPlaceSelectedListener(this);
    }

    private void getCitysFromSharedPreferences() {
        mSharedPreferences = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);

        if (mSharedPreferences.contains("Citys")) {
            Gson gson = new Gson();
            String json = mSharedPreferences.getString("Citys", "");
            Preferences.CITYS = gson.fromJson(json, new TypeToken<ArrayList<City>>() {}.getType());
            Log.d(TAG, String.valueOf(Preferences.CITYS));
        } else {
            Preferences.CITYS = new ArrayList<>();
        }
    }

    private void createSharedPreferences() {
        Gson gson = new Gson();
        Log.d(TAG, gson.toJson(Preferences.CITYS));
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString("Citys", gson.toJson(Preferences.CITYS));
        editor.apply();
    }

    private void setOnclickListeners(View... views) {
        for (View view : views) {
            view.setOnClickListener(this);
        }
    }

    private void drawerLayoutToggle(int gravityOpen, int gravityClose) {

        if (drawer.isDrawerOpen(gravityClose)) {
            drawer.closeDrawer(gravityClose);
            drawer.openDrawer(gravityOpen);
        } else {
            if (drawer.isDrawerOpen(gravityOpen)) {
                drawer.closeDrawer(gravityOpen);
            } else {
                drawer.openDrawer(gravityOpen);
            }
        }
    }

    private void getWeather(Call<Model> call){
        call.enqueue( new Callback<Model>(){

            @Override
            public void onResponse(Call<Model> call, Response<Model> response) {
                handleResponse(response.body());
            }

            @Override
            public void onFailure(Call<Model> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void startMoreActivity(int id) {
        if (Utils.sModel != null) {
            Intent intent = new Intent(this, MoreActivity.class);
            intent.putExtra("Hourly", new Gson().toJson(Utils.sModel.getData().getWeather()[sRecyclerViewDateCurrentPosition].getHourly()[id]));
            startActivity(intent);

        }
    }
}