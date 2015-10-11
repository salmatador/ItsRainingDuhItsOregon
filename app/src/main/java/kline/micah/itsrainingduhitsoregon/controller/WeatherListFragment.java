package kline.micah.itsrainingduhitsoregon.controller;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import kline.micah.itsrainingduhitsoregon.R;
import kline.micah.itsrainingduhitsoregon.model.GetWeatherJsonData;
import kline.micah.itsrainingduhitsoregon.model.Weather;
import kline.micah.itsrainingduhitsoregon.model.WeatherStation;

//TODO Code is getting a little wild please refactor, comment, and document code
public class WeatherListFragment extends Fragment {

    private static final String LOG = WeatherListFragment.class.getSimpleName();

    private RecyclerView mWeatherRecyclerView;
    private WeatherAdapter mAdapter;
    private String mLocation;
    private SharedPreferences sharedPreferences;
    private WeatherStation weatherStation;
    public WeatherListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        setRetainInstance(true);
        weatherStation = WeatherStation.get(getContext());
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setLocation();
        weatherStation.setCelsius(sharedPreferences.getString(getString(R.string.pref_units_key), getString(R.string.pref_units_metric)));
        View root = inflater.inflate(R.layout.fragment_weather_forecast_list, container, false);

        mWeatherRecyclerView = (RecyclerView) root.findViewById(R.id.weather_recycler_view);
        mWeatherRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        //TODO ACCESS LOCATION FROM GPS IF REQUESTED

        GetWeatherData getWeatherJsonData = new GetWeatherData(mLocation);

        getWeatherJsonData.buildForecastURL(getResources().getString(R.string.weather_api_key));

        if (networkConnection()) {
            if (mAdapter == null) {
                getWeatherJsonData.execute();
            } else {
                Toast.makeText(getContext(), "Did not update List", Toast.LENGTH_LONG).show();
                updateUI();
            }
        } else {
            Toast.makeText(
                    getContext(),
                    R.string.no_network_toast_text,
                    Toast.LENGTH_LONG)
                    .show();
        }

        return root;
    }

    private void setLocation() {
        mLocation = sharedPreferences.getString(getString(R.string.pref_location_key),
                getString(R.string.pref_location_default));
    }

    @Override
    public void onResume() {
        super.onResume();
        weatherStation.setCelsius(sharedPreferences.getString(getString(R.string.pref_units_key),
                getString(R.string.pref_units_metric)));
        if (mLocation != sharedPreferences.getString(getString(R.string.pref_location_key),
                getString(R.string.pref_location_default))) {
            setLocation();
        }
        GetWeatherData getWeatherJsonData = new GetWeatherData(mLocation);
        getWeatherJsonData.buildForecastURL(getResources().getString(R.string.weather_api_key));
        getWeatherJsonData.execute();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.menu_weather_list, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            startActivity(new Intent(getActivity(), WeatherSettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void updateUI() {
        List<Weather> weatherForecastList = weatherStation.getWeather();
        mAdapter = new WeatherAdapter(weatherForecastList);
        mWeatherRecyclerView.setAdapter(mAdapter);
    }

    private boolean networkConnection() {
        //TODO Update method to not use Deprecated Methods if available
        ConnectivityManager connectivityManager = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED;
    }

    private class WeatherHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mWeatherHighTemperature;
        private TextView mWeatherLowTemperature;
        private TextView mWeatherDescription;

        private Weather mWeather;

        public WeatherHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            mWeatherHighTemperature = (TextView) itemView.findViewById(R.id.list_item_high_temp_view);
            mWeatherLowTemperature = (TextView) itemView.findViewById(R.id.list_item_low_temp_view);
            mWeatherDescription = (TextView) itemView.findViewById(R.id.list_item_description_view);

        }

        public void bindWeather(Weather weather) {
            mWeather = weather;
            mWeatherHighTemperature.setText(String.valueOf(mWeather.getMaxTemp()));
            mWeatherLowTemperature.setText(String.valueOf(mWeather.getMinTemp()));
            mWeatherDescription.setText(mWeather.getMainWeather());
        }

        @Override
        public void onClick(View v) {
            Intent intent = WeatherPagerActivity.newIntent(getActivity(), mWeather.getId());
            startActivity(intent);

        }
    }

    private class WeatherAdapter extends RecyclerView.Adapter<WeatherHolder> {
        private List<Weather> mWeather;


        public WeatherAdapter(List<Weather> weather) {
            mWeather = weather;
        }


        @Override
        public WeatherHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater
                    .inflate(R.layout.list_item_weather, parent, false);
            return new WeatherHolder(view);
        }

        @Override
        public void onBindViewHolder(WeatherHolder holder, int position) {
            Weather weather = mWeather.get(position);
            holder.bindWeather(weather);
        }

        @Override
        public int getItemCount() {
            return mWeather.size();
        }
    }

    public class GetWeatherData extends GetWeatherJsonData {

        public GetWeatherData(String location) {
            super(location);
        }

        public void execute() {
            WeatherData weather = new WeatherData();
            weather.execute(getUri());
        }

        public class WeatherData extends DownloadWeatherData {
            @Override
            protected void onPostExecute(String resultString) {
                super.onPostExecute(resultString);
                //WeatherStation weatherStation = WeatherStation.get(getActivity());
                weatherStation.setWeather(getWeatherList());
                updateUI();

            }

            @Override
            protected String doInBackground(String... params) {
                return super.doInBackground(params);
            }
        }
    }

}
