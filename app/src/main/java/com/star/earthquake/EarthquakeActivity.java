package com.star.earthquake;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;


public class EarthquakeActivity extends AppCompatActivity {

    private static final int MENU_PREFERENCES = Menu.FIRST + 1;
    private static final int MENU_UPDATE = Menu.FIRST + 2;

    private static final int SHOW_PREFERENCES = 1;

    private boolean autoUpdateChecked = false;
    private int updateFreq = 0;
    private int minMag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_earthquake);

        updateFromPreferences();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        menu.add(0, MENU_PREFERENCES, Menu.NONE, R.string.menu_preferences);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        switch (item.getItemId()) {
            case MENU_PREFERENCES:
                Intent i = new Intent(this, PreferencesActivity.class);
                startActivityForResult(i, SHOW_PREFERENCES);
                return true;
        }

        return false;
    }

    private void updateFromPreferences() {
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        autoUpdateChecked =
                sharedPreferences.getBoolean(PreferencesActivity.PREF_AUTO_UPDATE, false);

        int updateFreqIndex =
                sharedPreferences.getInt(PreferencesActivity.PREF_UPDATE_FREQ_INDEX, 0);
        if (updateFreqIndex < 0) {
            updateFreqIndex = 0;
        }

        int minMagIndex =
                sharedPreferences.getInt(PreferencesActivity.PREF_MIN_MAG_INDEX, 0);
        if (minMagIndex < 0) {
            minMagIndex = 0;
        }

        Resources resources = getResources();

        String[] updateFreqValues = resources.getStringArray(R.array.update_freq_values);
        String[] minMagValues = resources.getStringArray(R.array.minimum_magnitude_values);

        updateFreq = Integer.parseInt(updateFreqValues[updateFreqIndex]);
        minMag = Integer.parseInt(minMagValues[minMagIndex]);
    }

    public int getMinMag() {
        return minMag;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SHOW_PREFERENCES) {
            if (resultCode == RESULT_OK) {
                updateFromPreferences();

                FragmentManager fragmentManager = getSupportFragmentManager();
                final EarthquakeListFragment earthquakeListFragment =
                        (EarthquakeListFragment) fragmentManager.findFragmentById(
                                R.id.EarthquakeListFragment);

                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        earthquakeListFragment.refreshEarthquakes();
                    }
                });

                thread.start();
            }
        }
    }
}
