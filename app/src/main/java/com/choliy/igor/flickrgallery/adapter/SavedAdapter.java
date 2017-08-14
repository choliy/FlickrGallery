package com.choliy.igor.flickrgallery.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.choliy.igor.flickrgallery.FlickrConstants;
import com.choliy.igor.flickrgallery.R;
import com.choliy.igor.flickrgallery.activity.PictureActivity;
import com.choliy.igor.flickrgallery.model.GalleryItem;
import com.choliy.igor.flickrgallery.util.ExtraUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SavedAdapter extends RecyclerView.Adapter<SavedAdapter.SavedHolder> {

    private List<GalleryItem> mItems = new ArrayList<>();

    @Override
    public SavedHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_item_saved, parent, Boolean.FALSE);
        return new SavedHolder(view);
    }

    @Override
    public void onBindViewHolder(SavedHolder holder, int position) {
        holder.bindView(position);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public void setItems(List<GalleryItem> items) {
        mItems = items;
        notifyDataSetChanged();
    }

    public GalleryItem removeItem(int position) {
        GalleryItem item = mItems.get(position);
        mItems.remove(position);
        notifyItemRemoved(position);
        return item;
    }

    public void restoreItem(int position, GalleryItem item) {
        mItems.add(position, item);
        notifyItemInserted(position);
    }

    class SavedHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.picture_saved) ImageView mPicture;
        @BindView(R.id.text_saved_owner) TextView mOwner;
        @BindView(R.id.text_saved_title) TextView mTitle;
        private View mItemView;

        SavedHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
            mItemView = itemView;
        }

        @Override
        public void onClick(View view) {
            Context context = view.getContext();
            Intent intent = PictureActivity.newInstance(context, getAdapterPosition(), Boolean.TRUE);
            context.startActivity(intent);
        }

        private void bindView(int position) {
            String url = mItems.get(position).getListPicUrl();
            ExtraUtils.loadPicture(mItemView.getContext(), url, mPicture, null);
            mOwner.setText(mItems.get(position).getOwnerName());
            String title = mItems.get(position).getTitle();
            if (title.equals(FlickrConstants.STRING_EMPTY))
                title = mItemView.getContext().getString(R.string.text_empty_title);
            mTitle.setText(title);
        }
    }
}