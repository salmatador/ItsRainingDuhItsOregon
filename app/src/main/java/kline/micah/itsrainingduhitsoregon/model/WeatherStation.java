package kline.micah.itsrainingduhitsoregon.model;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by desktop on 10/7/2015.
 * Creating a persistent Singleton Class
 */
public class WeatherStation {

    private static WeatherStation sWeatherStation;
    private List<Weather> mWeather;

    private WeatherStation(Context context) {
        mWeather = new ArrayList<>();
    }

    public static WeatherStation get(Context context) {
        if (sWeatherStation == null) {
            sWeatherStation = new WeatherStation(context);
        }
        return sWeatherStation;
    }

    public List<Weather> getWeather() {
        return mWeather;
    }

    public void setWeather(List<Weather> weatherList) {
        mWeather.clear();
        mWeather.addAll(weatherList);
    }

    public void update(Weather weather, int index) {
        mWeather.set(index, weather);
    }

    public void add(Weather weather) {
        mWeather.add(weather);
    }

    public Weather getForecastWeather(UUID id) {
        for (Weather weather : mWeather) {
            if (weather.getId().equals(id)) {
                return weather;
            }
        }
        return null;
    }
}
