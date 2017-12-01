package net.wizapps.fgallery.activity;

import android.os.Bundle;
import android.view.View;

import net.wizapps.fgallery.R;
import net.wizapps.fgallery.base.BaseActivity;
import net.wizapps.fgallery.fragment.SavedFragment;
import net.wizapps.fgallery.tool.Events;
import net.wizapps.fgallery.util.AnimUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.OnClick;

public class SavedActivity extends BaseActivity {

    @BindView(R.id.toolbar_saved) View mToolbar;
    @BindView(R.id.image_top_list) View mTopList;

    @Override
    protected int layoutRes() {
        return R.layout.activity_saved;
    }

    @Override
    protected void setUi(Bundle savedInstanceState) {
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