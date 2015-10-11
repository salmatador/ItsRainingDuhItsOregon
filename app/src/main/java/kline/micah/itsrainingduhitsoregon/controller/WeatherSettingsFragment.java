package kline.micah.itsrainingduhitsoregon.controller;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import kline.micah.itsrainingduhitsoregon.R;

/**
 * Created by Micah on 10/9/2015.
 */
public class WeatherSettingsFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.pref_general);

    }

}
