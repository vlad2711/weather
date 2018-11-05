package com.kram.vlad.weather.modules;

import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.itkacher.okhttpprofiler.OkHttpProfilerInterceptor;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.kram.vlad.weather.BuildConfig;
import com.kram.vlad.weather.Constants;
import com.kram.vlad.weather.api.IWeatherProvider;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by vlad on 25.07.17.
 * Dagger module. Provide data to retrofit and AsyncTask
 */

@Module
public class WeatherInfoTaskModule {

    public static final String TAG = WeatherInfoTaskModule.class.getSimpleName();

    @Provides
    Gson provideGson() {
        Log.d(TAG, "gson");
        return new GsonBuilder().create();
    }

    @Provides
    IWeatherProvider provideWeather(Retrofit retrofit) {
        Log.d(TAG, "weather");

        return retrofit.create(IWeatherProvider.class);
    }

    @Provides
    Retrofit provideRetrofit(Gson gson) {
        Log.d(TAG, "retrofit");
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        if (BuildConfig.DEBUG) builder.addInterceptor(new OkHttpProfilerInterceptor());

        OkHttpClient client = builder.build();

        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client)
                .baseUrl(Constants.WEATHER_BASE_URL)
                .build();
    }
}