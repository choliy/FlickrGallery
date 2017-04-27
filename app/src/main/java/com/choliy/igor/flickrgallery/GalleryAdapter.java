package com.choliy.igor.flickrgallery;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.PhotoHolder> {

    private static final String TAG = GalleryAdapter.class.getSimpleName();
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
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_item_gallery, parent, false);
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

        @BindView(R.id.gallery_image_item) ImageView mSinglePhoto;

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
            Log.i(TAG, "Photo URL: " + url);
            Glide.with(mContext)
                    .load(url)
                    .into(mSinglePhoto);

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