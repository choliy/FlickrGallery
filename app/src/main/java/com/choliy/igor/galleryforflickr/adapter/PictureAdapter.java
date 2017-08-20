package com.choliy.igor.galleryforflickr.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.choliy.igor.galleryforflickr.fragment.GalleryFragment;
import com.choliy.igor.galleryforflickr.fragment.PictureFragment;
import com.choliy.igor.galleryforflickr.fragment.SavedFragment;
import com.choliy.igor.galleryforflickr.model.GalleryItem;

public class PictureAdapter extends FragmentStatePagerAdapter {

    private boolean mSavedLibrary;

    public PictureAdapter(FragmentManager fm, boolean savedLibrary) {
        super(fm);
        mSavedLibrary = savedLibrary;
    }

    @Override
    public Fragment getItem(int position) {
        GalleryItem item;
        if (mSavedLibrary) item = SavedFragment.sSavedItems.get(position);
        else item = GalleryFragment.sGalleryItems.get(position);
        return PictureFragment.newInstance(item);
    }

    @Override
    public int getCount() {
        if (mSavedLibrary) return SavedFragment.sSavedItems.size();
        else return GalleryFragment.sGalleryItems.size();
    }
}