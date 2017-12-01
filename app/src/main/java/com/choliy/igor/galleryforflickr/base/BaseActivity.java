package com.choliy.igor.galleryforflickr.base;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.choliy.igor.galleryforflickr.R;
import com.choliy.igor.galleryforflickr.tool.Events;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.ButterKnife;

public abstract class BaseActivity extends AppCompatActivity {

    protected abstract int layoutRes();

    protected abstract void setUi(Bundle savedInstanceState);

    private final BroadcastReceiver mOnShowReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            setResultCode(Activity.RESULT_CANCELED);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layoutRes());
        ButterKnife.bind(this);
        setUi(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
        IntentFilter intentFilter = new IntentFilter(getString(R.string.broadcast_action_name));
        registerReceiver(mOnShowReceiver, intentFilter, getString(R.string.broadcast_permission), null);
    }

    @Override
    protected void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
        unregisterReceiver(mOnShowReceiver);
    }

    @Subscribe
    public void onEvent(Events.BaseEvent event) {
        // empty event for avoid crashes in non subscribed activities
    }
}