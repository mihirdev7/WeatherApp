package com.example.weatherapp.remote;

import com.example.weatherapp.model1.CurrentWeatherModel;
import com.example.weatherapp.models2.ForecastWeatherModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {
//current weather fetch data
    @GET("weather")
    Call<CurrentWeatherModel> getCurrentWeather
            (@Query("q") String city,
             @Query("appid") String appid,
             @Query("units") String units);

    //forecast weather fetch data
    @GET("forecast")
    Call<ForecastWeatherModel> getForecastWeather
            (@Query("q") String city,
             @Query("appid") String appid,
             @Query("units") String units);
}
