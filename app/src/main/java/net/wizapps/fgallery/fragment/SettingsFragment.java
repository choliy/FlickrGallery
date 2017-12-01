package net.wizapps.fgallery.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;

import net.wizapps.fgallery.R;
import net.wizapps.fgallery.tool.Constants;
import net.wizapps.fgallery.tool.Events;
import net.wizapps.fgallery.util.AlarmUtils;
import net.wizapps.fgallery.util.DialogUtils;
import net.wizapps.fgallery.util.InfoUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class SettingsFragment extends PreferenceFragment implements
        SharedPreferences.OnSharedPreferenceChangeListener,
        Preference.OnPreferenceClickListener {

    private ListPreference mGridPref;
    private ListPreference mStylePref;
    private ListPreference mPicturePref;
    private SwitchPreference mSplashPref;
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
        mSplashPref = (SwitchPreference) findPreference(getString(R.string.pref_key_splash));
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
        if (preference.getKey().equals(getString(R.string.pref_key_notification))) {
            setupNotificationService(mNotificationPref.isChecked());
        } else {
            DialogUtils.restoreDialog(getActivity());
        }
        return Boolean.TRUE;
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

    @Subscribe
    public void onEvent(Events.PrefRestoreEvent event) {
        mGridPref.setValueIndex(Constants.DEFAULT_GRID_POSITION);
        mStylePref.setValueIndex(Constants.DEFAULT_STYLE_POSITION);
        mPicturePref.setValueIndex(Constants.DEFAULT_PICTURE_POSITION);
        mSplashPref.setChecked(Constants.DEFAULT_SPLASH);
        mAnimationPref.setChecked(Constants.DEFAULT_ANIMATION);
        mNotificationPref.setChecked(Constants.DEFAULT_NOTIFICATION);
        setupNotificationService(Boolean.FALSE);
        InfoUtils.showShack(getView(), getString(R.string.pref_restore_info));
    }

    private void setPreferenceSummary(ListPreference listPref, String value) {
        int prefIndex = listPref.findIndexOfValue(value);
        if (prefIndex >= Constants.ZERO) {
            CharSequence prefLabel = listPref.getEntries()[prefIndex];
            listPref.setSummary(prefLabel);
        }
    }

    private void setupNotificationService(boolean startService) {
        AlarmUtils.setServiceAlarm(getActivity().getApplicationContext(), startService);
    }
}