package kline.micah.itsrainingduhitsoregon.controller;

import android.support.v4.app.Fragment;

public class WeatherListActivity extends MainActivity {

    @Override
    protected Fragment createFragment() {
        return new WeatherListFragment();
    }

}
