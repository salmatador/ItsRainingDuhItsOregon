package kline.micah.itsrainingduhitsoregon;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import java.util.UUID;

public class WeatherDetailsActivity extends MainActivity {


    private static final String WEATHER_FORECAST_ID = "kline.micah.itsrainingduhitsoregon.WeatherDetailsActivity.weather_id";

    public static Intent newIntent(Context packageContext, UUID weatherID) {
        Intent intent = new Intent(packageContext, WeatherDetailsActivity.class);
        intent.putExtra(WEATHER_FORECAST_ID, weatherID);
        return intent;
    }

    @Override
    protected Fragment createFragment() {
        UUID weatherId = (UUID) getIntent()
                .getSerializableExtra(WEATHER_FORECAST_ID);
        return WeatherDetailsFragment.newInstance(weatherId);
    }


}
