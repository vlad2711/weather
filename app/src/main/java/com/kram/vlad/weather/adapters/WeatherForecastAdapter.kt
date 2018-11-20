package com.kram.vlad.weather.adapters

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.PagerAdapter
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kram.vlad.weather.R
import com.kram.vlad.weather.Utils
import com.kram.vlad.weather.fragments.WeatherForecastFragment
import com.kram.vlad.weather.settings.Preferences

class WeatherForecastAdapter(fragmentManager: FragmentManager): FragmentPagerAdapter(fragmentManager) {
    private val TAG = this::class.java.simpleName
    override fun getItem(p0: Int): Fragment {
        Log.d(TAG, Preferences.CITYS[p0].name)
        return WeatherForecastFragment.newInstance(Preferences.CITYS[p0].name, p0)
    }

    override fun getCount() = Preferences.CITYS.size
}