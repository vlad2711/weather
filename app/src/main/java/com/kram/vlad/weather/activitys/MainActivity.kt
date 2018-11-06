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
import com.kram.vlad.weather.App
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
import com.kram.vlad.weather.models.Model
import com.kram.vlad.weather.models.Weather
import com.kram.vlad.weather.recycler_view_models.City
import com.kram.vlad.weather.settings.Preferences
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.places.Place
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment
import com.google.android.gms.location.places.ui.PlaceSelectionListener
import com.google.gson.Gson

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

        getOrientation()

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

            R.id.weatherHourlyIcon1-> startMoreActivity(1)
            R.id.weatherHourlyIcon2-> startMoreActivity(2)
            R.id.weatherHourlyIcon3-> startMoreActivity(3)
            R.id.weatherHourlyIcon4-> startMoreActivity(4)
            R.id.weatherHourlyIcon5-> startMoreActivity(5)
            R.id.weatherHourlyIcon6-> startMoreActivity(6)
        }
    }

    fun handleResponse(model: Model?) {

        if (model != null) {
            runOnUiThread {
                Utils.sModel = model
                initializeRecyclerViewDate(model.data.weather)
                weatherLayout!!.visibility = View.VISIBLE

                val currentCondition = model.data.current_condition[0]

                weatherImage!!.setImageBitmap(Utils.WEATHERIMAGES[currentCondition.weatherCode])
                temperature!!.text = String.format(resources.getString(R.string.degrees), currentCondition.temp_C)

                val hourly = model.data.weather[0].hourly
                setWeatherForecast(hourly)

                weatherLayout!!.visibility = View.VISIBLE
                progressBar!!.visibility = View.INVISIBLE
            }


        } else {
            Log.d(TAG, "null")

            //  Toast toast = new Toast(getApplicationContext());
            // toast.setText("Failed to download data.Try later");
            //  toast.show();
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
           // String cityName = mGeoLocationProvider.getAddress(latitude, longitude, this);

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
        weatherImage!!.setImageBitmap(Utils.WEATHERIMAGES[weather[position].hourly[0].weatherCode])
        temperature!!.setText(String.format(resources.getString(R.string.degrees), weather[position].hourly[0].tempC))
        setWeatherForecast(weather[position].hourly)
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

    private fun setWeatherForecast(hourly: Array<Hourly>) {
        weatherHourlyIcon1.text = Utils.WEATHERICONS[hourly[0].weatherCode]
        weatherHourlyIcon2.text = Utils.WEATHERICONS[hourly[1].weatherCode]
        weatherHourlyIcon3.text = Utils.WEATHERICONS[hourly[2].weatherCode]
        weatherHourlyIcon4.text = Utils.WEATHERICONS[hourly[3].weatherCode]
        weatherHourlyIcon5.text = Utils.WEATHERICONS[hourly[4].weatherCode]
        weatherHourlyIcon6.text = Utils.WEATHERICONS[hourly[5].weatherCode]
    }

    private fun initUserInterface() {
        setTypefacesAndIcons()
        initializeRecyclerView()
        initializeToolbarButton()
        setOnclickListeners(weatherHourlyIcon1,
                weatherHourlyIcon6,
                weatherHourlyIcon2,
                weatherHourlyIcon3,
                weatherHourlyIcon4,
                weatherHourlyIcon5,
                weatherImage)
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
        if (Utils().getScreenSize(this) < Preferences.PORTRAITSCREENSIZE) {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        } else {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        }
    }

    private fun setTypefacesAndIcons() {

        val utils = Utils()

        utils.setTypeface(applicationContext,
                "icons",
                weatherHourlyIcon1,
                weatherHourlyIcon6,
                weatherHourlyIcon2,
                weatherHourlyIcon3,
                weatherHourlyIcon4,
                weatherHourlyIcon5)

        utils.setTypeface(applicationContext,
                "qontra",
                temperature)

        utils.setWeatherImage(applicationContext)
        Utils.getIcons()
    }

    private fun createCallFromGeoPosition(weatherProvider: IWeatherProvider, latitude: String, longitude: String): Call<Model> {
        Log.d(TAG, "call")

        val mapJson = HashMap<String, String>()
        mapJson["key"] = Constants.WEATHER_API_KEY
        mapJson["q"] = "$latitude,$longitude"
        mapJson["num_of_days"] = "14"
        mapJson["date"] = "today"
        mapJson["format"] = "json"
        mapJson["show_comments"] = "yes"
        mapJson["showlocaltime"] = "yes"
        mapJson["lang"] = "ru"
        mapJson["tp"] = "1"

        weatherLayout!!.visibility = View.INVISIBLE
        progressBar!!.visibility = View.VISIBLE



        return weatherProvider.getWeather(Constants.WEATHER_API_KEY,
                "$latitude,$longitude",
                "14",
                "today",
                "json",
                "yes",
                "yes",
                "ru",
                "1")
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

    private fun setOnclickListeners(vararg views: View) {
        for (view in views) {
            view.setOnClickListener(this)
        }
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

    private fun getWeather(call: Call<Model>) {
        call.enqueue(object : Callback<Model> {

            override fun onResponse(call: Call<Model>, response: Response<Model>) {
                handleResponse(response.body())
            }

            override fun onFailure(call: Call<Model>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }

    private fun startMoreActivity(id: Int) {
        if (Utils.sModel != null) {
            val intent = Intent(this, MoreActivity::class.java)
            intent.putExtra("Hourly", Gson().toJson(Utils.sModel.data.weather[sRecyclerViewDateCurrentPosition].hourly[id]))
            startActivity(intent)

        }
    }


}