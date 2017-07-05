package com.choliy.igor.flickrgallery;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.choliy.igor.flickrgallery.util.DrawerUtils;
import com.choliy.igor.flickrgallery.util.FlickrUtils;
import com.choliy.igor.flickrgallery.util.PreferenceUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GalleryActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener {

    private FragmentManager mFragmentManager;
    private boolean mShowSearchType;

    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.search_view) SearchView mSearchView;
    @BindView(R.id.drawer_layout) DrawerLayout mDrawerLayout;
    @BindView(R.id.nav_view) NavigationView mNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        ButterKnife.bind(this);

        mFragmentManager = getSupportFragmentManager();
        Fragment fragment = mFragmentManager.findFragmentById(R.id.fragment_container);
        if (fragment == null) {
            fragment = GalleryFragment.newInstance();
            mFragmentManager
                    .beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit();
        }

        mNavigationView.setNavigationItemSelectedListener(this);
        setSupportActionBar(mToolbar);
        setupSearchView();

        if (savedInstanceState != null) {
            mShowSearchType = savedInstanceState.getBoolean(FlickrConstants.TOOLBAR_TYPE);
            toolbarType(mShowSearchType);
        }

        if (DrawerUtils.sIsAboutDialogShown)
            DrawerUtils.aboutDialog(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(FlickrConstants.TOOLBAR_TYPE, mShowSearchType);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START))
            mDrawerLayout.closeDrawer(GravityCompat.START);
        else super.onBackPressed();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_saved:
                // TODO
                break;
            case R.id.nav_history:
                // TODO
                break;
            case R.id.nav_settings:
                // TODO
                break;
            case R.id.nav_about:
                DrawerUtils.aboutDialog(this);
                break;
            case R.id.nav_share:
                DrawerUtils.shareIntent(this);
                break;
            case R.id.nav_email:
                DrawerUtils.emailIntent(this);
                break;
            case R.id.nav_feedback:
                DrawerUtils.feedbackIntent(this);
                break;
        }

        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    public void onToolbarClick(View view) {
        switch (view.getId()) {
            case R.id.toolbar_icon_menu:
                mDrawerLayout.openDrawer(mNavigationView);
                break;
            case R.id.toolbar_icon_back:
                toolbarType(mShowSearchType = false);
                break;
            case R.id.toolbar_icon_search:
                toolbarType(mShowSearchType = true);
                break;
        }
    }

    private void setupSearchView() {
        final ImageView closeIcon = (ImageView) mSearchView.findViewById(R.id.search_close_btn);
        TextView hintText = (TextView) mSearchView.findViewById(R.id.search_src_text);
        hintText.setHintTextColor(ContextCompat.getColor(this, R.color.textColorLightGray));

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()) {
                    closeIcon.setClickable(false);
                    closeIcon.setImageResource(FlickrConstants.NUMBER_ZERO);
                } else {
                    closeIcon.setClickable(true);
                    closeIcon.setImageResource(R.drawable.ic_close_black);
                }
                return true;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {
                PreferenceUtils.setStoredQuery(GalleryActivity.this, query);
                toolbarType(query.isEmpty());
                mFragmentManager
                        .beginTransaction()
                        .replace(R.id.fragment_container, GalleryFragment.newInstance())
                        .commit();
                return true;
            }
        });

        mSearchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String query = PreferenceUtils.getStoredQuery(GalleryActivity.this);
                mSearchView.setQuery(query, false);
            }
        });

        closeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSearchView.setQuery(FlickrConstants.STRING_EMPTY, false);
                FlickrUtils.showInfo(view, getString(R.string.text_search_query));
                PreferenceUtils.setStoredQuery(GalleryActivity.this, null);
            }
        });
    }

    private void toolbarType(boolean showSearchType) {
        LinearLayout toolbarMain = (LinearLayout) findViewById(R.id.toolbar_main);
        LinearLayout toolbarSearch = (LinearLayout) findViewById(R.id.toolbar_search);
        if (showSearchType) {
            toolbarMain.setVisibility(View.GONE);
            toolbarSearch.setVisibility(View.VISIBLE);
            mSearchView.setIconified(false);
        } else {
            toolbarMain.setVisibility(View.VISIBLE);
            toolbarSearch.setVisibility(View.GONE);
            mSearchView.setQuery(FlickrConstants.STRING_EMPTY, false);
            mSearchView.setIconified(true);
        }
    }
}