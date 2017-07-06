package com.choliy.igor.flickrgallery.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;

import com.choliy.igor.flickrgallery.R;
import com.choliy.igor.flickrgallery.util.FlickrUtils;

public class SettingsFragment extends PreferenceFragment implements
        SharedPreferences.OnSharedPreferenceChangeListener, Preference.OnPreferenceClickListener {

    private ListPreference mGridPref;
    private ListPreference mPicturePref;
    private SwitchPreference mAnimationPref;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);

        SharedPreferences preferences = getPreferenceScreen().getSharedPreferences();
        mGridPref = (ListPreference) findPreference(getString(R.string.pref_key_grid));
        mPicturePref = (ListPreference) findPreference(getString(R.string.pref_key_picture));
        mAnimationPref = (SwitchPreference) findPreference(getString(R.string.pref_key_animation));
        findPreference(getString(R.string.pref_key_restore)).setOnPreferenceClickListener(this);

        String gridValue = preferences.getString(mGridPref.getKey(), null);
        String pictureValue = preferences.getString(mPicturePref.getKey(), null);

        setPreferenceSummary(mGridPref, gridValue);
        setPreferenceSummary(mPicturePref, pictureValue);
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen()
                .getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }


    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen()
                .getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        mGridPref.setValueIndex(2);
        mPicturePref.setValueIndex(1);
        mAnimationPref.setChecked(true);
        FlickrUtils.showInfo(getView(), getString(R.string.pref_restore_info));
        return true;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Preference preference = findPreference(key);
        if (preference instanceof ListPreference) {
            ListPreference listPref = (ListPreference) preference;
            String value = sharedPreferences.getString(listPref.getKey(), null);
            setPreferenceSummary(listPref, value);
        }
    }

    private void setPreferenceSummary(ListPreference listPref, String value) {
        int prefIndex = listPref.findIndexOfValue(value);
        if (prefIndex >= 0) {
            CharSequence prefLabel = listPref.getEntries()[prefIndex];
            listPref.setSummary(prefLabel);
        }
    }
}