package com.choliy.igor.flickrgallery.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.choliy.igor.flickrgallery.FlickrConstants;
import com.choliy.igor.flickrgallery.R;
import com.choliy.igor.flickrgallery.data.FlickrLab;
import com.choliy.igor.flickrgallery.event.HistoryStartEvent;
import com.choliy.igor.flickrgallery.event.HistoryTitleEvent;
import com.choliy.igor.flickrgallery.event.ToolbarEvent;
import com.choliy.igor.flickrgallery.event.TopListEvent;
import com.choliy.igor.flickrgallery.fragment.GalleryFragment;
import com.choliy.igor.flickrgallery.model.HistoryItem;
import com.choliy.igor.flickrgallery.util.AnimUtils;
import com.choliy.igor.flickrgallery.util.ExtraUtils;
import com.choliy.igor.flickrgallery.util.InfoUtils;
import com.choliy.igor.flickrgallery.util.NavUtils;
import com.choliy.igor.flickrgallery.util.PrefUtils;
import com.choliy.igor.flickrgallery.util.TimeUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GalleryActivity extends BroadcastActivity implements
        NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.image_top_list) ImageView mTopList;
    @BindView(R.id.toolbar_gallery) Toolbar mToolbar;
    @BindView(R.id.search_view) SearchView mSearchView;
    @BindView(R.id.drawer_layout) DrawerLayout mDrawerLayout;
    @BindView(R.id.nav_view) NavigationView mNavigationView;

    public static final int REQUEST_CODE = 777;
    private boolean mShowSearchType;

    public static Intent newIntent(Context context) {
        return new Intent(context, GalleryActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        ButterKnife.bind(this);
        setupUi(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(FlickrConstants.TOOLBAR_KEY, mShowSearchType);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START))
            mDrawerLayout.closeDrawer(GravityCompat.START);
        else if (mShowSearchType)
            animateToolbar(mShowSearchType = Boolean.FALSE);
        else finishApp();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            AnimUtils.animToolbarVisibility(mToolbar, Boolean.TRUE);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new GalleryFragment())
                    .commit();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        NavUtils.onNavDrawerClicked(this, item.getItemId());
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return Boolean.TRUE;
    }

    @Subscribe
    public void onEvent(HistoryTitleEvent event) {
        PrefUtils.setStoredQuery(this, event.getHistoryTitle());
        showSearchToolbar();
    }

    @Subscribe
    public void onEvent(HistoryStartEvent event) {
        if (event.isStartClicked()) showSearchToolbar();
    }

    @Subscribe
    public void onEvent(ToolbarEvent event) {
        AnimUtils.animToolbarVisibility(mToolbar, event.isShowToolbar());
        AnimUtils.animateView(this, mTopList, !event.isShowToolbar());
    }

    @OnClick(R.id.image_top_list)
    public void onTopClicked() {
        EventBus.getDefault().post(new TopListEvent(FlickrConstants.INT_ZERO));
    }

    public void onToolbarClick(View view) {
        switch (view.getId()) {
            case R.id.toolbar_icon_menu:
                mDrawerLayout.openDrawer(mNavigationView);
                break;
            case R.id.toolbar_icon_back:
                animateToolbar(mShowSearchType = Boolean.FALSE);
                break;
            case R.id.toolbar_icon_search:
                animateToolbar(mShowSearchType = Boolean.TRUE);
                break;
        }
    }

    private void setupUi(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragment_container, new GalleryFragment())
                    .commit();
        } else {
            mShowSearchType = savedInstanceState.getBoolean(FlickrConstants.TOOLBAR_KEY);
            animateToolbar(mShowSearchType);
        }

        mNavigationView.setNavigationItemSelectedListener(this);
        ExtraUtils.setupDevDate(this, mNavigationView);
        setSupportActionBar(mToolbar);
        setupSearchView();
    }

    private void setupSearchView() {
        final ImageView closeIcon = (ImageView) mSearchView.findViewById(R.id.search_close_btn);
        TextView hintText = (TextView) mSearchView.findViewById(R.id.search_src_text);
        hintText.setHintTextColor(ContextCompat.getColor(this, R.color.colorTextLightGray));
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()) {
                    closeIcon.setClickable(Boolean.FALSE);
                    closeIcon.setImageResource(FlickrConstants.INT_ZERO);
                } else {
                    closeIcon.setClickable(Boolean.TRUE);
                    closeIcon.setImageResource(R.drawable.ic_close);
                }
                return Boolean.TRUE;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {
                PrefUtils.setStoredQuery(GalleryActivity.this, query);
                HistoryItem item = new HistoryItem(
                        query,
                        TimeUtils.getDate(),
                        TimeUtils.getTime(GalleryActivity.this));
                FlickrLab.getInstance(GalleryActivity.this).addHistory(item, Boolean.FALSE);

                animateToolbar(mShowSearchType = Boolean.FALSE);
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, new GalleryFragment())
                        .commit();

                return Boolean.TRUE;
            }
        });

        mSearchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String query = PrefUtils.getStoredQuery(GalleryActivity.this);
                mSearchView.setQuery(query, Boolean.FALSE);
            }
        });

        closeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSearchView.setQuery(FlickrConstants.STRING_EMPTY, Boolean.FALSE);
                InfoUtils.showShortShack(view, getString(R.string.text_search_query));
                PrefUtils.setStoredQuery(GalleryActivity.this, null);
            }
        });
    }

    private void showSearchToolbar() {
        AnimUtils.animToolbarVisibility(mToolbar, Boolean.TRUE);
        animateToolbar(mShowSearchType = Boolean.TRUE);
    }

    private void animateToolbar(boolean showSearchType) {
        AnimUtils.animateToolbarType(
                GalleryActivity.this,
                mSearchView,
                showSearchType);
    }

    private void finishApp() {
        String text = getString(R.string.text_exit);
        Snackbar snackbar = Snackbar.make(mToolbar, text, Snackbar.LENGTH_LONG);
        snackbar.setAction(R.string.dialog_yes_btn, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        snackbar.show();
    }
}