package com.choliy.igor.galleryforflickr.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.choliy.igor.galleryforflickr.R;
import com.choliy.igor.galleryforflickr.base.BaseActivity;
import com.choliy.igor.galleryforflickr.fragment.SavedFragment;
import com.choliy.igor.galleryforflickr.tool.Events;
import com.choliy.igor.galleryforflickr.util.AnimUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.OnClick;

public class SavedActivity extends BaseActivity {

    @BindView(R.id.toolbar_saved) Toolbar mToolbar;
    @BindView(R.id.image_top_list) ImageView mTopList;

    @Override
    public int layoutRes() {
        return R.layout.activity_saved;
    }

    @Override
    public void setUi(Bundle savedInstanceState) {
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
                EventBus.getDefault().post(new Events.RemovePicEvent());
                break;
            case R.id.image_top_list:
                EventBus.getDefault().post(new Events.TopListEvent());
                break;
        }
    }

    @Subscribe
    public void onEvent(Events.ToolbarVisibilityEvent event) {
        AnimUtils.animToolbarVisibility(mToolbar, event.isShowToolbar());
        AnimUtils.animateView(this, mTopList, !event.isShowToolbar());
    }
}