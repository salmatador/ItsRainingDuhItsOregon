package kline.micah.itsrainingduhitsoregon;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class WeatherListFragment extends Fragment {

    private static final String LOG = WeatherListFragment.class.getSimpleName();

    public static final String BASIC_WEATHER = "BASIC_WEATHER";
    public static final String HIGH_TEMP = "HIGH_TEMP";
    public static final String LOW_TEMP = "LOW_TEMP";
    public static final String MAIN_WEATHER = "MAIN_WEATHER";


    private ArrayAdapter<String> mArrayAdapter;

    private List<Weather> mWeatherList;

    private ListView weatherListView;

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

        weatherListView = (ListView) root.findViewById(R.id.weather_list_view);
        mArrayAdapter = new ArrayAdapter<>(getActivity(), R.layout.fragment_weather_list_textview, R.id.weather_list_item, new ArrayList<String>());
        weatherListView.setAdapter(mArrayAdapter);

        weatherListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Weather weather = mWeatherList.get(position);

                //String intentExtra = mArrayAdapter.getItem(position).toString();

                Intent newIntent = new Intent(getContext(), WeatherDetailsActivity.class);
                newIntent.putExtra(HIGH_TEMP, weather.getMaxTempF());
                newIntent.putExtra(LOW_TEMP, weather.getMinTempF());
                newIntent.putExtra(MAIN_WEATHER, weather.getMainWeather());
                newIntent.putExtra(BASIC_WEATHER, weather.toString());
                startActivity(newIntent);
            }
        });


        GetWeatherData getWeatherJsonData = new GetWeatherData("DALLAS,OR,US");

        getWeatherJsonData.buildForecastURL();

        if (networkConnection()) {
            getWeatherJsonData.execute();
        } else {
            Toast.makeText(
                    getContext(),
                    R.string.no_network_toast_text,
                    Toast.LENGTH_LONG)
                    .show();
        }

        return root;
    }

    private boolean networkConnection() {
        //TODO Update method to not use Depriciated Methods if available
        ConnectivityManager connectivityManager = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED;
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
                mWeatherList = getWeatherList();
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

}
