package com.choliy.igor.flickrgallery.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.choliy.igor.flickrgallery.R;
import com.choliy.igor.flickrgallery.util.PrefUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SplashActivity extends BroadcastActivity {

    @BindView(R.id.toolbar_splash) Toolbar mToolbar;
    @BindView(R.id.toolbar_icon_menu) ImageView mImageMenu;
    @BindView(R.id.toolbar_icon_search) ImageView mImageSearch;
    @BindView(R.id.toolbar_title) TextView mTextTitle;
    @BindView(R.id.dark_background) View mBackground;
    @BindView(R.id.image_splash_white) ImageView mImageWhite;
    @BindView(R.id.image_splash_shadow) ImageView mImageShadow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PrefUtils.isFirstStart(this);
        if (!PrefUtils.getSplashSettings(this)) startGalleryActivity();

        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        disableViews();
    }

    @Override
    protected void onStart() {
        super.onStart();
        startAnimation();
    }

    private void startAnimation() {
        firstAnimation();
    }

    private void firstAnimation() {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.anim_scale_show);
        animation.setDuration(getResources().getInteger(R.integer.anim_duration_500));
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                secondAnimation();
            }
        });
        mImageWhite.startAnimation(animation);
    }

    private void secondAnimation() {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.anim_alpha_show);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mImageWhite.setVisibility(View.INVISIBLE);
                thirdAnimation();
            }
        });
        mImageShadow.startAnimation(animation);
        mBackground.startAnimation(animation);
        mImageShadow.setVisibility(View.VISIBLE);
        mBackground.setVisibility(View.VISIBLE);
    }

    private void thirdAnimation() {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.anim_translate_toolbar);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                fourthAnimation();
            }
        });
        mToolbar.startAnimation(animation);
        mToolbar.setVisibility(View.VISIBLE);
    }

    private void fourthAnimation() {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.anim_alpha_show);
        animation.setDuration(getResources().getInteger(R.integer.anim_duration_300));
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                fifthAnimation();
            }
        });
        mImageSearch.startAnimation(animation);
        mImageSearch.setVisibility(View.VISIBLE);
    }

    private void fifthAnimation() {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.anim_alpha_show);
        animation.setDuration(getResources().getInteger(R.integer.anim_duration_300));
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                sixthAnimation();
            }
        });
        mTextTitle.startAnimation(animation);
        mTextTitle.setVisibility(View.VISIBLE);
    }

    private void sixthAnimation() {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.anim_alpha_show);
        animation.setDuration(getResources().getInteger(R.integer.anim_duration_300));
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                seventhAnimation();
            }
        });
        mImageMenu.startAnimation(animation);
        mImageMenu.setVisibility(View.VISIBLE);
    }

    private void seventhAnimation() {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.anim_alpha_hide);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                startGalleryActivity();
            }
        });
        mImageShadow.startAnimation(animation);
        mImageShadow.setVisibility(View.INVISIBLE);
    }

    private void startGalleryActivity() {
        startActivity(new Intent(SplashActivity.this, GalleryActivity.class));
        finish();
    }

    private void disableViews() {
        mImageMenu.setVisibility(View.INVISIBLE);
        mTextTitle.setVisibility(View.INVISIBLE);
        mImageSearch.setVisibility(View.INVISIBLE);
        mImageMenu.setClickable(Boolean.FALSE);
        mImageSearch.setClickable(Boolean.FALSE);
    }
}