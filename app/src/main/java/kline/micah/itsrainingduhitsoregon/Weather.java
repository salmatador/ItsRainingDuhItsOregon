package kline.micah.itsrainingduhitsoregon;

/**
 * Created by desktop on 9/28/2015.
 * This class holds weather data that should be retieved from api.openweathermap.org
 * TODO Extend class to hold current weather data as well
 */
public class Weather {

    private String mMainWeather;
    private double mMinTemp;
    private double mMaxTemp;

    public Weather(String mainWeather, Double highTemp, Double lowTemp) {
        mMainWeather = mainWeather;
        mMaxTemp = highTemp;
        mMinTemp = lowTemp;
    }

    public String getMainWeather() {
        return mMainWeather;
    }

    public double getMinTemp() {
        return Math.round(mMinTemp);
    }

    public double getMaxTemp() {
        return Math.round(mMaxTemp);
    }

    public double getMinTempF() {
        return Math.round((mMinTemp * 1.8) + 32);
    }

    public double getMaxTempF() {
        return Math.round((mMaxTemp * 1.8) + 32);
    }

    @Override
    public String toString() {
        return getMainWeather() + " - " + getMaxTempF() + " - " + getMinTempF();
    }

}
