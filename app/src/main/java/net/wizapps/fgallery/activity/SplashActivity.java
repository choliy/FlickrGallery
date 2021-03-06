package net.wizapps.fgallery.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import net.wizapps.fgallery.R;
import net.wizapps.fgallery.tool.OnAnimationEndListener;
import net.wizapps.fgallery.util.PrefUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SplashActivity extends AppCompatActivity {

    @BindView(R.id.toolbar_splash) View mToolbar;
    @BindView(R.id.toolbar_icon_menu) View mImageMenu;
    @BindView(R.id.toolbar_icon_search) View mImageSearch;
    @BindView(R.id.toolbar_title) View mTextTitle;
    @BindView(R.id.dark_background) View mBackground;
    @BindView(R.id.image_splash_clear) View mImageClear;
    @BindView(R.id.image_splash_shadow) View mImageShadow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!PrefUtils.getSplashSettings(this)) {
            startGalleryActivity();
        } else {
            setContentView(R.layout.activity_splash);
            ButterKnife.bind(this);
            disableViews();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        firstAnimation();
    }

    private void firstAnimation() {
        Animation animScale = AnimationUtils.loadAnimation(this, R.anim.anim_scale_start_show);
        Animation animAlpha = AnimationUtils.loadAnimation(this, R.anim.anim_alpha_show);
        animScale.setAnimationListener(new OnAnimationEndListener() {
            @Override
            public void onAnimationEnd(Animation animation) {
                secondAnimation();
            }
        });
        mImageClear.startAnimation(animScale);
        mBackground.startAnimation(animAlpha);
        mBackground.setVisibility(View.VISIBLE);
    }

    private void secondAnimation() {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.anim_scale_start_hide);
        animation.setAnimationListener(new OnAnimationEndListener() {
            @Override
            public void onAnimationEnd(Animation animation) {
                thirdAnimation();
            }
        });
        mImageClear.startAnimation(animation);
    }

    private void thirdAnimation() {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.anim_alpha_show);
        animation.setAnimationListener(new OnAnimationEndListener() {
            @Override
            public void onAnimationEnd(Animation animation) {
                mImageClear.setVisibility(View.INVISIBLE);
                fourthAnimation();
            }
        });
        mImageShadow.startAnimation(animation);
        mImageShadow.setVisibility(View.VISIBLE);
    }

    private void fourthAnimation() {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.anim_translate_toolbar);
        animation.setAnimationListener(new OnAnimationEndListener() {
            @Override
            public void onAnimationEnd(Animation animation) {
                fifthAnimation();
            }
        });
        mToolbar.startAnimation(animation);
        mToolbar.setVisibility(View.VISIBLE);
    }

    private void fifthAnimation() {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.anim_alpha_show);
        animation.setDuration(getResources().getInteger(R.integer.anim_duration_300));
        animation.setAnimationListener(new OnAnimationEndListener() {
            @Override
            public void onAnimationEnd(Animation animation) {
                sixthAnimation();
            }
        });
        mImageSearch.startAnimation(animation);
        mImageSearch.setVisibility(View.VISIBLE);
    }

    private void sixthAnimation() {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.anim_alpha_show);
        animation.setDuration(getResources().getInteger(R.integer.anim_duration_300));
        animation.setAnimationListener(new OnAnimationEndListener() {
            @Override
            public void onAnimationEnd(Animation animation) {
                seventhAnimation();
            }
        });
        mTextTitle.startAnimation(animation);
        mTextTitle.setVisibility(View.VISIBLE);
    }

    private void seventhAnimation() {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.anim_alpha_show);
        animation.setDuration(getResources().getInteger(R.integer.anim_duration_300));
        animation.setAnimationListener(new OnAnimationEndListener() {
            @Override
            public void onAnimationEnd(Animation animation) {
                eighthAnimation();
            }
        });
        mImageMenu.startAnimation(animation);
        mImageMenu.setVisibility(View.VISIBLE);
    }

    private void eighthAnimation() {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.anim_scale_end_show);
        animation.setAnimationListener(new OnAnimationEndListener() {
            @Override
            public void onAnimationEnd(Animation animation) {
                ninthAnimation();
            }
        });
        mImageShadow.startAnimation(animation);
    }

    private void ninthAnimation() {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.anim_scale_end_hide);
        animation.setAnimationListener(new OnAnimationEndListener() {
            @Override
            public void onAnimationEnd(Animation animation) {
                startGalleryActivity();
            }
        });
        mImageShadow.startAnimation(animation);
        mImageShadow.setVisibility(View.INVISIBLE);
    }

    private void startGalleryActivity() {
        startActivity(new Intent(this, GalleryActivity.class));
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