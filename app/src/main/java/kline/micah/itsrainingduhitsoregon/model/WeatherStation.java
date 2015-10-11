package kline.micah.itsrainingduhitsoregon.model;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import kline.micah.itsrainingduhitsoregon.R;

/**
 * Created by desktop on 10/7/2015.
 * Creating a persistent Singleton Class
 */
public class WeatherStation {

    private static WeatherStation sWeatherStation;
    private List<Weather> mWeather;
    private boolean mIsCelsius;
    private Context mContext;

    private WeatherStation(Context context) {
        mContext = context;
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

    public void setCelsius(String string) {
        Log.e("INSIDE SET CEslsius", string);
        if (string.equals(mContext.getResources().getString(R.string.pref_units_metric))) {
            Log.e("CELSIUS = METRIC", string);
            mIsCelsius = true;

        } else {
            mIsCelsius = false;
        }
    }

    public boolean isCelsius() {
        return mIsCelsius;
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
