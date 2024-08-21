package com.example.weatherapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weatherapp.R;
import com.example.weatherapp.models2.ForecastWeatherModel;
import com.example.weatherapp.models2.WeatherData;

import java.util.List;

//Adapter class for binding forecast weather data to the RecyclerView.
public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.ForecastViewHolder> {
    // List to hold the weather data items
    private final List<WeatherData> list;


    public ForecastAdapter(List<WeatherData> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ForecastAdapter.ForecastViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_forecast, parent, false);
        return new ForecastViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ForecastAdapter.ForecastViewHolder holder, int position) {
        // Get the weather data for the current position
        WeatherData forecast = list.get(position);

        String time = forecast.getDt_txt();
        double windSpeed = forecast.getWind().getSpeed();
        double temperature = forecast.getMain().getTemp();
        String mainInfo = forecast.getWeather().get(0).getMain();
        double humidity = forecast.getMain().getHumidity();
        double tempMin = forecast.getMain().getTemp_min();
        double tempMax = forecast.getMain().getTemp_max();
        holder.mainInfoText.setText(mainInfo);
        holder.humitempId.setText(String.format("%.0f%%", humidity));
        holder.lowtempId.setText(String.format("%.0f°", tempMin));
        holder.highTempId.setText(String.format("%.0f°", tempMax));
        holder.windtempId.setText(String.format("%.0f km/h", windSpeed));
        holder.timeTextView.setText(time);
        holder.tempText.setText(String.format("%.0f°", temperature));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ForecastViewHolder extends RecyclerView.ViewHolder {
        TextView timeTextView, highTempId, windtempId, humitempId,mainInfoText,lowtempId,tempText;


        //Constructor to initialize the ViewHolder with the item's views.
        public ForecastViewHolder(@NonNull View itemView) {
            super(itemView);
            timeTextView = itemView.findViewById(R.id.timeTextView);
            windtempId = itemView.findViewById(R.id.windtempId);
            highTempId = itemView.findViewById(R.id.highTempId);
            mainInfoText=itemView.findViewById(R.id.mainInfoText);
            humitempId=itemView.findViewById(R.id.humitempId);
            lowtempId=itemView.findViewById(R.id.lowtempId);
            tempText=itemView.findViewById(R.id.tempText);
        }
    }
}
