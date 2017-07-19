package com.choliy.igor.flickrgallery.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;

import com.choliy.igor.flickrgallery.R;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public abstract class BroadcastActivity extends AppCompatActivity {

    private BroadcastReceiver mOnShowReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            setResultCode(Activity.RESULT_CANCELED);
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter(getString(R.string.broadcast_action_name));
        registerReceiver(mOnShowReceiver, intentFilter, getString(R.string.broadcast_permission), null);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mOnShowReceiver);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}