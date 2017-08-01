package com.choliy.igor.flickrgallery.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.choliy.igor.flickrgallery.FlickrConstants;
import com.choliy.igor.flickrgallery.R;
import com.choliy.igor.flickrgallery.event.RemovePicEvent;
import com.choliy.igor.flickrgallery.event.ToolbarEvent;
import com.choliy.igor.flickrgallery.fragment.SavedFragment;
import com.choliy.igor.flickrgallery.event.TopListEvent;
import com.choliy.igor.flickrgallery.util.AnimUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SavedActivity extends BroadcastActivity {

    @BindView(R.id.toolbar_saved) Toolbar mToolbar;
    @BindView(R.id.image_top_list) ImageView mTopList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved);
        ButterKnife.bind(this);

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.saved_container, new SavedFragment())
                    .commit();
        }
    }

    @OnClick({R.id.ic_return_saved, R.id.ic_delete_pictures, R.id.image_top_list})
    public void onBackClick(View view) {
        switch (view.getId()) {
            case R.id.ic_return_saved:
                finish();
                break;
            case R.id.ic_delete_pictures:
                EventBus.getDefault().post(new RemovePicEvent(Boolean.TRUE));
                break;
            case R.id.image_top_list:
                EventBus.getDefault().post(new TopListEvent(FlickrConstants.INT_ZERO));
                break;
        }
    }

    @Subscribe
    public void onEvent(ToolbarEvent event) {
        AnimUtils.animToolbarVisibility(mToolbar, event.isShowToolbar());
        AnimUtils.animateView(this, mTopList, !event.isShowToolbar());
    }
}