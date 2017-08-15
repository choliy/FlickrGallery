package com.choliy.igor.flickrgallery.fragment;

import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.FrameLayout;
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
import com.choliy.igor.flickrgallery.util.InfoUtils;
import com.choliy.igor.flickrgallery.util.TimeUtils;
import com.wang.avi.AVLoadingIndicatorView;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnLongClick;

public class PictureFragment extends EventFragment implements RequestListener<String, Bitmap> {

    @BindView(R.id.picture_owner) TextView mOwner;
    @BindView(R.id.picture_title) TextView mTitle;
    @BindView(R.id.picture_image) ImageView mPicture;
    @BindView(R.id.picture_date) TextView mDate;
    @BindView(R.id.picture_resolution) TextView mResolution;
    @BindView(R.id.picture_description) TextView mDescription;
    @BindView(R.id.picture_return) ImageView mReturnImage;
    @BindView(R.id.picture_shadow) View mPictureShadow;
    @BindView(R.id.progress_view) AVLoadingIndicatorView mProgress;
    @BindView(R.id.selector_layout) FrameLayout mSelectorLayout;

    private GalleryItem mItem;
    private Bitmap mBitmap;
    private String mPictureTitle;
    private String mPictureDate;
    private String mPictureRes;
    private String mPictureDesc;

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
        setSelectorView();
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

    @OnClick(R.id.picture_return)
    public void onPictureClick() {
        getActivity().finish();
    }

    @OnLongClick({R.id.layout_title, R.id.layout_date, R.id.layout_resolution, R.id.layout_description})
    public boolean onLayoutClick(View view) {
        String text = null;
        switch (view.getId()) {
            case R.id.layout_title:
                FabUtils.copyData(getActivity(), mPictureTitle);
                text = getString(R.string.text_copied_title);
                break;
            case R.id.layout_date:
                FabUtils.copyData(getActivity(), mPictureDate);
                text = getString(R.string.text_copied_date);
                break;
            case R.id.layout_resolution:
                FabUtils.copyData(getActivity(), mPictureRes);
                text = getString(R.string.text_copied_resolution);
                break;
            case R.id.layout_description:
                FabUtils.copyData(getActivity(), mPictureDesc);
                text = getString(R.string.text_copied_description);
                break;
        }

        InfoUtils.showToast(getActivity(), text);
        return Boolean.TRUE;
    }

    private void setSelectorView() {
        View view = new View(getActivity());
        view.setLayoutParams(new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT));

        boolean newVersion = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
        if (newVersion)
            view.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.selector_list_new));
        else
            view.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.selector_list_old));

        view.setClickable(Boolean.TRUE);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new OnPictureClickTask(getActivity(), mBitmap, mItem).execute();
            }
        });

        mSelectorLayout.addView(view);
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
            mPictureTitle = getString(R.string.text_empty_title);
        else mPictureTitle = title;
        mTitle.setText(mPictureTitle);

        // set date
        String date = mItem.getDate();
        if (date.equals(FlickrConstants.STRING_EMPTY))
            mPictureDate = getString(R.string.text_empty_date);
        else mPictureDate = TimeUtils.formatDate(getActivity(), date);
        mDate.setText(mPictureDate);

        // set resolution
        loadPictureResolution();

        // set description
        mPictureDesc = ExtraUtils.parseDescription(getActivity(), mItem.getDescription());
        mDescription.setText(mPictureDesc);
    }

    private void loadPictureResolution() {
        Glide.with(this).load(getUrl(Boolean.TRUE)).into(new SimpleTarget<GlideDrawable>() {
            @Override
            public void onResourceReady(
                    GlideDrawable resource,
                    GlideAnimation<? super GlideDrawable> glideAnimation) {

                String width = String.valueOf(resource.getIntrinsicWidth());
                String height = String.valueOf(resource.getIntrinsicHeight());
                mPictureRes = getString(R.string.text_picture_res_data, width, height);
                mResolution.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorTextBlack));
                mResolution.setText(mPictureRes);
            }
        });
    }

    private String getUrl(boolean bigPicture) {
        return FabUtils.getPictureUrl(mItem, bigPicture);
    }
}