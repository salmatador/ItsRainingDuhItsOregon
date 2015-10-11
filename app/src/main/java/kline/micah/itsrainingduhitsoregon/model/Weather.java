package kline.micah.itsrainingduhitsoregon.model;

import java.util.UUID;

/**
 * Created by desktop on 9/28/2015.
 * This class holds weather data that should be retieved from api.openweathermap.org
 * TODO Extend class to hold current weather data as well
 */
public class Weather {

    private String mMainWeather;
    private double mMinTemp;
    private double mMaxTemp;
    private UUID mId;
    private WeatherStation weatherStation;

    public Weather(String mainWeather, Double highTemp, Double lowTemp) {
        mId = UUID.randomUUID();
        mMainWeather = mainWeather;
        mMaxTemp = highTemp;
        mMinTemp = lowTemp;
        weatherStation = WeatherStation.get(null);
    }

    public String getMainWeather() {
        return mMainWeather;
    }

    public double getMinTemp() {
        if (weatherStation != null && weatherStation.isCelsius()) {
            return Math.round(mMinTemp);
        } else {
            return getMinTempF();
        }
    }

    public double getMaxTemp() {
        if (weatherStation != null && weatherStation.isCelsius()) {
            return Math.round(mMaxTemp);
        } else {
            return getMaxTempF();
        }

    }

    public double getMinTempF() {
        return Math.round((mMinTemp * 1.8) + 32);
    }

    public double getMaxTempF() {
        return Math.round((mMaxTemp * 1.8) + 32);
    }

    @Override
    public String toString() {
        return getMainWeather() + " - " + getMaxTemp() + " - " + getMinTemp();
    }

    public UUID getId() {
        return mId;
    }
}
