package com.kram.vlad.weather.activitys


import android.Manifest
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.ActivityInfo
import android.location.Geocoder
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.design.widget.TabLayout
import android.support.v4.app.ActivityCompat
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import com.google.gson.reflect.TypeToken
import com.kram.vlad.weather.Constants
import com.kram.vlad.weather.R
import com.kram.vlad.weather.Utils
import com.kram.vlad.weather.api.IWeatherProvider
import com.kram.vlad.weather.callbacks.CityChooseCallback
import com.kram.vlad.weather.callbacks.ForecastDateCallback
import com.kram.vlad.weather.callbacks.UpdateItemCallback
import com.kram.vlad.weather.geolocation.GeoLocationCallback
import com.kram.vlad.weather.geolocation.GeoLocationProvider
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
import com.kram.vlad.weather.adapters.WeatherForecastAdapter
import io.reactivex.Observable

import java.io.IOException
import java.util.ArrayList
import java.util.Locale

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlinx.android.synthetic.main.activity_main.*


/**
 * MainActivity of app
 */

class MainActivity : AppCompatActivity(), GeoLocationCallback, PlaceSelectionListener, UpdateItemCallback, CityChooseCallback {


    companion object {
        val TAG = MainActivity::class.java.simpleName
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

    fun handleResponse(weatherModel: WeatherModel?, city: String) {
        if (weatherModel != null) {
            runOnUiThread {
                Utils.sWeatherModels[city] = weatherModel
                progressBar!!.visibility = View.INVISIBLE
                pager.adapter = WeatherForecastAdapter(supportFragmentManager)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun checkPermissions(vararg permissions: String) {
        ActivityCompat.requestPermissions(this, permissions, Constants.PERMISSION_REQUEST_CODE)
    }


    override fun geolocation(latitude: Double, longitude: Double) {
        try {

            val gcd = Geocoder(this, Locale.getDefault())
            val addresses = gcd.getFromLocation(latitude, longitude, 1)
            val cityName = addresses[0].locality
            getWeather(createCallFromGeoPosition(mWeatherProvider!!, latitude.toString(), longitude.toString()), cityName)

            Preferences.CITYS.add(0, City(latitude.toString(),
                    longitude.toString(),
                    cityName,
                    "current"))
            if (mAutocompleteFragment != null && cityName != null)
                mAutocompleteFragment!!.setText(cityName)

            initializeCities()
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    override fun updateItemCallback() {
        initializeCities()
    }

    override fun cityChooseCallback(city: City, position: Int) {
        getWeather(createCallFromGeoPosition(mWeatherProvider!!, city.latitude.toString(), city.longitude.toString()), city.name)
    }

    override fun onPlaceSelected(place: Place) {

        Preferences.CITYS.add(City(
                place.latLng.latitude.toString(),
                place.latLng.longitude.toString(),
                place.name as String,
                "added"))
        getWeather(createCallFromGeoPosition(mWeatherProvider!!, place.latLng.latitude.toString(), place.latLng.longitude.toString()), place.name as String)

        initializeCities()

        this.updateItemCallback()

        Log.i(TAG, "Place: " + place.name)
    }

    override fun onError(status: Status) {
        Log.i(TAG, "An error occurred: $status")
    }


    private fun initUserInterface() {
        setTypefacesAndIcons()
        initializeAutoCompleteFragment()
        getCitysFromSharedPreferences()
        getOrientation()
        initializeCities()

       // pager.adapter = WeatherForecastAdapter(supportFragmentManager)
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
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }

    private fun setTypefacesAndIcons() {
        Utils.setWeatherImage()
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

    private fun initializeCities() {

        Log.d(TAG, Preferences.CITYS.size.toString())
        if(cities.tabCount > 0){
            cities.removeAllTabs()
        }

        for (i in 0 until Preferences.CITYS.size) {
            val tab = cities.newTab()
            tab.text = Preferences.CITYS[i].name
            cities.addTab(tab)
        }

        cities.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabReselected(p0: TabLayout.Tab?) {}
            override fun onTabUnselected(p0: TabLayout.Tab?) {}
            override fun onTabSelected(p0: TabLayout.Tab?) {
                pager.currentItem = p0!!.position
            }
        })

        pager.addOnPageChangeListener(object: ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(p0: Int) {}
            override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {}
            override fun onPageSelected(p0: Int) {
                cities.getTabAt(p0)!!.select()
            }
        })

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
            Preferences.CITYS = gson.fromJson(json, object: TypeToken<ArrayList<City>>(){}.type)
            val requests = ArrayList<Observable<WeatherModel>>()

            for (i in 0 until Preferences.CITYS.size) {
                requests.add(mWeatherProvider!!.getWeatherObservable(Constants.WEATHER_API_KEY,
                        "${Preferences.CITYS[i].latitude},${Preferences.CITYS[i].longitude}",
                        "14",
                        "today",
                        "json",
                        "yes",
                        "yes",
                        "ru",
                        "3"))
            }

            Observable.zip(requests){
                for (i in 0 until it.size) {
                    Utils.sWeatherModels[Preferences.CITYS[i].name] = (it[i] as WeatherModel)
                }
            }.subscribe({
                pager.adapter = WeatherForecastAdapter(supportFragmentManager)
            }){
                it.printStackTrace()
            }
            Log.d(TAG, Preferences.CITYS.toString())
        } else {
            Preferences.CITYS = ArrayList()
        }

        initializeCities()
    }

    override fun onStop() {
        super.onStop()
        if (Preferences.CITYS.size > 0) {
            createSharedPreferences()
        }
    }

    private fun createSharedPreferences() {
        val gson = Gson()

        val buf = Preferences.CITYS[0]
        Preferences.CITYS.removeAt(0)
        val editor = mSharedPreferences!!.edit()
        editor.putString("Citys", gson.toJson(Preferences.CITYS))
        editor.apply()

        Preferences.CITYS.add(0, buf)
    }

    private fun getWeather(call: Call<WeatherModel>, city: String) {
        call.enqueue(object : Callback<WeatherModel> {

            override fun onResponse(call: Call<WeatherModel>, response: Response<WeatherModel>) {
                handleResponse(response.body(), city)
            }

            override fun onFailure(call: Call<WeatherModel>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }
}