package com.choliy.igor.flickrgallery.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.choliy.igor.flickrgallery.FlickrConstants;
import com.choliy.igor.flickrgallery.R;
import com.choliy.igor.flickrgallery.async.OnPictureClickTask;
import com.choliy.igor.flickrgallery.model.GalleryItem;
import com.choliy.igor.flickrgallery.util.ExtraUtils;
import com.choliy.igor.flickrgallery.util.FabUtils;
import com.choliy.igor.flickrgallery.util.TimeUtils;
import com.wang.avi.AVLoadingIndicatorView;

import butterknife.BindView;
import butterknife.OnClick;

public class PictureFragment extends EventFragment implements RequestListener<String, Bitmap> {

    @BindView(R.id.picture_owner) TextView mOwner;
    @BindView(R.id.picture_title) TextView mTitle;
    @BindView(R.id.picture_image) ImageView mPicture;
    @BindView(R.id.picture_date) TextView mDate;
    @BindView(R.id.picture_resolution) TextView mResolution;
    @BindView(R.id.picture_description) TextView mDescription;
    @BindView(R.id.progress_view) AVLoadingIndicatorView mProgress;
    @BindView(R.id.picture_return) ImageView mReturnImage;
    @BindView(R.id.picture_shadow) View mPictureShadow;

    private GalleryItem mItem;
    private Bitmap mBitmap;

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
        setData();
    }

    @Override
    public boolean onException(
            Exception e,
            String model,
            Target<Bitmap> target,
            boolean isFirstResource) {
        return Boolean.FALSE;
    }

    @Override
    public boolean onResourceReady(
            Bitmap resource,
            String model,
            Target<Bitmap> target,
            boolean isFromMemoryCache,
            boolean isFirstResource) {

        mBitmap = resource;
        mProgress.smoothToHide();
        mPicture.setClickable(Boolean.TRUE);
        mPictureShadow.setVisibility(View.VISIBLE);
        return Boolean.FALSE;
    }

    @OnClick({R.id.picture_view, R.id.picture_return})
    public void onPictureClick(View view) {
        switch (view.getId()) {
            case R.id.picture_view:
                new OnPictureClickTask(getActivity(), mBitmap, mItem).execute();
                break;
            case R.id.picture_return:
                getActivity().finish();
                break;
        }
    }

    private void setData() {
        // set owner name
        mOwner.setText(mItem.getOwnerName());

        // set picture
        String picUrl = getUrl(Boolean.FALSE);
        ExtraUtils.loadPicture(getActivity(), picUrl, mPicture, this);

        // set title
        String title = mItem.getTitle();
        if (title.equals(FlickrConstants.STRING_EMPTY))
            mTitle.setText(getString(R.string.text_empty_title));
        else
            mTitle.setText(title);

        // set date
        String date = mItem.getDate();
        if (date.equals(FlickrConstants.STRING_EMPTY))
            mDate.setText(getString(R.string.text_empty_date));
        else {
            date = TimeUtils.formatDate(getActivity(), date);
            mDate.setText(date);
        }

        // set resolution
        loadPictureResolution();

        // set description
        String description = mItem.getDescription();
        mDescription.setText(ExtraUtils.parseDescription(getActivity(), description));
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
}