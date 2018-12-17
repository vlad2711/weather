package com.kram.vlad.weather.fragments

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kram.vlad.weather.Constants
import com.kram.vlad.weather.R
import com.kram.vlad.weather.Utils
import com.kram.vlad.weather.adapters.TimelineAdapter
import com.kram.vlad.weather.api.IWeatherProvider
import com.kram.vlad.weather.models.WeatherModel
import com.kram.vlad.weather.settings.Preferences
import kotlinx.android.synthetic.main.weather_forecat_fragment.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class WeatherForecastFragment: Fragment() {
    private lateinit var cityName: String
    private var position = 0
    private val TAG = this::class.java.simpleName
    companion object {
        fun newInstance(city: String, position: Int): WeatherForecastFragment {
            val fragment = WeatherForecastFragment()
            val bundle = Bundle()
            bundle.putString("city", city)
            bundle.putInt("position", position)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.weather_forecat_fragment, container, false)
        cityName = arguments!!.getString("city")!!
        position = arguments!!.getInt("position")


        Log.d(TAG, cityName)

        if(Utils.sWeatherModels.containsKey(cityName)) {
            userInterfaceInit(view)
            initDates(view, Utils.sWeatherModels[cityName]!!)
        } else getForecast(createCallFromGeoPosition(IWeatherProvider.create(), Preferences.CITYS[position].latitude, Preferences.CITYS[position].longitude))

        return view
    }

    private fun userInterfaceInit(view: View){
        view.nestedScrollView.visibility = View.VISIBLE
        view.progressBar.visibility = View.INVISIBLE


        Log.d(TAG, cityName)



        view.weather_icon.setImageResource(Utils.WEATHERIMAGES[Utils.sWeatherModels[cityName]!!.data.current_condition[0].weatherCode]!!)
        view.weather_state.text = Utils.sWeatherModels[cityName]!!.data.current_condition[0].lang_ru[0].value
        view.temperature.text = String.format(resources.getString(R.string.degrees), Utils.sWeatherModels[cityName]!!.data.current_condition[0].temp_C)
        view.timeline_view.layoutManager = LinearLayoutManager(view.context)
        view.timeline_view.adapter = TimelineAdapter(Utils.sWeatherModels[cityName]!!.data.weather[0])
        view.city_name.text = cityName
    }

    private fun createCallFromGeoPosition(weatherProvider: IWeatherProvider, latitude: String, longitude: String): Call<WeatherModel> {
        return weatherProvider.getWeather(Constants.WEATHER_API_KEY,
                "$latitude,$longitude",
                "14",
                //"today",
                "json",
                "yes",
                "yes",
                "ru",
                "3")
    }

    fun handleResponse(weatherModel: WeatherModel?) {
        if (weatherModel != null) {
            activity!!.runOnUiThread {
                Utils.sWeatherModels[cityName] = weatherModel
                if(view != null) {
                    userInterfaceInit(view!!)
                    initDates(weatherModel)
                    view!!.progressBar.visibility = View.INVISIBLE
                    view!!.nestedScrollView.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun initDates(weatherModel: WeatherModel){
        if(view != null) initDates(view!!, weatherModel)
    }

    private fun initDates(view: View, weatherModel: WeatherModel){
        for (i in 0 until weatherModel.data.weather.size) {
            val date = view.dates.newTab()
            val c = Calendar.getInstance()
            c.time = Date()
            date.text = SimpleDateFormat("dd MMM", Locale.getDefault()).format(SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(weatherModel.data.weather[i].date))

            view.dates.addTab(date)
        }

        view.dates.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabReselected(p0: TabLayout.Tab?) {}
            override fun onTabUnselected(p0: TabLayout.Tab?) {}
            override fun onTabSelected(p0: TabLayout.Tab) {
                Log.d(TAG, Utils.sWeatherModels[cityName]!!.data.weather[p0.position].hourly[0].tempC.toString())
                view.weather_icon.setImageResource(Utils.WEATHERIMAGES[Utils.sWeatherModels[cityName]!!.data.weather[p0.position].hourly[0].weatherCode]!!)
                view.weather_state.text = Utils.sWeatherModels[cityName]!!.data.weather[p0.position].hourly[0].lang_ru[0].value
                view.temperature.text = String.format(resources.getString(R.string.degrees), Utils.sWeatherModels[cityName]!!.data.weather[p0.position].hourly[0].tempC)
                view.timeline_view.adapter = TimelineAdapter(Utils.sWeatherModels[cityName]!!.data.weather[p0.position])
            }
        })
    }


    private fun getForecast(call: Call<WeatherModel>){
        if(view != null) {
            view!!.progressBar.visibility = View.VISIBLE
            view!!.nestedScrollView.visibility = View.INVISIBLE
        }

        call.enqueue(object : Callback<WeatherModel> {
            override fun onResponse(call: Call<WeatherModel>, response: Response<WeatherModel>) { handleResponse(response.body()) }
            override fun onFailure(call: Call<WeatherModel>, t: Throwable) { t.printStackTrace() }
        })
    }
}
