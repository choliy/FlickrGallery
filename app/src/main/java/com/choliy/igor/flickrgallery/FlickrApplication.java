package com.choliy.igor.flickrgallery;

import android.app.Application;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class FlickrApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath(getString(R.string.font_normal))
                .setFontAttrId(R.attr.fontPath)
                .build());
    }
}