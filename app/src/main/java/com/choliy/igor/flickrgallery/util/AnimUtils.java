package com.choliy.igor.flickrgallery.util;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;

import com.choliy.igor.flickrgallery.FlickrConstants;
import com.choliy.igor.flickrgallery.R;

public final class AnimUtils {

    private AnimUtils() {
    }

    public static void animateView(Context context, View view, boolean show) {
        int animResId;
        if (show) {
            animResId = R.anim.anim_translate_show_top;
            view.setVisibility(View.VISIBLE);
        } else {
            animResId = R.anim.anim_translate_hide_top;
            view.setVisibility(View.INVISIBLE);
        }
        startAnimation(context, view, animResId);
    }

    public static void animateBackButton(Context context, View view, boolean show) {
        int animResId;
        if (show) {
            animResId = R.anim.anim_translate_show_back;
            view.setVisibility(View.VISIBLE);
        } else {
            animResId = R.anim.anim_translate_hide_back;
            view.setVisibility(View.INVISIBLE);
        }
        startAnimation(context, view, animResId);
    }

    public static void animToolbarVisibility(Toolbar toolbar, boolean show) {
        if (show) {
            toolbar.animate()
                    .translationY(0)
                    .setInterpolator(new DecelerateInterpolator(2));
        } else {
            toolbar.animate()
                    .translationY(-toolbar.getHeight())
                    .setInterpolator(new AccelerateInterpolator(2));
        }
    }

    public static void animateToolbarType(
            Context context,
            SearchView searchView,
            boolean showSearchType) {

        Toolbar toolbar = (Toolbar) ((AppCompatActivity) context).findViewById(R.id.toolbar_gallery);
        LinearLayout toolbarMain = (LinearLayout) toolbar.findViewById(R.id.toolbar_main);
        LinearLayout toolbarSearch = (LinearLayout) toolbar.findViewById(R.id.toolbar_search);

        int animHideId = R.anim.anim_alpha_hide;
        int animShowId = R.anim.anim_alpha_show;
        if (showSearchType) {
            Animation hide = AnimationUtils.loadAnimation(context, animHideId);
            toolbarMain.startAnimation(hide);
            toolbarMain.setVisibility(View.GONE);

            Animation show = AnimationUtils.loadAnimation(context, animShowId);
            toolbarSearch.startAnimation(show);
            toolbarSearch.setVisibility(View.VISIBLE);

            searchView.setIconified(Boolean.FALSE);
        } else {
            Animation show = AnimationUtils.loadAnimation(context, animShowId);
            toolbarMain.startAnimation(show);
            toolbarMain.setVisibility(View.VISIBLE);

            Animation hide = AnimationUtils.loadAnimation(context, animHideId);
            toolbarSearch.startAnimation(hide);
            toolbarSearch.setVisibility(View.GONE);

            searchView.setQuery(FlickrConstants.STRING_EMPTY, Boolean.FALSE);
            searchView.setIconified(Boolean.TRUE);
        }
    }

    private static void startAnimation(Context context, View view, int animResId) {
        Animation animation = AnimationUtils.loadAnimation(context, animResId);
        view.startAnimation(animation);
    }
}