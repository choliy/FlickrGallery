package com.choliy.igor.flickrgallery.fragment;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;

import com.choliy.igor.flickrgallery.R;
import com.choliy.igor.flickrgallery.view.SquareLayout;

import butterknife.BindView;
import butterknife.OnClick;

public class SecretFragment extends CustomFragment {

    @BindView(R.id.layout_secret_off) SquareLayout mLayoutOff;
    @BindView(R.id.layout_secret_on) SquareLayout mLayoutOn;

    public static final String TAG = SecretFragment.class.getSimpleName();
    private MediaPlayer mPlayer;

    @Override
    int layoutRes() {
        return R.layout.dialog_secret;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mPlayer = MediaPlayer.create(getActivity(), R.raw.flash_sound);
    }

    @OnClick({R.id.flash_off, R.id.flash_on})
    public void onClick(View view) {
        mPlayer.start();
        switch (view.getId()) {
            case R.id.flash_off:
                mLayoutOff.setVisibility(View.INVISIBLE);
                mLayoutOn.setVisibility(View.VISIBLE);
                break;
            case R.id.flash_on:
                mLayoutOff.setVisibility(View.VISIBLE);
                mLayoutOn.setVisibility(View.INVISIBLE);
                break;
        }
    }
}