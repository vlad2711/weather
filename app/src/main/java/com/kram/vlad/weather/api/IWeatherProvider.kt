package com.kram.vlad.weather.api


import com.kram.vlad.weather.models.WeatherModel

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Query
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient
import com.itkacher.okhttpprofiler.OkHttpProfilerInterceptor
import com.kram.vlad.weather.BuildConfig
import com.kram.vlad.weather.Constants
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers


/**
 * Created by 1 on 03.01.2017.
 * Get interface for retrofit. Return WeatherModel class.
 * Used for request to weather api
 */

interface IWeatherProvider {


    @GET("/premium/v1/weather.ashx")
    fun getWeather(@Query("key") key: String,

                   @Query("q") q: String,
                   @Query("num_of_days") numOfDays: String,
                   @Query("date") date: String,
                   @Query("format") format: String,
                   @Query("show_comments") comments: String,
                   @Query("showlocaltime") localtime: String,
                   @Query("lang") lang: String,
                   @Query("tp") tp: String): Call<WeatherModel>

    @GET("/premium/v1/weather.ashx")
    fun getWeatherObservable(@Query("key") key: String,
                             @Query("q") q: String,
                             @Query("num_of_days") numOfDays: String,
                             @Query("date") date: String,
                             @Query("format") format: String,
                             @Query("show_comments") comments: String,
                             @Query("showlocaltime") localtime: String,
                             @Query("lang") lang: String,
                             @Query("tp") tp: String): Observable<WeatherModel>

    companion object {
        fun create(): IWeatherProvider {
            val builder = OkHttpClient.Builder()
            if (BuildConfig.DEBUG) builder.addInterceptor(OkHttpProfilerInterceptor())

            val client = builder.build()

            return Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                    .client(client)
                    .baseUrl(Constants.WEATHER_BASE_URL)
                    .build().create(IWeatherProvider::class.java)
        }
    }
}
