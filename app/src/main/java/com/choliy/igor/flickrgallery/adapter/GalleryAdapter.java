package com.choliy.igor.flickrgallery.adapter;

import android.content.Context;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.choliy.igor.flickrgallery.R;
import com.choliy.igor.flickrgallery.model.GalleryItem;
import com.choliy.igor.flickrgallery.util.PrefUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.PhotoHolder> {

    private Context mContext;
    private List<GalleryItem> mItems;
    private OnPhotoHolderListener mListener;

    public GalleryAdapter(
            Context context,
            List<GalleryItem> items,
            OnPhotoHolderListener listener) {

        mContext = context;
        mItems = items;
        mListener = listener;
    }

    @Override
    public PhotoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layout;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            layout = R.layout.list_item_gallery_lollipop;
        else layout = R.layout.list_item_gallery_kitkat;
        View view = LayoutInflater.from(mContext).inflate(layout, parent, Boolean.FALSE);
        return new PhotoHolder(view);
    }

    @Override
    public void onBindViewHolder(PhotoHolder holder, int position) {
        holder.bindItem(position);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public void updateItems(List<GalleryItem> items) {
        mItems = items;
        notifyDataSetChanged();
    }

    class PhotoHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.gallery_image_item) ImageView mPhotoImage;

        PhotoHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            String photoId = mItems.get(getAdapterPosition()).getId();
            mListener.onPhotoClicked(photoId);
        }

        private void bindItem(int position) {
            String url = mItems.get(position).getPictureUrl();
            boolean animationOn = PrefUtils.getAnimationSettings(mContext);
            if (animationOn) {
                Glide.with(mContext)
                        .load(url)
                        .animate(R.anim.anim_photo)
                        .into(mPhotoImage);
            } else Glide.with(mContext).load(url).into(mPhotoImage);

            // Callback on the end of the list
            if (position == getItemCount() - 1)
                mListener.onRequestedLastItem(getItemCount());
        }
    }

    public interface OnPhotoHolderListener {

        void onRequestedLastItem(int position);

        void onPhotoClicked(String photoId);

    }
}