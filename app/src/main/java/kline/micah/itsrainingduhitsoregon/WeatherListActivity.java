package kline.micah.itsrainingduhitsoregon;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class WeatherListActivity extends MainActivity {

    @Override
    protected Fragment createFragment() {
        return new WeatherListFragment();
    }

}