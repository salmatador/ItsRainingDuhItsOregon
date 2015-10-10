package kline.micah.itsrainingduhitsoregon;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;


public class WeatherListFragment extends Fragment {

    private static final String LOG = WeatherListFragment.class.getSimpleName();

    private RecyclerView mWeatherRecyclerView;
    private WeatherAdapter mAdapter;

    private boolean isEmpty = true;

    public WeatherListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_weather_forecast_list, container, false);

        mWeatherRecyclerView = (RecyclerView) root.findViewById(R.id.weather_recycler_view);
        mWeatherRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        //TODO IMPLEMENT PREFERENCE VALUE TO PROVIDE LOCATION
        //TODO ACCESS LOCATION FROM GPS IF REQUESTED
        GetWeatherData getWeatherJsonData = new GetWeatherData("97338");

        getWeatherJsonData.buildForecastURL(getResources().getString(R.string.weather_api_key));

        if (networkConnection()) {
            if (mAdapter == null) {
                getWeatherJsonData.execute();
                Log.v(LOG, "GETTING JSON DATA");
            } else {
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

    private void updateUI() {
        WeatherStation weatherStation = WeatherStation.get(getActivity());
        List<Weather> weatherForecastList = weatherStation.getWeather();
        mAdapter = new WeatherAdapter(weatherForecastList);
        mWeatherRecyclerView.setAdapter(mAdapter);

    }

    private boolean networkConnection() {
        //TODO Update method to not use Depriciated Methods if available
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
            mWeatherHighTemperature.setText(String.valueOf(mWeather.getMaxTempF()));
            mWeatherLowTemperature.setText(String.valueOf(mWeather.getMinTempF()));
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
                WeatherStation weatherStation = WeatherStation.get(getActivity());
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
