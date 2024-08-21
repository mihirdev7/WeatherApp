package com.example.weatherapp.constants;

import com.example.weatherapp.remote.ApiInterface;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Constants {
    public static final String BASE_URL = "https://api.openweathermap.org/data/2.5/";
    //public static final String API_ID="";
    public static final String C_UNITS="metric";
    public static final String F_UNITS="imperial";
    public static final String PREFS_NAME = "prefs";
    private static Retrofit retrofit = null;

    //create instance to get the data from API
    //Retrofit is the library build on top of HTTP
    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
    public static ApiInterface getApiInterface() {
        return getClient().create(ApiInterface.class);
    }

    public static String convertUnixToTime(long unixSeconds) {
        Date date = new Date(unixSeconds * 1000L); // convert seconds to milliseconds
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        sdf.setTimeZone(TimeZone.getDefault()); // Set timezone to default
        return sdf.format(date);
    }
}
