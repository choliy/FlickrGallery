package com.choliy.igor.galleryforflickr.activity;

import android.os.Bundle;

import com.choliy.igor.galleryforflickr.FlickrConstants;
import com.choliy.igor.galleryforflickr.R;
import com.choliy.igor.galleryforflickr.event.AnimPrefEvent;
import com.choliy.igor.galleryforflickr.util.PrefUtils;

import org.greenrobot.eventbus.EventBus;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingsActivity extends BroadcastActivity {

    private String mGridValue;
    private String mStyleValue;
    private String mAnimationValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);

        if (savedInstanceState == null) {
            String[] settings = PrefUtils.getSettings(this);
            mGridValue = settings[FlickrConstants.INT_ZERO];
            mStyleValue = settings[FlickrConstants.INT_ONE];
            mAnimationValue = settings[FlickrConstants.INT_TWO];
        } else {
            mGridValue = savedInstanceState.getString(getString(R.string.pref_key_grid));
            mStyleValue = savedInstanceState.getString(getString(R.string.pref_key_style));
            mAnimationValue = savedInstanceState.getString(getString(R.string.pref_key_animation));
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(getString(R.string.pref_key_grid), mGridValue);
        outState.putString(getString(R.string.pref_key_style), mStyleValue);
        outState.putString(getString(R.string.pref_key_animation), mAnimationValue);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        checkAndFinish();
    }

    @OnClick(R.id.ic_return_settings)
    public void onReturnClick() {
        checkAndFinish();
    }

    private void checkAndFinish() {
        String[] settings = PrefUtils.getSettings(this);
        String gridValue = settings[FlickrConstants.INT_ZERO];
        String styleValue = settings[FlickrConstants.INT_ONE];
        String animationValue = settings[FlickrConstants.INT_TWO];

        boolean gridEquals = gridValue.equals(mGridValue);
        boolean styleEquals = styleValue.equals(mStyleValue);
        boolean animationEquals = animationValue.equals(mAnimationValue);

        if (!gridEquals || !styleEquals) {
            setResult(RESULT_OK);
        }

        if (!animationEquals) {
            boolean isAnimationOn = PrefUtils.getAnimationSettings(this);
            EventBus.getDefault().postSticky(new AnimPrefEvent(isAnimationOn));
        }

        finish();
    }
}