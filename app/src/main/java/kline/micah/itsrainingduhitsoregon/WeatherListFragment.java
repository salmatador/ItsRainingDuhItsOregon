package kline.micah.itsrainingduhitsoregon;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class WeatherListFragment extends Fragment {

    private static final String LOG = WeatherListFragment.class.getSimpleName();


    private ArrayAdapter<String> mArrayAdapter;


    private ListView weatherList;

    public WeatherListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_weather_list, container, false);

        weatherList = (ListView) root.findViewById(R.id.weather_list_view);
        mArrayAdapter = new ArrayAdapter<>(getActivity(), R.layout.fragment_weather_list_textview, R.id.weather_list_item, new ArrayList<String>());
        weatherList.setAdapter(mArrayAdapter);

        weatherList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // String positionString = testData[position];
                startActivity(new Intent(getContext(), WeatherDetailsActivity.class));
            }
        });

        //GetData getData = new GetData("http://api.openweathermap.org/data/2.5/forecast/daily?q=97338&mode=json&units=metric&cnt=16");
        //getData.execute();

        GetWeatherData getWeatherJsonData = new GetWeatherData("97338");

        getWeatherJsonData.buildForecastURL();
        //getWeatherJsonData.buildCurrentWeatherUrl();

        getWeatherJsonData.execute();
        //FetchWeather fetchWeather = new FetchWeather();
        //fetchWeather.execute();

        return root;
    }

    public class GetWeatherData extends GetWeatherJsonData {

        public void execute() {
            WeatherData weather = new WeatherData();
            weather.execute(getUri());
        }

        public GetWeatherData(String location) {
            super(location);
        }

        public class WeatherData extends DownloadWeatherData {
            @Override
            protected void onPostExecute(String resultString) {
                super.onPostExecute(resultString);
                mArrayAdapter.clear();
                for (Weather string : getWeatherList()) {
                    mArrayAdapter.add(string.toString());
                }
                //mArrayAdapter.add(getWeatherList());
            }

            @Override
            protected String doInBackground(String... params) {
                return super.doInBackground(params);
            }
        }
    }

    private class FetchWeather extends AsyncTask<String, Void, String[]> {

        private String responseString = null;
        private HttpURLConnection mHttpURLConnection = null;
        private BufferedReader reader = null;

        String DATA_OBJ_LIST = "list";
        String DATA_OBJ_TEMP = "temp";
        String DATA_OBJ_WEATHER = "weather";
        String DATA_MAIN = "main";
        String DATA_MIN = "min";
        String DATA_MAX = "max";

        //private String Base = "http://api.openweathermap.org/data/2.5/forecast/daily?q=97338&mode=json&units=metric&cnt=16";

        private Uri uri = Uri.parse("http://api.openweathermap.org/data/2.5/forecast")
                .buildUpon()
                .appendPath("daily")
                .appendQueryParameter("q", "97338")
                .appendQueryParameter("mode", "json")
                .appendQueryParameter("units", "metric")
                .appendQueryParameter("cnt", "16")
                .build();


        public String[] parseResponseJsonString() throws JSONException {
            //TODO Implement method to parse Json response string to String[]
            String[] data = new String[16];
            Log.v(LOG, uri.toString());
            Log.v(LOG, responseString);
            JSONObject weatherData = new JSONObject(responseString);
            JSONArray days = weatherData.getJSONArray(DATA_OBJ_LIST);
            for (int index = 0; index < 16; index++) {
                JSONObject day = days.getJSONObject(index);
                JSONObject temperature = day.getJSONObject(DATA_OBJ_TEMP);
                JSONObject dayWeather = day.getJSONArray(DATA_OBJ_WEATHER).getJSONObject(0);
                String description = dayWeather.getString(DATA_MAIN);
                double minimum = temperature.getDouble(DATA_MIN);
                double maximum = temperature.getDouble(DATA_MAX);

                data[index] = description + " - " + maximum + " - " + minimum;
                Log.v(LOG, data[index]);
            }

            return data;
        }

        public String[] getJsonData() {

            try {
                URL mURL = new URL(uri.toString());

                mHttpURLConnection = (HttpURLConnection) mURL.openConnection();
                mHttpURLConnection.setRequestMethod("GET");
                mHttpURLConnection.connect();

                InputStream inputStream = mHttpURLConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    return null;
                }

                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }

                if (buffer.length() == 0) {
                    return null;
                }
                responseString = buffer.toString();
            } catch (IOException e) {

            } finally {
                if (mHttpURLConnection != null) {
                    mHttpURLConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        e.printStackTrace();
                    }
                }
            }


            //TODO Implement method to fetch JSON String data from api.openweathermap.org
            try {
                return parseResponseJsonString();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected String[] doInBackground(String... params) {
            return getJsonData();
        }

        @Override
        protected void onPostExecute(String[] strings) {
            super.onPostExecute(strings);
            mArrayAdapter.clear();
            mArrayAdapter.addAll(strings);
        }
    }
}
