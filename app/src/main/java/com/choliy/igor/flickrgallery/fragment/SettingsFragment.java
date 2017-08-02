package com.choliy.igor.flickrgallery.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;

import com.choliy.igor.flickrgallery.FlickrConstants;
import com.choliy.igor.flickrgallery.R;
import com.choliy.igor.flickrgallery.event.PrefRestoreEvent;
import com.choliy.igor.flickrgallery.util.AlarmUtils;
import com.choliy.igor.flickrgallery.util.DialogUtils;
import com.choliy.igor.flickrgallery.util.InfoUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class SettingsFragment extends PreferenceFragment implements
        SharedPreferences.OnSharedPreferenceChangeListener,
        Preference.OnPreferenceClickListener {

    private ListPreference mGridPref;
    private ListPreference mStylePref;
    private ListPreference mPicturePref;
    private CheckBoxPreference mSplashPref;
    private SwitchPreference mAnimationPref;
    private SwitchPreference mNotificationPref;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);

        SharedPreferences preferences = getPreferenceScreen().getSharedPreferences();
        mGridPref = (ListPreference) findPreference(getString(R.string.pref_key_grid));
        mStylePref = (ListPreference) findPreference(getString(R.string.pref_key_style));
        mPicturePref = (ListPreference) findPreference(getString(R.string.pref_key_picture));
        mSplashPref = (CheckBoxPreference) findPreference(getString(R.string.pref_key_splash));
        mAnimationPref = (SwitchPreference) findPreference(getString(R.string.pref_key_animation));
        mNotificationPref = (SwitchPreference) findPreference(getString(R.string.pref_key_notification));
        mNotificationPref.setOnPreferenceClickListener(this);
        findPreference(getString(R.string.pref_key_restore)).setOnPreferenceClickListener(this);

        String gridValue = preferences.getString(mGridPref.getKey(), null);
        String styleValue = preferences.getString(mStylePref.getKey(), null);
        String pictureValue = preferences.getString(mPicturePref.getKey(), null);

        setPreferenceSummary(mGridPref, gridValue);
        setPreferenceSummary(mStylePref, styleValue);
        setPreferenceSummary(mPicturePref, pictureValue);
    }

    @Override
    public void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
        getPreferenceScreen()
                .getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }


    @Override
    public void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
        getPreferenceScreen()
                .getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        if (preference.getKey().equals(getString(R.string.pref_key_notification)))
            setupNotificationService(mNotificationPref.isChecked());
        else
            DialogUtils.restoreDialog(getActivity());
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
        if (prefIndex >= FlickrConstants.INT_ZERO) {
            CharSequence prefLabel = listPref.getEntries()[prefIndex];
            listPref.setSummary(prefLabel);
        }
    }

    @Subscribe
    public void onEvent(PrefRestoreEvent event) {
        if (event.isRestoreSettings()) {
            mGridPref.setValueIndex(FlickrConstants.DEFAULT_GRID_POSITION);
            mStylePref.setValueIndex(FlickrConstants.DEFAULT_STYLE_POSITION);
            mPicturePref.setValueIndex(FlickrConstants.DEFAULT_PICTURE_POSITION);
            mSplashPref.setChecked(FlickrConstants.DEFAULT_SPLASH);
            mAnimationPref.setChecked(FlickrConstants.DEFAULT_ANIMATION);
            mNotificationPref.setChecked(FlickrConstants.DEFAULT_NOTIFICATION);
            setupNotificationService(Boolean.FALSE);
            InfoUtils.showShortShack(getView(), getString(R.string.pref_restore_info));
        }
    }

    private void setupNotificationService(boolean startService) {
        AlarmUtils.setServiceAlarm(getActivity().getApplicationContext(), startService);
    }
}