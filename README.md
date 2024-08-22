# Weather App

## Overview

The Weather App is an Android application designed to provide users with accurate and up-to-date weather forecasts. The app includes features like location-based weather updates, customizable themes (dark and light mode), and a user-friendly interface that allows easy navigation between different fragments.

## Features

- **Current Weather Data**: Displays the current weather conditions based on the user's location or searched city.
- **5-Day Forecast**: Provides a 5-day weather forecast with 3-hour intervals.
- **Dark & Light Mode**: Users can switch between dark and light themes, with preferences saved for future sessions.
- **Temperature Units**: Users can toggle between Fahrenheit and Celsius.
- **Animated Splash Screen**: An attractive animated splash screen welcomes users to the app.
- **Fragment Navigation**: Bottom navigation and Navigation UI for seamless fragment transitions.
- **Error Handling**: Comprehensive error handling for a smoother user experience.
- **API Integration**: Fetches weather data from the OpenWeather API.

## Screenshots
![Home](https://github.com/user-attachments/assets/470e0bb2-815a-4bad-994b-dd9107266352)
![Home Fragment](https://github.com/user-attachments/assets/0194e052-d97a-419f-8b3d-95de25617e29)
![Forecast Fragment](https://github.com/user-attachments/assets/34f0a73e-00f3-4135-bd0e-12101b9ad96c)
![settings](https://github.com/user-attachments/assets/1504b101-4a08-4699-95b0-50598ec49a8b)
![Permission](https://github.com/user-attachments/assets/c1c2793a-5de0-4201-b663-8e00d359464d)
![Location](https://github.com/user-attachments/assets/8830f6f4-4dc2-4b9a-ab97-1cca9b823ee8)

## Installation

1. Clone the repository:

    ```bash
    git clone https://github.com/your_username/weather-app.git
    ```

2. Open the project in Android Studio.
3. Build the project and run it on an emulator or physical device.

## Usage

- **Home Screen**: View the current weather data for your location.
- **Search**: Use the search feature to find weather data for a specific city.
- **Forecast**: Navigate to the forecast fragment to see the 5-day weather forecast.
- **Settings**: Adjust the app's theme and temperature units according to your preference.

## Technologies Used

- **Java**: The primary programming language for the app.
- **Retrofit**: For API calls and data retrieval.
- **SharedPreferences**: To store user preferences (e.g., theme mode, temperature unit).
- **Material Design**: For creating an intuitive and attractive user interface.

## API

This app uses the [OpenWeather API](https://openweathermap.org/api) to fetch weather data.

- **API Key**: You need to add your OpenWeather API key in the `Constants` class.
  
