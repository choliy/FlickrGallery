package com.choliy.igor.flickrgallery;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;

import com.choliy.igor.flickrgallery.util.PreferenceUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GalleryActivity extends AppCompatActivity {

    private FragmentManager mFragmentManager;
    private boolean mShowSearchType;

    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.search_view) SearchView mSearchView;

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

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        setupSearchView();
        if (savedInstanceState != null) {
            mShowSearchType = savedInstanceState.getBoolean(FlickrConstants.TOOLBAR_TYPE);
            toolbarType(mShowSearchType);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(FlickrConstants.TOOLBAR_TYPE, mShowSearchType);
        super.onSaveInstanceState(outState);
    }

    public void onToolbarClick(View view) {
        switch (view.getId()) {
            case R.id.toolbar_icon_menu:
                // TODO
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
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                PreferenceUtils.setStoredQuery(GalleryActivity.this, query);
                toolbarType(false);
                mFragmentManager
                        .beginTransaction()
                        .replace(R.id.fragment_container, GalleryFragment.newInstance())
                        .commit();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        mSearchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String query = PreferenceUtils.getStoredQuery(GalleryActivity.this);
                mSearchView.setQuery(query, false);
            }
        });

        View closeButton = mSearchView.findViewById(android.support.v7.appcompat.R.id.search_close_btn);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSearchView.setQuery(FlickrConstants.STRING_EMPTY, false);
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
            mShowSearchType = false;
        }
    }
}