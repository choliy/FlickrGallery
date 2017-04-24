package com.choliy.igor.flickrgallery;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.PhotoHolder> {

    private Context mContext;
    private List<GalleryItem> mItems;
    private OnPhotoHolderListener mListener;

    public GalleryAdapter(Context context, List<GalleryItem> items, OnPhotoHolderListener listener) {
        mContext = context;
        mItems = items;
        mListener = listener;
    }

    @Override
    public PhotoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        TextView photoTitle = new TextView(mContext);
        return new PhotoHolder(photoTitle);
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

        private TextView mPhotoTitle;

        PhotoHolder(View itemView) {
            super(itemView);
            mPhotoTitle = (TextView) itemView;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            String photoId = mItems.get(getAdapterPosition()).getId();
            mListener.onPhotoClicked(photoId);
        }

        private void bindItem(int position) {
            mPhotoTitle.setText(mItems.get(position).getTitle());

            // Callback on the end of the list
            if (position == getItemCount() - 1) mListener.onRequestedLastItem(getItemCount());
        }
    }

    public interface OnPhotoHolderListener {

        void onRequestedLastItem(int position);

        void onPhotoClicked(String photoId);

    }
}