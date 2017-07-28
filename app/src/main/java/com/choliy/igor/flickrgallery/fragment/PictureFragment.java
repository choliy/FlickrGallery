package com.choliy.igor.flickrgallery.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.choliy.igor.flickrgallery.FlickrConstants;
import com.choliy.igor.flickrgallery.R;
import com.choliy.igor.flickrgallery.model.GalleryItem;
import com.choliy.igor.flickrgallery.util.ExtraUtils;
import com.choliy.igor.flickrgallery.util.PrefUtils;
import com.choliy.igor.flickrgallery.util.TimeUtils;
import com.wang.avi.AVLoadingIndicatorView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PictureFragment extends Fragment implements RequestListener {

    @BindView(R.id.picture_owner) TextView mOwner;
    @BindView(R.id.picture_title) TextView mTitle;
    @BindView(R.id.picture_image) ImageView mPicture;
    @BindView(R.id.picture_date) TextView mDate;
    @BindView(R.id.picture_description) TextView mDescription;
    @BindView(R.id.progress_view) AVLoadingIndicatorView mProgressView;
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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(Boolean.TRUE);
        mItem = getArguments().getParcelable(FlickrConstants.ITEM_KEY);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_picture, container, Boolean.FALSE);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        setData();
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

        mProgressView.smoothToHide();
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
        String url;
        if (!mItem.getMediumPictureUrl().equals(FlickrConstants.JSON_NO_SUCH_SIZE))
            url = mItem.getMediumPictureUrl();
        else if (!mItem.getSmallPictureUrl().equals(FlickrConstants.JSON_NO_SUCH_SIZE))
            url = mItem.getSmallPictureUrl();
        else if (!mItem.getExtraSmallPictureUrl().equals(FlickrConstants.JSON_NO_SUCH_SIZE)) {
            smallPicture();
            url = mItem.getExtraSmallPictureUrl();
        } else {
            smallPicture();
            url = mItem.getListPictureUrl();
        }

        if (PrefUtils.getAnimationSettings(getActivity())) {
            Glide.with(getActivity())
                    .load(url)
                    .listener(this)
                    .animate(R.anim.anim_scale_picture)
                    .into(mPicture);
        } else Glide.with(getActivity()).load(url).listener(this).into(mPicture);
    }

    private void smallPicture() {
        ExtraUtils.showLongInfo(getActivity(), getString(R.string.text_picture_small));
    }
}