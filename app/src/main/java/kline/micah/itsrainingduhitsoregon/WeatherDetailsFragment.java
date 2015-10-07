package kline.micah.itsrainingduhitsoregon;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * TODO Use a View Pager to allow swiping from one day to the next
 */
public class WeatherDetailsFragment extends Fragment {


    private TextView weatherDetailsTextView;
    private TextView highTemperatureTextView;
    private TextView lowTemperatureTextView;
    private TextView mainWeatherTextView;
    private String[] weatherString = new String[4];

    public WeatherDetailsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getActivity().getIntent();
        weatherString[0] = intent.getStringExtra(WeatherListFragment.BASIC_WEATHER);
        weatherString[1] = String.valueOf(intent.getDoubleExtra(WeatherListFragment.HIGH_TEMP, 0.0));
        weatherString[2] = String.valueOf(intent.getDoubleExtra(WeatherListFragment.LOW_TEMP, 0.0));
        weatherString[3] = intent.getStringExtra(WeatherListFragment.MAIN_WEATHER);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_weather_details, container, false);
        weatherDetailsTextView = (TextView) rootView.findViewById(R.id.weatherString);
        highTemperatureTextView = (TextView) rootView.findViewById(R.id.highTemperature);
        lowTemperatureTextView = (TextView) rootView.findViewById(R.id.lowTemperature);
        mainWeatherTextView = (TextView) rootView.findViewById(R.id.mainWeather);
        if (weatherString != null) {
            weatherDetailsTextView.setText(weatherString[0]);
            highTemperatureTextView.setText(weatherString[1]);
            lowTemperatureTextView.setText(weatherString[2]);
            mainWeatherTextView.setText(weatherString[3]);
        }
        return rootView;
    }
}







