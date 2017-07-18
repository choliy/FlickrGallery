package com.choliy.igor.flickrgallery.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.choliy.igor.flickrgallery.R;
import com.choliy.igor.flickrgallery.util.PrefUtils;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class SettingsActivity extends BroadcastActivity {

    private String mGridValue;
    private String mStyleValue;
    private String mPictureValue;
    private String mAnimationValue;
    private String mNotificationValue;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        if (savedInstanceState == null) {
            String[] settings = PrefUtils.getSettings(this);
            mGridValue = settings[0];
            mStyleValue = settings[1];
            mPictureValue = settings[2];
            mAnimationValue = settings[3];
            mNotificationValue = settings[4];
        } else {
            mGridValue = savedInstanceState.getString(getString(R.string.pref_key_grid));
            mStyleValue = savedInstanceState.getString(getString(R.string.pref_key_style));
            mPictureValue = savedInstanceState.getString(getString(R.string.pref_key_picture));
            mAnimationValue = savedInstanceState.getString(getString(R.string.pref_key_animation));
            mNotificationValue = savedInstanceState.getString(getString(R.string.pref_key_notification));
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(getString(R.string.pref_key_grid), mGridValue);
        outState.putString(getString(R.string.pref_key_style), mStyleValue);
        outState.putString(getString(R.string.pref_key_picture), mPictureValue);
        outState.putString(getString(R.string.pref_key_animation), mAnimationValue);
        outState.putString(getString(R.string.pref_key_notification), mNotificationValue);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        checkAndFinish();
    }

    public void onReturnClick(View view) {
        checkAndFinish();
    }

    private void checkAndFinish() {
        String[] settings = PrefUtils.getSettings(this);
        String gridValue = settings[0];
        String styleValue = settings[1];
        String pictureValue = settings[2];
        String animationValue = settings[3];
        String notificationValue = settings[4];

        boolean gridEquals = gridValue.equals(mGridValue);
        boolean styleEquals = styleValue.equals(mStyleValue);
        boolean pictureEquals = pictureValue.equals(mPictureValue);
        boolean animationEquals = animationValue.equals(mAnimationValue);
        boolean notificationEquals = notificationValue.equals(mNotificationValue);

        if (!gridEquals || !styleEquals || !pictureEquals || !animationEquals || !notificationEquals) {
            setResult(RESULT_OK, new Intent());
            finish();
        } else {
            NavUtils.navigateUpFromSameTask(this);
            finish();
        }
    }
}