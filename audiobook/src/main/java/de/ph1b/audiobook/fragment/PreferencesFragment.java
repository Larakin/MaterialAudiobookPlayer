package de.ph1b.audiobook.fragment;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;

import de.ph1b.audiobook.R;
import de.ph1b.audiobook.dialog.SeekPreferenceDialog;
import de.ph1b.audiobook.dialog.SleepPreferenceDialog;
import de.ph1b.audiobook.dialog.VariablePlaybackSpeedPreferenceDialog;


public class PreferencesFragment extends PreferenceFragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

        ActionBar actionBar = ((ActionBarActivity) getActivity()).getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }


    private void initValues() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());

        int seekAmount = sp.getInt(getString(R.string.pref_key_seek_time), 20);
        String seekSummary = String.valueOf(seekAmount) + " " + getString(R.string.seconds);
        SeekPreferenceDialog seekPreferenceDialog = (SeekPreferenceDialog) findPreference(getString(R.string.pref_key_seek_time));
        seekPreferenceDialog.setSummary(seekSummary);

        int sleepAmount = sp.getInt(getString(R.string.pref_key_sleep_time), 20);
        String sleepSummary = String.valueOf(sleepAmount) + " " + getString(R.string.minutes);
        SleepPreferenceDialog sleepPreferenceDialog = (SleepPreferenceDialog) findPreference(getString(R.string.pref_key_sleep_time));
        sleepPreferenceDialog.setSummary(sleepSummary);


        if (Build.VERSION.SDK_INT < 16 || com.aocate.media.MediaPlayer.isPrestoLibraryInstalled(getActivity())) {
            VariablePlaybackSpeedPreferenceDialog speedDialog =
                    (VariablePlaybackSpeedPreferenceDialog) findPreference
                            (getString(R.string.pref_key_variable_playback_speed));
            if (speedDialog != null) {
                getPreferenceScreen().removePreference(speedDialog);
            }
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        initValues();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        sharedPreferences.registerOnSharedPreferenceChangeListener(onSharedPreferenceChangeListener);
    }

    @Override
    public void onPause() {
        super.onPause();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(onSharedPreferenceChangeListener);
    }

    private final SharedPreferences.OnSharedPreferenceChangeListener onSharedPreferenceChangeListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
            initValues();
        }
    };
}
