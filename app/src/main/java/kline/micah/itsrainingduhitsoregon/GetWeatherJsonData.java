package kline.micah.itsrainingduhitsoregon;


import android.net.Uri;
import android.support.v4.util.Pair;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by desktop on 9/28/2015.
 */
public class GetWeatherJsonData extends GetData {

    private static final String LOG_TAG = GetWeatherJsonData.class.getSimpleName();

    private Uri mUri;
    private String mLocation;
    private List<Weather> mWeatherList;

    final String WEATHER_BASE_URL = "http://api.openweathermap.org/data/2.5/";
    final String WEATHER_CURRENT = "weather";
    final String WEATHER_FORECAST = "forecast";
    final String WEATHER_DAILY = "daily";
    final String WEATHER_SEARCH_PARAM = "q";
    final String WEATHER_MODE = "mode";
    final String WEATHER_UNITS = "units";
    final String WEATHER_COUNT = "cnt";

    public List<Weather> getWeatherList() {
        return mWeatherList;
    }

    public GetWeatherJsonData(String location) {
        super(null);
        mLocation = location;
        mWeatherList = new ArrayList<>();
    }

    public void buildCurrentWeatherUrl() {
        mUri = Uri.parse(WEATHER_BASE_URL)
                .buildUpon()
                .appendPath(WEATHER_CURRENT)
                .appendQueryParameter(WEATHER_SEARCH_PARAM, mLocation)
                .appendQueryParameter(WEATHER_MODE, "json")
                .appendQueryParameter(WEATHER_UNITS, "metric")
                .build();


        Log.v("LOG", mUri.toString());
    }

    public void buildForecastURL() {
        mUri = Uri.parse(WEATHER_BASE_URL)
                .buildUpon()
                .appendPath(WEATHER_FORECAST)
                .appendPath(WEATHER_DAILY)
                .appendQueryParameter(WEATHER_SEARCH_PARAM, mLocation)
                .appendQueryParameter(WEATHER_MODE, "json")
                .appendQueryParameter(WEATHER_UNITS, "metric")
                .appendQueryParameter(WEATHER_COUNT, "16")
                .build();


        Log.v("LOG", mUri.toString());
    }

    public void parseForecastJson() {

        final String JSON_LIST = "list";
        final String JSON_WEATHER = "weather";
        final String JSON_TEMP = "temp";
        final String JSON_MIN = "min";
        final String JSON_MAX = "max";
        final String JSON_MAIN = "main";

        try {
            JSONObject forecast = new JSONObject(getData());
            JSONArray forecastList = forecast.getJSONArray(JSON_LIST);
            for (int i = 0; i < forecastList.length(); i++) {
                JSONObject forecastDay = forecastList.getJSONObject(i);
                JSONObject forecastTemp = forecastDay.getJSONObject(JSON_TEMP);
                Double highTemp = forecastTemp.getDouble(JSON_MAX);
                Double lowTemp = forecastTemp.getDouble(JSON_MIN);
                JSONObject forecastWeather = forecastDay.getJSONArray(JSON_WEATHER).getJSONObject(0);
                String mainWeather = forecastWeather.getString(JSON_MAIN);

                mWeatherList.add(new Weather(mainWeather, highTemp, lowTemp));
            }

        } catch (JSONException jsonE) {
            Log.e(LOG_TAG, "ERROR parsing JSON DATA", jsonE);
        }

    }

    public String getUri() {
        return mUri.toString();
    }

    public void execute() {
        DownloadWeatherData downloadWeatherData = new DownloadWeatherData();
        downloadWeatherData.execute(mUri.toString());
    }

    public void parseCurrentWeatherJson() {

    }

    public class DownloadWeatherData extends DownloadData {
        @Override
        protected void onPostExecute(String resultString) {
            super.onPostExecute(resultString);
            parseForecastJson();
        }

        @Override
        protected String doInBackground(String... params) {
            return super.doInBackground(params);
        }
    }

}
