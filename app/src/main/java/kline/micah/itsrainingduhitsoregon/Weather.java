package kline.micah.itsrainingduhitsoregon;

/**
 * Created by desktop on 9/28/2015.
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

    @Override
    public String toString() {
        return getMainWeather() + " - " + getMaxTemp() + " - " + getMinTemp();
    }

}
