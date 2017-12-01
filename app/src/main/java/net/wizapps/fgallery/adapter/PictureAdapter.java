package net.wizapps.fgallery.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import net.wizapps.fgallery.fragment.GalleryFragment;
import net.wizapps.fgallery.fragment.PictureFragment;
import net.wizapps.fgallery.fragment.SavedFragment;
import net.wizapps.fgallery.model.GalleryItem;

public class PictureAdapter extends FragmentStatePagerAdapter {

    private final boolean mSavedLibrary;

    public PictureAdapter(FragmentManager fm, boolean savedLibrary) {
        super(fm);
        mSavedLibrary = savedLibrary;
    }

    @Override
    public Fragment getItem(int position) {
        GalleryItem item;
        if (mSavedLibrary) {
            item = SavedFragment.sSavedItems.get(position);
        } else {
            item = GalleryFragment.sGalleryItems.get(position);
        }
        return PictureFragment.newInstance(item);
    }

    @Override
    public int getCount() {
        if (mSavedLibrary) {
            return SavedFragment.sSavedItems.size();
        } else {
            return GalleryFragment.sGalleryItems.size();
        }
    }
}