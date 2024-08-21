package com.example.weatherapp.ui;

import static android.content.Context.MODE_PRIVATE;
import static com.example.weatherapp.constants.Constants.API_ID;
import static com.example.weatherapp.constants.Constants.C_UNITS;
import static com.example.weatherapp.constants.Constants.F_UNITS;
import static com.example.weatherapp.constants.Constants.PREFS_NAME;
import static com.example.weatherapp.constants.Constants.convertUnixToTime;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.weatherapp.R;
import com.example.weatherapp.constants.Constants;
import com.example.weatherapp.model1.CurrentWeatherModel;
import com.example.weatherapp.remote.ApiInterface;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {
    // UI elements for displaying weather data
    SearchView citySearchView;
    TextView cityTextId, typeTextId, wDataTextId, feelsTextID, highTextID, lowTextID, pressureTextId, humidityTextId, sunriseTextID, sunsetTextID;
    private ProgressBar progressBar;

    // Interface for making API calls
    private ApiInterface apiInterface;

    // Model to hold the current weather data
    private CurrentWeatherModel data;

    // Constant key for storing Fahrenheit preference in SharedPreferences
    private static final String PREF_FARENHIT = "farenhit_data";

    // Variable to hold the temperature unit preference (Fahrenheit or Celsius)
    boolean isFarenhitData;

    // API call object for fetching weather data
    Call<CurrentWeatherModel> call;

    @Override
    public void onResume() {
        super.onResume();
        checkLocationPermission();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        cityTextId = view.findViewById(R.id.cityTextId);
        typeTextId = view.findViewById(R.id.typeTextId);
        wDataTextId = view.findViewById(R.id.wDataTextId);
        feelsTextID = view.findViewById(R.id.feelsTextID);
        highTextID = view.findViewById(R.id.highTextID);
        lowTextID = view.findViewById(R.id.lowTextID);
        progressBar = view.findViewById(R.id.progressBar);
        citySearchView = view.findViewById(R.id.searchView);
        pressureTextId = view.findViewById(R.id.pressureTextID);
        humidityTextId = view.findViewById(R.id.humidityTextID);
        sunriseTextID = view.findViewById(R.id.sunriseTextID);
        sunsetTextID = view.findViewById(R.id.sunsetTextID);
        // Initializing API interface from Constants class
        apiInterface = Constants.getApiInterface();

        SharedPreferences sharedPreferences = getContext().getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        isFarenhitData = sharedPreferences.getBoolean(PREF_FARENHIT, false);

        // Checking location permission when fragment is loaded
        checkLocationPermission();
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
                // Fetching weather data for the entered city
                fetchCurrentWeather(query.trim());
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Handle text change if needed
                return false;
            }
        });
        return view;
    }

    // Method to fetch current weather data for a given city
    private void fetchCurrentWeather(String cityName) {
        progressBar.setVisibility(View.VISIBLE);
        // Using appropriate units based on the temperature preference
        if (isFarenhitData) {
            call = apiInterface.getCurrentWeather(cityName, API_ID, F_UNITS);
        } else {
            call = apiInterface.getCurrentWeather(cityName, API_ID, C_UNITS);
        }
        call.enqueue(new Callback<CurrentWeatherModel>() {
            @Override
            public void onResponse(Call<CurrentWeatherModel> call, Response<CurrentWeatherModel> response) {
                progressBar.setVisibility(View.GONE);
                data = response.body();
                if (data != null) {
                    updateUI();
                } else {
                    Toast.makeText(getContext(), "Failed to load weather data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CurrentWeatherModel> call, Throwable throwable) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getContext(), "Error: " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Method to update the UI with the fetched weather data
    private void updateUI() {
        double temp = data.getMain().getTemp();
        String description = data.getWeather().get(0).getDescription();
        String cityName = data.getName();
        double feelsLike = data.getMain().getFeels_like();
        double tempMax = data.getMain().getTemp_max();
        double tempMin = data.getMain().getTemp_min();
        long sunrise = data.getSys().getSunrise();
        long sunset = data.getSys().getSunset();

        cityTextId.setText(cityName);
        typeTextId.setText(description);
        wDataTextId.setText(String.format("%.0f°", temp));
        feelsTextID.setText(String.format("%.0f°", feelsLike));
        highTextID.setText(String.format("%.0f°", tempMax));
        lowTextID.setText(String.format("%.0f°", tempMin));
        pressureTextId.setText(String.format("%d hPa", data.getMain().getPressure()));
        humidityTextId.setText(String.format("%d%%", data.getMain().getHumidity()));
        sunriseTextID.setText(convertUnixToTime(sunrise));
        sunsetTextID.setText(convertUnixToTime(sunset));
        updateBackground(description);
    }

    // Method to update the background based on the weather description
    private void updateBackground(String description) {
        // Reference to the root view
        View rootView = getView();

        // Set of keywords that indicate a sky/clear weather
        Set<String> skyKeywords = new HashSet<>(Arrays.asList(
                "clear sky", "few clouds", "scattered clouds", "broken clouds", "mist", "haze", "overcast clouds", "smoke", "fog"));

        if (skyKeywords.contains(description)) {
            rootView.setBackgroundResource(R.drawable.skybg);
        } else {
            // Default background if no match found
            rootView.setBackgroundResource(R.drawable.rainbg2);
        }
    }

    // Method to check and request location permission
    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // Permission already granted
            checkLocationEnabled();
        } else {
            // Request permission
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
        }
    }

    // Method to get the user's current location
    private void getCurrentLocation() {
        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            checkLocationPermission();
            return;
        }
        fusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
                    if (location != null) {
                        // Use location.getLatitude() and location.getLongitude()
                        getCityNameFromLocation(location.getLatitude(), location.getLongitude());
                    } else {
                        // Handle the case where location is null
                        Toast.makeText(getContext(), "Unable to get current location", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    // Handle failure to get location
                    Toast.makeText(getContext(), "Failed to get location: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    // Method to get the city name from latitude and longitude
    private void getCityNameFromLocation(double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 5);
            String cityName = null;

            for (Address address : addresses) {
                String Name = address.getLocality();
                if (Name != null && !Name.isEmpty()) {
                    cityName = Name;
                    break; // Stop as soon as a city name is found
                }
            }
            if (cityName != null) {
                fetchCurrentWeather(cityName);
            } else {
                // Handle the case where no city name was found
                Toast.makeText(getContext(), "City name not found, try a nearby location", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Failed to get city name: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    // Location permission request launcher
    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    // Permission is granted.
                    getCurrentLocation();
                } else {
                    // Permission denied.
                    Toast.makeText(getContext(), "Location permission denied", Toast.LENGTH_SHORT).show();
                }
            });

    // Method to check if location services are enabled
    private void checkLocationEnabled() {
        LocationManager locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        boolean isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (!isGpsEnabled && !isNetworkEnabled) {
            // Location services are not enabled
            showLocationSettingsAlert();
        } else {
            // Location services are enabled
            getCurrentLocation();
        }
    }

    private void showLocationSettingsAlert() {
        new AlertDialog.Builder(getContext())
                .setTitle("Enable Location")
                .setMessage("Your location is disabled. Please enable it in settings to continue.")
                .setPositiveButton("Settings", (dialog, which) -> {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                })
                .setNegativeButton("Cancel", (dialog, which) -> {
                    dialog.cancel();
                })
                .show();
    }
}