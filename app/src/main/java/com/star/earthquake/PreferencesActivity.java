package com.star.earthquake;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;

public class PreferencesActivity extends AppCompatActivity {

    private CheckBox autoUpdateCheckBox;
    private Spinner updateFreqSpinner;
    private Spinner magnitudeSpinner;

    public static final String USER_PREFERENCE = "USER_PREFERENCE";
    public static final String PREF_AUTO_UPDATE = "PREF_AUTO_UPDATE";
    public static final String PREF_MIN_MAG_INDEX = "PREF_MIN_MAG_INDEX";
    public static final String PREF_UPDATE_FREQ_INDEX = "PREF_UPDATE_FREQ_INDEX";

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);

        autoUpdateCheckBox = (CheckBox) findViewById(R.id.checkbox_auto_update);
        updateFreqSpinner = (Spinner) findViewById(R.id.spinner_update_freq);
        magnitudeSpinner = (Spinner) findViewById(R.id.spinner_quake_mag);

        populateSpinners();

        sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        updateUIFromPreferences();

        Button okButton = (Button) findViewById(R.id.okButton);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savePreferences();
                PreferencesActivity.this.setResult(RESULT_OK);
                finish();
            }
        });

        Button cancelButton = (Button) findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PreferencesActivity.this.setResult(RESULT_CANCELED);
                finish();
            }
        });
    }

    private void populateSpinners() {
        ArrayAdapter<CharSequence> freqArrayAdapter =
                ArrayAdapter.createFromResource(this, R.array.update_freq_options,
                        android.R.layout.simple_spinner_item);

        freqArrayAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);

        updateFreqSpinner.setAdapter(freqArrayAdapter);

        ArrayAdapter<CharSequence> magnitudeArrayAdapter =
                ArrayAdapter.createFromResource(this, R.array.minimum_magnitude_options,
                        android.R.layout.simple_spinner_item);

        magnitudeArrayAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);

        magnitudeSpinner.setAdapter(magnitudeArrayAdapter);
    }

    private void updateUIFromPreferences() {
        boolean autoUpdateChecked = sharedPreferences.getBoolean(PREF_AUTO_UPDATE, false);
        int updateFreqIndex = sharedPreferences.getInt(PREF_UPDATE_FREQ_INDEX, 2);
        int minMagIndex = sharedPreferences.getInt(PREF_MIN_MAG_INDEX, 0);

        autoUpdateCheckBox.setChecked(autoUpdateChecked);
        updateFreqSpinner.setSelection(updateFreqIndex);
        magnitudeSpinner.setSelection(minMagIndex);
    }

    private void savePreferences() {
        boolean autoUpdateChecked = autoUpdateCheckBox.isChecked();
        int updateFreqIndex = updateFreqSpinner.getSelectedItemPosition();
        int minMagIndex = magnitudeSpinner.getSelectedItemPosition();

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(PREF_AUTO_UPDATE, autoUpdateChecked);
        editor.putInt(PREF_UPDATE_FREQ_INDEX, updateFreqIndex);
        editor.putInt(PREF_MIN_MAG_INDEX, minMagIndex);

        editor.commit();
    }
}
