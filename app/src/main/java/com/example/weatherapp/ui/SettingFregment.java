package com.example.weatherapp.ui;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.SharedPreferences;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatDelegate;

import com.example.weatherapp.R;

public class SettingFregment extends Fragment {
    private Switch themeSwitch,tempSwitch;
    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "prefs";
    private static final String PREF_DARK_MODE = "dark_mode";
    private static final String PREF_FAHRENHEIT = "fahrenheit_data";



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_setting_fregment, container, false);
        themeSwitch = view.findViewById(R.id.themeSwitch);

        tempSwitch=view.findViewById(R.id.tempSwitch);

        getContext();
        sharedPreferences = getContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        // Check current theme and set the switch accordingly
        boolean isDarkMode = sharedPreferences.getBoolean(PREF_DARK_MODE, false);
        themeSwitch.setChecked(isDarkMode);
        updateTheme(isDarkMode);

        boolean isFahrenheit = sharedPreferences.getBoolean(PREF_FAHRENHEIT, false);
        tempSwitch.setChecked(isFahrenheit);

        tempSwitch.setOnCheckedChangeListener((buttonView, isChecked)->{
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(PREF_FAHRENHEIT, isChecked);
            editor.apply();
        });
        themeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // Save theme preference
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(PREF_DARK_MODE, isChecked);
            editor.apply();

            updateTheme(isChecked);
        });
        return view;
    }

    private void updateTheme(boolean isDarkMode) {
        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }


}