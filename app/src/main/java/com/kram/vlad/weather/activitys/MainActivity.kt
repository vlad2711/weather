package com.kram.vlad.weather.activitys


import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.ActivityInfo
import android.location.Geocoder
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v4.app.ActivityCompat
import android.support.v4.view.GravityCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.Gravity
import android.view.View
import com.google.gson.reflect.TypeToken
import com.kram.vlad.weather.Constants
import com.kram.vlad.weather.R
import com.kram.vlad.weather.Utils
import com.kram.vlad.weather.adapters.CityNamesAdapter
import com.kram.vlad.weather.adapters.DateAdapter
import com.kram.vlad.weather.api.IWeatherProvider
import com.kram.vlad.weather.callbacks.CityChooseCallback
import com.kram.vlad.weather.callbacks.ForecastDateCallback
import com.kram.vlad.weather.callbacks.UpdateItemCallback
import com.kram.vlad.weather.geolocation.GeoLocationCallback
import com.kram.vlad.weather.geolocation.GeoLocationProvider
import com.kram.vlad.weather.models.Hourly
import com.kram.vlad.weather.models.WeatherModel
import com.kram.vlad.weather.models.Weather
import com.kram.vlad.weather.recycler_view_models.City
import com.kram.vlad.weather.settings.Preferences
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.places.Place
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment
import com.google.android.gms.location.places.ui.PlaceSelectionListener
import com.google.gson.Gson
import com.kram.vlad.weather.adapters.TimelineAdapter

import java.io.IOException
import java.util.ArrayList
import java.util.HashMap
import java.util.Locale

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlinx.android.synthetic.main.activity_main.*


/**
 * MainActivity of app
 */

class MainActivity : AppCompatActivity(), GeoLocationCallback, PlaceSelectionListener, UpdateItemCallback, CityChooseCallback, View.OnClickListener, ForecastDateCallback {

    companion object {

        val TAG = MainActivity::class.java.simpleName

        var sRecyclerViewDateCurrentPosition = 0
        var sRecyclerViewCityCurrentPosition = 0
    }

    var mWeatherProvider: IWeatherProvider? = IWeatherProvider.create()

    private var mSharedPreferences: SharedPreferences? = null
    private var mGeoLocationProvider: GeoLocationProvider? = null
    private var mAutocompleteFragment: PlaceAutocompleteFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkPermissions(Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION)
        }


        toolbarInit()
        initUserInterface()
        //geolocation(48.510473, 32.251812)

        initGeolocation()

    }

    override fun onDestroy() {
        super.onDestroy()
        if (Preferences.CITYS.size > 0) {
            Preferences.CITYS.removeAt(0)
            createSharedPreferences()
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.cityChoose -> drawerLayoutToggle(Gravity.LEFT, Gravity.RIGHT)
            R.id.navigationRight -> drawerLayoutToggle(Gravity.RIGHT, Gravity.LEFT)
        }
    }

    fun handleResponse(weatherModel: WeatherModel?) {
        if (weatherModel != null) {
            runOnUiThread {
                Utils.sWeatherModel = weatherModel
                initializeRecyclerViewDate(weatherModel.data.weather)
                setWeatherForecast(weatherModel.data.weather[0])
                progressBar!!.visibility = View.INVISIBLE

                weather_icon.setImageResource(Utils.WEATHERIMAGES[weatherModel.data.current_condition[0].weatherCode]!!)
                weather_state.text = weatherModel.data.current_condition[0].lang_ru[0].value
                temperature.text = String.format(resources.getString(R.string.degrees), weatherModel.data.current_condition[0].temp_C)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun checkPermissions(vararg permissions: String) {
        ActivityCompat.requestPermissions(this, permissions, Constants.PERMISSION_REQUEST_CODE)
    }


    override fun geolocation(latitude: Double, longitude: Double) {

        getWeather(createCallFromGeoPosition(mWeatherProvider!!, latitude.toString(), longitude.toString()))

        try {

            val gcd = Geocoder(this, Locale.getDefault())
            val addresses = gcd.getFromLocation(latitude, longitude, 1)
            val cityName = addresses[0].locality

            city_name.text = cityName
            Preferences.CITYS.add(City(latitude.toString(),
                    longitude.toString(),
                    cityName))
            if (mAutocompleteFragment != null && cityName != null)
                mAutocompleteFragment!!.setText(cityName)

        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    override fun updateItemCallback() {
        initializeRecyclerView()
    }

    override fun cityChooseCallback(city: City, position: Int) {

        sRecyclerViewCityCurrentPosition = position

        if (mAutocompleteFragment != null) {
            try {
                mAutocompleteFragment!!.setText(mGeoLocationProvider!!.getAddress(java.lang.Double.parseDouble(city.latitude),
                        java.lang.Double.parseDouble(city.longitude),
                        this))
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }

        getWeather(createCallFromGeoPosition(mWeatherProvider!!, city.latitude.toString(), city.longitude.toString()))

    }

    override fun forecastDateCallback(weather: Array<Weather>, position: Int) {
        setWeatherForecast(weather[position])
        initializeRecyclerViewDate(weather)

    }

    override fun onPlaceSelected(place: Place) {

        Preferences.CITYS.add(City(
                place.latLng.latitude.toString(),
                place.latLng.longitude.toString(),
                place.name as String))
        getWeather(createCallFromGeoPosition(mWeatherProvider!!, place.latLng.latitude.toString(), place.latLng.longitude.toString()))


        this.updateItemCallback()

        Log.i(TAG, "Place: " + place.name)
    }

    override fun onError(status: Status) {
        Log.i(TAG, "An error occurred: $status")
    }

    override fun onBackPressed() {

        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            if (drawerLayout.isDrawerOpen(GravityCompat.END)) {
                drawerLayout.closeDrawer(GravityCompat.END)
            } else {
                super.onBackPressed()
            }
        }
    }

    private fun setWeatherForecast(weather: Weather) {
        timeline_view.layoutManager = LinearLayoutManager(this)
        timeline_view.adapter = TimelineAdapter(weather)
    }

    private fun initUserInterface() {
        setTypefacesAndIcons()
        initializeRecyclerView()
        initializeToolbarButton()

        initializeAutoCompleteFragment()
        getCitysFromSharedPreferences()
    }

    private fun initGeolocation() {
        mGeoLocationProvider = GeoLocationProvider(this)
    }

    private fun toolbarInit() {
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
    }

    private fun getOrientation() {
        requestedOrientation = if (Utils().getScreenSize(this) < Preferences.PORTRAITSCREENSIZE) {
            ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        } else {
            ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        }
    }

    private fun setTypefacesAndIcons() {

        Utils.setWeatherImage()
        Utils.getIcons()
    }

    private fun createCallFromGeoPosition(weatherProvider: IWeatherProvider, latitude: String, longitude: String): Call<WeatherModel> {
        Log.d(TAG, "call")
        progressBar!!.visibility = View.VISIBLE

        return weatherProvider.getWeather(Constants.WEATHER_API_KEY,
                "$latitude,$longitude",
                "14",
                "today",
                "json",
                "yes",
                "yes",
                "ru",
                "3")
    }

    private fun initializeToolbarButton() {
        toolbar!!.findViewById<View>(R.id.cityChoose).setOnClickListener(this)
        toolbar!!.findViewById<View>(R.id.navigationRight).setOnClickListener(this)
    }

    private fun initializeRecyclerView() {
        drawerRecyclerView.adapter = CityNamesAdapter(this, applicationContext)
        drawerRecyclerView.layoutManager = LinearLayoutManager(this)

    }

    private fun initializeRecyclerViewDate(weather: Array<Weather>) {
        drawerRecyclerViewDate.adapter = DateAdapter(weather, this, applicationContext)
        drawerRecyclerViewDate.layoutManager = LinearLayoutManager(this)
    }

    private fun initializeAutoCompleteFragment() {
        mAutocompleteFragment = fragmentManager.findFragmentById(R.id.place_autocomplete_fragment) as PlaceAutocompleteFragment

        mAutocompleteFragment!!.setOnPlaceSelectedListener(this)
    }

    private fun getCitysFromSharedPreferences() {
        mSharedPreferences = getSharedPreferences(Constants.APP_PREFERENCES, Context.MODE_PRIVATE)

        if (mSharedPreferences!!.contains("Citys")) {
            val gson = Gson()
            val json = mSharedPreferences!!.getString("Citys", "")
            Preferences.CITYS = gson.fromJson(json, object : TypeToken<ArrayList<City>>() {

            }.type)
            Log.d(TAG, Preferences.CITYS.toString())
        } else {
            Preferences.CITYS = ArrayList()
        }
    }

    private fun createSharedPreferences() {
        val gson = Gson()
        Log.d(TAG, gson.toJson(Preferences.CITYS))
        val editor = mSharedPreferences!!.edit()
        editor.putString("Citys", gson.toJson(Preferences.CITYS))
        editor.apply()
    }


    private fun drawerLayoutToggle(gravityOpen: Int, gravityClose: Int) {

        if (drawerLayout.isDrawerOpen(gravityClose)) {
            drawerLayout.closeDrawer(gravityClose)
            drawerLayout.openDrawer(gravityOpen)
        } else {
            if (drawerLayout.isDrawerOpen(gravityOpen)) {
                drawerLayout.closeDrawer(gravityOpen)
            } else {
                drawerLayout.openDrawer(gravityOpen)
            }
        }
    }

    private fun getWeather(call: Call<WeatherModel>) {
        call.enqueue(object : Callback<WeatherModel> {

            override fun onResponse(call: Call<WeatherModel>, response: Response<WeatherModel>) {
                handleResponse(response.body())
            }

            override fun onFailure(call: Call<WeatherModel>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }
}