package com.kram.vlad.weather.api;


import com.kram.vlad.weather.models.Model;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;


/**
 * Created by 1 on 03.01.2017.
 * Get interface for retrofit. Return Model class.
 * Used for request to weather api
 */

public interface IWeatherProvider {


    @GET("/premium/v1/weather.ashx")
    Call<Model> getWeather(@Query("key") String key,

                                  @Query("q") String q,
                                  @Query("num_of_days") String numOfDays,
                                  @Query("date") String date,
                                  @Query("format")String format,
                                  @Query("show_comments") String comments,
                                  @Query("showlocaltime") String localtime,
                                  @Query("lang") String lang,
                                  @Query("tp") String tp);
}
