package com.example.weatherapp.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.example.weatherapp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {
    private NavController navController;
    private BottomNavigationView bottomNav;
    private  NavHostFragment navHostFrag;
    private static final String PREFS_NAME = "prefs";
    private static final String PREF_DARK_MODE = "dark_mode";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean isDarkMode = sharedPreferences.getBoolean(PREF_DARK_MODE, false);

        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNav=findViewById(R.id.bottomNavId);

         navHostFrag=(NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentContainerId);
         navController=navHostFrag.getNavController();
         NavigationUI.setupWithNavController(bottomNav,navController);

        bottomNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                NavOptions navOptions=new NavOptions.Builder()
                        .setLaunchSingleTop(true)
                        .setPopUpTo(R.id.nav_graph,false)
                        .build();

                if(item.getItemId()==R.id.homeId){
                    navController.navigate(R.id.homeFragment,null,navOptions);
                    return true;
                }
                else if(item.getItemId()==R.id.forecastId){
                    navController.navigate(R.id.forecastFragment,null,navOptions);
                    //navController.popBackStack();
                    return true;
                } else{
                    navController.navigate(R.id.settingFregment,null,navOptions);
                    //navController.popBackStack();
                    return true;
                }
            }
        });
        OnBackPressedDispatcher onBackPressedDispatcher=getOnBackPressedDispatcher();
        onBackPressedDispatcher.addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if(navController.getCurrentDestination().getId()!=R.id.homeFragment){
                    navController.navigate(R.id.homeFragment);
                    bottomNav.setSelectedItemId(R.id.homeId);
                }
                else{
                    finish();
                }
            }
        });
    }
}