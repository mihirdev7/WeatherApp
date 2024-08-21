package com.example.weatherapp.ui;

import static android.content.Context.MODE_PRIVATE;
import static com.example.weatherapp.constants.Constants.API_ID;
import static com.example.weatherapp.constants.Constants.C_UNITS;
import static com.example.weatherapp.constants.Constants.F_UNITS;
import static com.example.weatherapp.constants.Constants.PREFS_NAME;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.example.weatherapp.R;
import com.example.weatherapp.adapter.ForecastAdapter;
import com.example.weatherapp.constants.Constants;
import com.example.weatherapp.models2.ForecastWeatherModel;
import com.example.weatherapp.models2.WeatherData;
import com.example.weatherapp.remote.ApiInterface;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForecastFragment extends Fragment {
    // Model to hold forecast weather data
    private ForecastWeatherModel data;

    // UI elements for city search and displaying forecast data
    private SearchView citySearchView;
    private RecyclerView forecastRecyclerView;

    // Adapter for the RecyclerView
    private ForecastAdapter forecastAdapter;

    // List to hold individual weather data items
    private List<WeatherData> weatherDataList;

    // Interface for making API calls
    private ApiInterface apiInterface;

    // API call object for fetching forecast data
    private Call<ForecastWeatherModel> call;

    // Variable to hold the temperature unit preference (Fahrenheit or Celsius)
    private boolean isFarenhitData;

    // Constant key for storing Fahrenheit preference in SharedPreferences
    private static final String PREF_FARENHIT = "farenhit_data";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_forecast, container, false);
        citySearchView = view.findViewById(R.id.searchView);
        forecastRecyclerView=view.findViewById(R.id.forecastRecyclerView);
        // Initializing the list to hold forecast data
        weatherDataList = new ArrayList<>();
        // Initializing API interface from Constants class
        apiInterface = Constants.getApiInterface();

        // Retrieving temperature unit preference from SharedPreferences
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        isFarenhitData = sharedPreferences.getBoolean(PREF_FARENHIT, false);

        // Setting up city search functionality
        citySearchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                citySearchView.setIconified(false);
                citySearchView.requestFocusFromTouch();
                InputMethodManager im = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (im != null) {
                    im.showSoftInput(citySearchView.findFocus(), InputMethodManager.SHOW_IMPLICIT);
                }
            }
        });

        citySearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Fetching forecast weather data for the entered city
                fetchForecastWeather(query.trim());
                return true;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                // Handle text change if needed
                return false;
            }
        });

        forecastAdapter = new ForecastAdapter(weatherDataList);
        forecastRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        forecastRecyclerView.setAdapter(forecastAdapter);

        return view;
    }
    // Method to fetch forecast weather data for a given city
    private void fetchForecastWeather(String cityName) {

        if(isFarenhitData){
            call = apiInterface.getForecastWeather(cityName, API_ID, F_UNITS);
        }else{
            call = apiInterface.getForecastWeather(cityName, API_ID, C_UNITS);
        }
        call.enqueue(new Callback<ForecastWeatherModel>() {
            @Override
            public void onResponse(Call<ForecastWeatherModel> call, Response<ForecastWeatherModel> response) {
                data = response.body();
                if (data != null && data.getList() != null) {
                    //forecastRecyclerView.setBackgroundColor(R.color.colorPrimary);
                    weatherDataList.clear();
                    weatherDataList.addAll(data.getList());
                    forecastAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getContext(), "Failed to load weather data", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<ForecastWeatherModel> call, Throwable throwable) {
                Log.e("error",throwable.getMessage());
            }
        });
    }
}