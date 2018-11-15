package com.kram.vlad.weather.adapters

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.PagerAdapter
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kram.vlad.weather.R
import com.kram.vlad.weather.Utils
import com.kram.vlad.weather.fragments.WeatherForecastFragment
import com.kram.vlad.weather.settings.Preferences
import kotlinx.android.synthetic.main.weather_forecat_fragment.view.*

class WeatherForecastAdapter(fragmentManager: FragmentManager): FragmentPagerAdapter(fragmentManager) {
    override fun getItem(p0: Int) = WeatherForecastFragment.newInstance(Preferences.CITYS[p0].name)

    override fun getCount() = Preferences.CITYS.size
}