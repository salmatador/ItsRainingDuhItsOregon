package kline.micah.itsrainingduhitsoregon.controller;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Micah on 10/9/2015.
 */
public class WeatherSettingsActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getFragmentManager()
                .beginTransaction()
                .replace(android.R.id.content, new WeatherSettingsFragment())
                .commit();
    }
}
