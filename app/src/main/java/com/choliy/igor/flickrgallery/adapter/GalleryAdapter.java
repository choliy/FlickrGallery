package com.choliy.igor.flickrgallery.adapter;

import android.content.Context;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.choliy.igor.flickrgallery.R;
import com.choliy.igor.flickrgallery.interfaces.FlickrConstants;
import com.choliy.igor.flickrgallery.interfaces.OnPictureClickListener;
import com.choliy.igor.flickrgallery.model.GalleryItem;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.PictureHolder> {

    private Context mContext;
    private List<GalleryItem> mItems;
    private OnPictureClickListener mListener;
    private String mGridSize;
    private String mGridStyle;
    private boolean mIsAnimationOn;

    public GalleryAdapter(
            Context context,
            List<GalleryItem> items,
            OnPictureClickListener listener,
            String gridSize,
            String gridStyle,
            boolean isAnimationOn) {

        mContext = context;
        mItems = items;
        mListener = listener;
        mGridSize = gridSize;
        mGridStyle = gridStyle;
        mIsAnimationOn = isAnimationOn;
    }

    @Override
    public PictureHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(setupLayout(), parent, Boolean.FALSE);
        return new PictureHolder(view);
    }

    @Override
    public void onBindViewHolder(PictureHolder holder, int position) {
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

    private int setupLayout() {
        int layout;
        String cardStyle = mContext.getString(R.string.pref_grid_style_value_3);
        boolean newVersion = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;

        if (mGridStyle.equals(cardStyle) && newVersion)
            layout = R.layout.list_item_card_new;
        else if (mGridStyle.equals(cardStyle) && !newVersion)
            layout = R.layout.list_item_card_old;
        else if (newVersion)
            layout = R.layout.list_item_simple_new;
        else
            layout = R.layout.list_item_simple_old;

        return layout;
    }

    class PictureHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.gallery_item_description) LinearLayout mDescription;
        @BindView(R.id.gallery_item_image) ImageView mPicture;
        @BindView(R.id.gallery_item_owner) TextView mOwner;
        @BindView(R.id.gallery_item_title) TextView mTitle;

        PictureHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mListener.onPictureClicked(mItems.get(getAdapterPosition()));
        }

        private void bindItem(int position) {
            loadPicture(position);
            setData(position);

            // Callback on the end of the list
            if (position == getItemCount() - 1)
                mListener.onRequestedLastItem(getItemCount());
        }

        private void loadPicture(int position) {
            String urlList = mItems.get(position).getListPictureUrl();
            String urlExtraSmall = mItems.get(position).getExtraSmallPictureUrl();
            String urlSmall = mItems.get(position).getSmallPictureUrl();
            String grid1x2 = mContext.getString(R.string.pref_grid_size_value_1);
            String grid2x3 = mContext.getString(R.string.pref_grid_size_value_2);

            if (mGridSize.equals(grid2x3)
                    && !urlExtraSmall.equals(FlickrConstants.JSON_NO_SUCH_SIZE))
                glideLoading(urlExtraSmall);
            else if (mGridSize.equals(grid1x2)
                    && !urlSmall.equals(FlickrConstants.JSON_NO_SUCH_SIZE))
                glideLoading(urlSmall);
            else if (mGridSize.equals(grid1x2)
                    && urlSmall.equals(FlickrConstants.JSON_NO_SUCH_SIZE)
                    && !urlExtraSmall.equals(FlickrConstants.JSON_NO_SUCH_SIZE))
                glideLoading(urlExtraSmall);
            else
                glideLoading(urlList);
        }

        private void setData(int position) {
            String grid1x2 = mContext.getString(R.string.pref_grid_size_value_1);
            String grid2x3 = mContext.getString(R.string.pref_grid_size_value_2);
            if (mGridSize.equals(grid1x2)) {
                setDescription(position);
            } else if (mGridSize.equals(grid2x3)) {
                setDescription(position);
                mOwner.setTextSize(17.0f);
                mTitle.setTextSize(15.0f);
                mTitle.setMaxLines(1);
            }
        }

        private void setDescription(int position) {
            mOwner.setText(mItems.get(position).getOwnerName());
            String title = mItems.get(position).getTitle();
            if (title.equals(FlickrConstants.STRING_EMPTY))
                mTitle.setText(mContext.getString(R.string.text_empty_title));
            else
                mTitle.setText(title);

            mDescription.setVisibility(View.VISIBLE);
        }

        private void glideLoading(String url) {
            if (mIsAnimationOn) {
                Glide.with(mContext)
                        .load(url)
                        .animate(R.anim.anim_picture)
                        .into(mPicture);
            } else Glide.with(mContext).load(url).into(mPicture);
        }
    }
}