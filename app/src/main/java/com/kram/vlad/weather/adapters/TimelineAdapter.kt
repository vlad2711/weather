package com.kram.vlad.weather.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kram.vlad.weather.R
import com.kram.vlad.weather.Utils
import com.kram.vlad.weather.models.Hourly
import com.kram.vlad.weather.models.Weather
import kotlinx.android.synthetic.main.timeline_item.view.*

class TimelineAdapter(val weather: Weather): RecyclerView.Adapter<TimelineAdapter.Holder>() {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int) = Holder(LayoutInflater.from(p0.context).inflate(R.layout.timeline_item, p0, false))

    override fun getItemCount() = weather.hourly.size

    override fun onBindViewHolder(p0: Holder, p1: Int) = p0.bind(weather.hourly[p1], itemCount)


    class Holder(view: View): RecyclerView.ViewHolder(view){
        private val TAG = this::class.java.simpleName
        fun bind(hourly: Hourly, size: Int){
            itemView.weather_icon.setImageResource(Utils.WEATHERIMAGES[hourly.weatherCode]!!)
            itemView.temperature.text = String.format(itemView.resources.getString(R.string.degrees), hourly.tempC)
            itemView.time.text = String.format(itemView.resources.getString(R.string.time), Utils.getTime(hourly.time.toInt()).toString())
            itemView.weather_state.text = hourly.lang_ru[0].value
            if(adapterPosition + 1 == size) itemView.timeline_image.setImageResource(R.drawable.timeline_end)
        }
    }
}