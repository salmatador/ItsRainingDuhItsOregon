package kline.micah.itsrainingduhitsoregon.controller;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.UUID;

import kline.micah.itsrainingduhitsoregon.R;
import kline.micah.itsrainingduhitsoregon.model.Weather;
import kline.micah.itsrainingduhitsoregon.model.WeatherStation;

/**
 * TODO Use a View Pager to allow swiping from one day to the next
 */
public class WeatherDetailsFragment extends Fragment {

    private static final String ARG_WEATHER_ID = "weather_id";

    private Weather mWeather;

    private TextView weatherDetailsTextView;
    private TextView highTemperatureTextView;
    private TextView lowTemperatureTextView;
    private TextView mainWeatherTextView;


    public WeatherDetailsFragment() {
    }

    public static WeatherDetailsFragment newInstance(UUID weatherId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_WEATHER_ID, weatherId);

        WeatherDetailsFragment fragment = new WeatherDetailsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        UUID weatherId = (UUID) getArguments().getSerializable(ARG_WEATHER_ID);
        mWeather = WeatherStation.get(getActivity()).getForecastWeather(weatherId);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_weather_details, container, false);
        weatherDetailsTextView = (TextView) rootView.findViewById(R.id.weatherString);
        highTemperatureTextView = (TextView) rootView.findViewById(R.id.highTemperature);
        lowTemperatureTextView = (TextView) rootView.findViewById(R.id.lowTemperature);
        mainWeatherTextView = (TextView) rootView.findViewById(R.id.mainWeather);

        if (mWeather != null) {
            weatherDetailsTextView.setText(mWeather.toString());
            highTemperatureTextView.setText(String.valueOf(mWeather.getMaxTempF()));
            lowTemperatureTextView.setText(String.valueOf(mWeather.getMinTempF()));
            mainWeatherTextView.setText(mWeather.getMainWeather());
        }
        return rootView;
    }
}







