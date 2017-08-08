package com.choliy.igor.flickrgallery.fragment;

import android.content.Intent;
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
import com.choliy.igor.flickrgallery.activity.ZoomActivity;
import com.choliy.igor.flickrgallery.model.GalleryItem;
import com.choliy.igor.flickrgallery.util.ExtraUtils;
import com.choliy.igor.flickrgallery.util.FabUtils;
import com.choliy.igor.flickrgallery.util.TimeUtils;
import com.wang.avi.AVLoadingIndicatorView;

import butterknife.BindView;
import butterknife.OnClick;

public class PictureFragment extends EventFragment implements RequestListener {

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

    @OnClick(R.id.picture_view)
    public void onPictureClick() {
        Intent intent = new Intent(getActivity(), ZoomActivity.class);
        intent.putExtra(FlickrConstants.PICTURE_KEY, getUrl(Boolean.TRUE));
        startActivity(intent);
    }

    @Override
    public boolean onException(Exception e, Object model, Target target, boolean isFirstResource) {
        return Boolean.FALSE;
    }

    @Override
    public boolean onResourceReady(
            Object resource,
            Object model,
            Target target,
            boolean isFromMemoryCache,
            boolean isFirstResource) {

        mProgress.smoothToHide();
        mTopShadow.setVisibility(View.VISIBLE);
        mBottomShadow.setVisibility(View.VISIBLE);
        return Boolean.FALSE;
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

        // ser resolution
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

        // set picture url
        String url = getUrl(Boolean.FALSE);
        ExtraUtils.loadPicture(getActivity(), url, mPicture, this);
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