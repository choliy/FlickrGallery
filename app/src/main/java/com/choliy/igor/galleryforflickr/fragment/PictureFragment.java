package com.choliy.igor.galleryforflickr.fragment;

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
import com.choliy.igor.galleryforflickr.R;
import com.choliy.igor.galleryforflickr.activity.ZoomActivity;
import com.choliy.igor.galleryforflickr.async.OnPictureClickTask;
import com.choliy.igor.galleryforflickr.base.BaseFragment;
import com.choliy.igor.galleryforflickr.model.GalleryItem;
import com.choliy.igor.galleryforflickr.tool.Constants;
import com.choliy.igor.galleryforflickr.tool.Events;
import com.choliy.igor.galleryforflickr.util.ExtraUtils;
import com.choliy.igor.galleryforflickr.util.FabUtils;
import com.choliy.igor.galleryforflickr.util.InfoUtils;
import com.choliy.igor.galleryforflickr.util.TimeUtils;
import com.wang.avi.AVLoadingIndicatorView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnLongClick;

public class PictureFragment extends BaseFragment implements RequestListener<String, Bitmap> {

    @BindView(R.id.picture_owner) TextView mOwner;
    @BindView(R.id.picture_title) TextView mTitle;
    @BindView(R.id.picture_image) ImageView mPicture;
    @BindView(R.id.picture_date) TextView mDate;
    @BindView(R.id.picture_resolution) TextView mResolution;
    @BindView(R.id.picture_description) TextView mDescription;
    @BindView(R.id.picture_shadow) View mPictureShadow;
    @BindView(R.id.progress_view) AVLoadingIndicatorView mProgress;
    @BindView(R.id.selector_layout) FrameLayout mSelectorLayout;

    private GalleryItem mItem;
    private Bitmap mBitmap;
    private boolean mClickable = Boolean.TRUE;

    public static Fragment newInstance(GalleryItem item) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.ITEM_KEY, item);
        PictureFragment fragment = new PictureFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int layoutRes() {
        return R.layout.fragment_picture;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mItem = getArguments().getParcelable(Constants.ITEM_KEY);
        setSelectorView();
        setData();
    }

    @Override
    public boolean onException(
            Exception e,
            String model,
            Target<Bitmap> target,
            boolean isFirstResource) {

        mClickable = Boolean.FALSE;
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

    @Subscribe
    public void onEvent(Events.PictureClickEvent event) {
        startActivity(ZoomActivity.newInstance(getActivity(), event.getPicUrl(), event.getBytes()));
    }

    @OnClick(R.id.picture_return)
    public void onReturnClick() {
        EventBus.getDefault().post(new Events.BackPressedEvent());
    }

    @OnLongClick({R.id.layout_title, R.id.layout_date, R.id.layout_resolution, R.id.layout_description})
    public boolean onLayoutClick(View view) {
        String text = null;
        switch (view.getId()) {
            case R.id.layout_title:
                FabUtils.copyData(getActivity(), mTitle.getText().toString());
                text = getString(R.string.text_copied_title);
                break;
            case R.id.layout_date:
                FabUtils.copyData(getActivity(), mDate.getText().toString());
                text = getString(R.string.text_copied_date);
                break;
            case R.id.layout_resolution:
                FabUtils.copyData(getActivity(), mResolution.getText().toString());
                text = getString(R.string.text_copied_resolution);
                break;
            case R.id.layout_description:
                FabUtils.copyData(getActivity(), mDescription.getText().toString());
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
        if (newVersion) {
            view.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.selector_list_new));
        } else {
            view.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.selector_list_old));
        }

        view.setClickable(Boolean.TRUE);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mClickable) new OnPictureClickTask(mBitmap, mItem).execute();
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
        if (title.equals(Constants.EMPTY)) {
            mTitle.setText(getString(R.string.text_empty_title));
        } else {
            mTitle.setText(title);
        }

        // set date
        String date = mItem.getDate();
        if (date.equals(Constants.EMPTY)) {
            mDate.setText(getString(R.string.text_empty_date));
        } else {
            mDate.setText(TimeUtils.formatDate(getActivity(), date));
        }

        // set resolution
        loadPictureResolution();

        // set description
        String description = ExtraUtils.parseDescription(getActivity(), mItem.getDescription());
        mDescription.setText(description);
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
        return FabUtils.getPictureUrl(mItem, bigPicture);
    }
}