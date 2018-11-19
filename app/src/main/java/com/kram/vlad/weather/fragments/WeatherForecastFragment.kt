package com.kram.vlad.weather.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kram.vlad.weather.Constants
import com.kram.vlad.weather.R
import com.kram.vlad.weather.Utils
import com.kram.vlad.weather.activitys.MainActivity
import com.kram.vlad.weather.adapters.TimelineAdapter
import com.kram.vlad.weather.api.IWeatherProvider
import com.kram.vlad.weather.models.WeatherModel
import com.kram.vlad.weather.settings.Preferences
import kotlinx.android.synthetic.main.weather_forecat_fragment.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WeatherForecastFragment: Fragment() {
    private lateinit var cityName: String

    companion object {
        fun newInstance(city: String): WeatherForecastFragment {
            val fragment = WeatherForecastFragment()
            val bundle = Bundle()
            bundle.putString("city", city)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.weather_forecat_fragment, container, false)
        cityName = arguments!!.getString("city")!!
        if(Utils.sWeatherModels.containsKey(cityName)) userInterfaceInit(view)
        return view
    }

    private fun userInterfaceInit(view: View){
        view.weather_icon.setImageResource(Utils.WEATHERIMAGES[Utils.sWeatherModels[cityName]!!.data.current_condition[0].weatherCode]!!)
        view.weather_state.text = Utils.sWeatherModels[cityName]!!.data.current_condition[0].lang_ru[0].value
        view.temperature.text = String.format(resources.getString(R.string.degrees), Utils.sWeatherModels[cityName]!!.data.current_condition[0].temp_C)
        view.timeline_view.layoutManager = LinearLayoutManager(view.context)
        view.timeline_view.adapter = TimelineAdapter(Utils.sWeatherModels[cityName]!!.data.weather[0])
        view.city_name.text = cityName
    }
}