package com.choliy.igor.flickrgallery.fragment;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.choliy.igor.flickrgallery.FlickrConstants;
import com.choliy.igor.flickrgallery.R;
import com.choliy.igor.flickrgallery.async.OnPictureClickTask;
import com.choliy.igor.flickrgallery.model.GalleryItem;
import com.choliy.igor.flickrgallery.util.ExtraUtils;
import com.choliy.igor.flickrgallery.util.FabUtils;
import com.choliy.igor.flickrgallery.util.PrefUtils;
import com.choliy.igor.flickrgallery.util.TimeUtils;
import com.wang.avi.AVLoadingIndicatorView;

import butterknife.BindView;
import butterknife.OnClick;

public class PictureFragment extends EventFragment {

    @BindView(R.id.picture_owner) TextView mOwner;
    @BindView(R.id.picture_title) TextView mTitle;
    @BindView(R.id.picture_image) ImageView mPicture;
    @BindView(R.id.picture_date) TextView mDate;
    @BindView(R.id.picture_resolution) TextView mResolution;
    @BindView(R.id.picture_description) TextView mDescription;
    @BindView(R.id.progress_view) AVLoadingIndicatorView mProgress;
    @BindView(R.id.top_picture_shadow) View mTopShadow;
    @BindView(R.id.bottom_picture_shadow) View mBottomShadow;

    private GalleryItem mItem;
    private Bitmap mBitmap;
    private Animation mAnimation;
    private boolean mAnimationOn;

    public static Fragment newInstance(GalleryItem item) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(FlickrConstants.ITEM_KEY, item);
        PictureFragment fragment = new PictureFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    int layoutRes() {
        return R.layout.fragment_picture;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mItem = getArguments().getParcelable(FlickrConstants.ITEM_KEY);
        mAnimationOn = PrefUtils.getAnimationSettings(getActivity());
        mAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.anim_scale_picture);
        setData();
    }

    @OnClick(R.id.picture_view)
    public void onPictureClick() {
        new OnPictureClickTask(getActivity(), mBitmap, mItem).execute();
    }

    private void setData() {
        // set owner name
        mOwner.setText(mItem.getOwnerName());

        // set title
        String title = mItem.getTitle();
        if (title.equals(FlickrConstants.STRING_EMPTY))
            mTitle.setText(getString(R.string.text_empty_title));
        else
            mTitle.setText(title);

        // set resolution
        loadPictureResolution();

        // set date
        String date = mItem.getDate();
        if (date.equals(FlickrConstants.STRING_EMPTY))
            mDate.setText(getString(R.string.text_empty_date));
        else {
            date = TimeUtils.formatDate(getActivity(), date);
            mDate.setText(date);
        }

        // set description
        String description = mItem.getDescription();
        mDescription.setText(ExtraUtils.parseDescription(getActivity(), description));

        // set picture
        String picUrl = getUrl(Boolean.FALSE);
        new PictureTask().execute(picUrl);
    }

    private void setPicture(Bitmap bitmap) {
        if (mAnimationOn) {
            mPicture.setVisibility(View.INVISIBLE);
            mPicture.setImageBitmap(bitmap);
            mPicture.startAnimation(mAnimation);
            mPicture.setVisibility(View.VISIBLE);
        } else
            mPicture.setImageBitmap(bitmap);

        mProgress.smoothToHide();
        mPicture.setClickable(Boolean.TRUE);
        mTopShadow.setVisibility(View.VISIBLE);
        mBottomShadow.setVisibility(View.VISIBLE);
    }

    private void loadPictureResolution() {
        Glide.with(this).load(getUrl(Boolean.TRUE)).into(new SimpleTarget<GlideDrawable>() {
            @Override
            public void onResourceReady(
                    GlideDrawable resource,
                    GlideAnimation<? super GlideDrawable> glideAnimation) {

                String width = String.valueOf(resource.getIntrinsicWidth());
                String height = String.valueOf(resource.getIntrinsicHeight());
                String resolution = getString(R.string.text_picture_res_data, width, height);
                mResolution.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorTextBlack));
                mResolution.setText(resolution);
            }
        });
    }

    private String getUrl(boolean bigPicture) {
        return FabUtils.getPictureUrl(getActivity(), mItem, bigPicture);
    }

    private class PictureTask extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... params) {
            return FabUtils.getBitmapFromURL(params[FlickrConstants.INT_ZERO]);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            mBitmap = bitmap;
            setPicture(bitmap);
        }
    }
}