package com.kram.vlad.weather.activitys

import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import com.google.gson.Gson
import com.kram.vlad.weather.R
import com.kram.vlad.weather.Utils
import com.kram.vlad.weather.models.Hourly
import kotlinx.android.synthetic.main.more.*

import java.util.Calendar
import java.util.Date
import java.util.GregorianCalendar
/**
 * Created by vlad on 19.08.17.
 * Activity, used when user tap in one of button in MainActivity
 */

class MoreActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.more)

        val hourly = Gson().fromJson(intent.getStringExtra("Hourly"), Hourly::class.java)

        if (hourly != null)
            initInterface(hourly)
    }


    private fun initInterface(hourly: Hourly) {

        val res = resources

        val calendar = GregorianCalendar.getInstance()
        calendar.set(Calendar.HOUR, 0)

        /*wind!!.text = String.format(res.getString(R.string.wind), hourly.windspeedKmph)
        precip!!.text = String.format(res.getString(R.string.precip), hourly.precipMM)
        pressure!!.text = String.format(res.getString(R.string.pressure), hourly.pressure)
        temperatureMore!!.text = String.format(res.getString(R.string.degrees), hourly.tempC)
        humidity!!.text = String.format(res.getString(R.string.humidity), hourly.humidity)
        value!!.text = hourly.lang_ru[0].value
        imageView2!!.setImageBitmap(Utils.WEATHERIMAGES[hourly.weatherCode])

        Utils().setTypeface(this, "qontra",
                wind,
                precip,
                pressure,
                temperatureMore,
                humidity,
                value)*/
    }

    companion object {

        val TAG = MoreActivity::class.java.simpleName
    }
}
