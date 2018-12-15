package com.kram.vlad.weather.activitys

import android.Manifest
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Build
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.ActivityCompat
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.google.gson.reflect.TypeToken
import com.kram.vlad.weather.Constants
import com.kram.vlad.weather.R
import com.kram.vlad.weather.Utils
import com.kram.vlad.weather.geolocation.GeoLocationCallback
import com.kram.vlad.weather.geolocation.GeoLocationProvider
import com.kram.vlad.weather.recycler_view_models.City
import com.kram.vlad.weather.settings.Preferences
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.places.Place
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment
import com.google.android.gms.location.places.ui.PlaceSelectionListener
import com.google.gson.Gson
import com.kram.vlad.weather.adapters.WeatherForecastAdapter
import java.io.IOException
import java.util.ArrayList
import java.util.Locale
import kotlinx.android.synthetic.main.activity_main.*


/**
 * MainActivity of app
 */

class MainActivity : AppCompatActivity(), GeoLocationCallback, PlaceSelectionListener {

    companion object {
        val TAG = MainActivity::class.java.simpleName
    }

    private var geolocatioEnabled: Boolean = false
    private var mSharedPreferences: SharedPreferences? = null
    private var mGeoLocationProvider: GeoLocationProvider? = null
    private var mAutocompleteFragment: PlaceAutocompleteFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initUserInterface()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) checkPermissions(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
    }


    private fun checkPermissions(vararg permissions: String) {
        ActivityCompat.requestPermissions(this, permissions, Constants.PERMISSION_REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == Constants.PERMISSION_REQUEST_CODE){
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                initGeolocation()
            } else{
                getCitysFromSharedPreferences()
                Toast.makeText(this, "Can't use geolocation to find your position :(", Toast.LENGTH_SHORT).show()
                geolocatioEnabled = false
            }
        }
    }

    override fun geolocation(latitude: Double, longitude: Double) {
        try {

            val gcd = Geocoder(this, Locale.getDefault())
            val addresses = gcd.getFromLocation(latitude, longitude, 1)
            val cityName = addresses[0].locality

            Preferences.CITYS.add(City(latitude.toString(),
                    longitude.toString(),
                    cityName,
                    "current"))

            pager.adapter!!.notifyDataSetChanged()

            initializeCities()
            getCitysFromSharedPreferences()

        } catch (e: IOException) {
            e.printStackTrace()
        }
    }


    fun onPlaceSelected(name: String, latitude: String, longitude: String){
        Preferences.CITYS.add(City(latitude, longitude, name,"added"))

        if(pager.adapter != null) pager.adapter!!.notifyDataSetChanged()
        initializeCities()
    }


    override fun onPlaceSelected(place: Place) {
        onPlaceSelected(place.name as String,
                place.latLng.latitude.toString(),
                place.latLng.longitude.toString() )
    }

    override fun onError(status: Status) {
        Log.i(TAG, "An error occurred: $status")
    }

    private fun initUserInterface() {
        setTypefacesAndIcons()
        initializeAutoCompleteFragment()
        getOrientation()
        pager.adapter = WeatherForecastAdapter(supportFragmentManager)
    }

    private fun initGeolocation() {
        mGeoLocationProvider = GeoLocationProvider(this)
    }

    private fun getOrientation() {
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }

    private fun setTypefacesAndIcons() {
        Utils.setWeatherImage()
    }

    private fun initializeCities() {
        if(cities.tabCount > 0) cities.removeAllTabs()


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
            val city: ArrayList<City> = gson.fromJson(json, object: TypeToken<ArrayList<City>>(){}.type)
            for (i in 0 until city.size) Preferences.CITYS.add(city[i])
              pager.adapter!!.notifyDataSetChanged()
            initializeCities()
        }
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
        if(geolocatioEnabled)Preferences.CITYS.removeAt(0)
        val editor = mSharedPreferences!!.edit()
        editor.putString("Citys", gson.toJson(Preferences.CITYS))
        editor.apply()

        if(geolocatioEnabled)Preferences.CITYS.add(0, buf)
    }
}