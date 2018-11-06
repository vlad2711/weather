package com.kram.vlad.weather.adapters


import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView

import com.kram.vlad.weather.R
import com.kram.vlad.weather.Utils
import com.kram.vlad.weather.activitys.MainActivity
import com.kram.vlad.weather.callbacks.ForecastDateCallback
import com.kram.vlad.weather.models.Weather
import com.kram.vlad.weather.settings.Preferences
import kotlinx.android.synthetic.main.date_item.view.*

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date

/**
 * Created by vlad on 17.08.17.
 * Adapter For date recycler view
 */

class DateAdapter(private val mWeather: Array<Weather>, private val mForecastDateCallback: ForecastDateCallback, context: Context) : RecyclerView.Adapter<DateAdapter.CardViewHolder>() {
    private val mContext: Context? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DateAdapter.CardViewHolder {
        return CardViewHolder(LayoutInflater.from(parent
                .context)
                .inflate(R.layout.date_item, parent, false))
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) = holder.bind()

    override fun getItemCount(): Int {
        return mWeather.size
    }


    inner class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(){
            Utils().setTypeface(mContext, "qontra", itemView.date)

            if (MainActivity.sRecyclerViewDateCurrentPosition != position) {
                itemView.date_layout!!.setBackgroundResource(R.color.white)
            } else {
                itemView.date_layout!!.setBackgroundResource(R.color.mikadoyellow)
            }

            itemView.date_layout!!.setOnClickListener { v ->
                mForecastDateCallback.forecastDateCallback(mWeather, position)
                itemView.date_layout!!.setBackgroundResource(R.color.mikadoyellow)

                MainActivity.sRecyclerViewDateCurrentPosition = position
            }

            try {
                var formatter = SimpleDateFormat(Preferences.startDateFormat)
                val date = formatter.parse(mWeather[position].date)
                formatter = SimpleDateFormat(Preferences.dateFormat)
                itemView.date!!.text = formatter.format(date)
            } catch (e: ParseException) {
                e.printStackTrace()
            }


        }
    }
}