package com.choliy.igor.galleryforflickr.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.choliy.igor.galleryforflickr.R;
import com.choliy.igor.galleryforflickr.util.DialogUtils;
import com.choliy.igor.galleryforflickr.util.NavUtils;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnLongClick;

public class AboutFragment extends CustomFragment {

    public static final String TAG = AboutFragment.class.getSimpleName();
    @BindView(R.id.btn_developer) TextView mDeveloper;

    @Override
    int layoutRes() {
        return R.layout.dialog_about;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        int year = Calendar.getInstance().get(Calendar.YEAR);
        String text = getString(R.string.dialog_developer, String.valueOf(year));
        mDeveloper.setText(text);
    }

    @OnClick({R.id.btn_developer, R.id.btn_email, R.id.btn_feedback, R.id.btn_close})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_developer:
                String developerUrl = getString(R.string.dialog_url);
                Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(developerUrl));
                NavUtils.checkBeforeLaunching(getActivity(), webIntent);
                break;
            case R.id.btn_email:
                NavUtils.emailIntent(getActivity());
                break;
            case R.id.btn_feedback:
                NavUtils.feedbackIntent(getActivity());
                break;
            case R.id.btn_close:
                getDialog().dismiss();
                break;
        }
    }

    @OnLongClick(R.id.btn_version)
    public boolean onLongClick() {
        DialogUtils.secretDialog(getActivity());
        getDialog().dismiss();
        return Boolean.TRUE;
    }
}