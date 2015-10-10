package kline.micah.itsrainingduhitsoregon.controller;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import java.util.List;
import java.util.UUID;

import kline.micah.itsrainingduhitsoregon.R;
import kline.micah.itsrainingduhitsoregon.model.Weather;
import kline.micah.itsrainingduhitsoregon.model.WeatherStation;

public class WeatherPagerActivity extends FragmentActivity {

    private static final String WEATHER_FORECAST_ID = "kline.micah.itsrainingduhitsoregon.WeatherDetailsActivity.weather_id";

    private ViewPager mViewPager;
    private List<Weather> mWeather;

    public static Intent newIntent(Context packageContext, UUID weatherId) {
        Intent intent = new Intent(packageContext, WeatherPagerActivity.class);
        intent.putExtra(WEATHER_FORECAST_ID, weatherId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_pager);

        UUID weatherId = (UUID) getIntent()
                .getSerializableExtra(WEATHER_FORECAST_ID);

        mViewPager = (ViewPager) findViewById(R.id.activity_weather_pager_view_pager);

        mWeather = WeatherStation.get(this).getWeather();
        FragmentManager fragmentManager = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {

            @Override
            public Fragment getItem(int position) {
                Weather weather = mWeather.get(position);
                return WeatherDetailsFragment.newInstance(weather.getId());
            }

            @Override
            public int getCount() {
                return mWeather.size();
            }
        });
    }
}
