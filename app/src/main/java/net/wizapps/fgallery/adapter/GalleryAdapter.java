package net.wizapps.fgallery.adapter;

import android.content.Context;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import net.wizapps.fgallery.R;
import net.wizapps.fgallery.model.GalleryItem;
import net.wizapps.fgallery.tool.AnimationEnd;
import net.wizapps.fgallery.tool.Constants;
import net.wizapps.fgallery.tool.Events;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.PictureHolder> {

    private List<GalleryItem> mItems;
    private final String mGridSize;
    private final String mGridStyle;
    private boolean mIsAnimationOn;
    private boolean mClickable = Boolean.TRUE;

    public GalleryAdapter(
            List<GalleryItem> items,
            String gridSize,
            String gridStyle,
            boolean isAnimationOn) {

        mItems = items;
        mGridSize = gridSize;
        mGridStyle = gridStyle;
        mIsAnimationOn = isAnimationOn;
    }

    @Override
    public PictureHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(setupLayout(parent.getContext()), parent, Boolean.FALSE);
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

    private int setupLayout(Context context) {
        int layout;
        String cardStyle = context.getString(R.string.pref_grid_style_value_3);
        boolean newVersion = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;

        if (mGridStyle.equals(cardStyle) && newVersion) {
            layout = R.layout.list_item_card_new;
        } else if (mGridStyle.equals(cardStyle) && !newVersion) {
            layout = R.layout.list_item_card_old;
        } else if (newVersion) {
            layout = R.layout.list_item_simple_new;
        } else {
            layout = R.layout.list_item_simple_old;
        }

        return layout;
    }

    public void setClickable(boolean clickable) {
        mClickable = clickable;
    }

    public void setAnimation(boolean isAnimationOn) {
        mIsAnimationOn = isAnimationOn;
    }

    public class PictureHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.gallery_item_description) LinearLayout mDescription;
        @BindView(R.id.gallery_item_image) ImageView mPicture;
        @BindView(R.id.gallery_item_owner) TextView mOwner;
        @BindView(R.id.gallery_item_title) TextView mTitle;
        @BindView(R.id.highlight_picture) View mHighlight;

        PictureHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickable) {
                EventBus.getDefault().post(new Events.ItemPositionEvent(getAdapterPosition()));
            }
        }

        private void bindItem(int position) {
            loadPicture(position);
            setData(position);

            // Callback on the end of the list
            if (position == getItemCount() - Constants.ONE) {
                EventBus.getDefault().post(new Events.ItemLastEvent());
            }
        }

        private void loadPicture(int position) {
            String urlSmallList = mItems.get(position).getSmallListPicUrl();
            String urlList = mItems.get(position).getListPicUrl();
            String urlExtraSmall = mItems.get(position).getExtraSmallPicUrl();
            String urlSmall = mItems.get(position).getSmallPicUrl();

            Context context = itemView.getContext();
            String grid1x2 = context.getString(R.string.pref_grid_size_value_1);
            String grid2x3 = context.getString(R.string.pref_grid_size_value_2);
            String grid5x8 = context.getString(R.string.pref_grid_size_value_5);

            if (mGridSize.equals(grid2x3) && !urlExtraSmall.equals(Constants.JSON_NO_SUCH_SIZE)) {
                glideLoading(urlExtraSmall);
            } else if (mGridSize.equals(grid1x2) && !urlSmall.equals(Constants.JSON_NO_SUCH_SIZE)) {
                glideLoading(urlSmall);
            } else if (mGridSize.equals(grid1x2) && urlSmall.equals(Constants.JSON_NO_SUCH_SIZE) && !urlExtraSmall.equals(Constants.JSON_NO_SUCH_SIZE)) {
                glideLoading(urlExtraSmall);
            } else if (mGridSize.equals(grid5x8)) {
                glideLoading(urlSmallList);
            } else {
                glideLoading(urlList);
            }
        }

        private void setData(int position) {
            Context context = itemView.getContext();
            String grid1x2 = context.getString(R.string.pref_grid_size_value_1);
            String grid2x3 = context.getString(R.string.pref_grid_size_value_2);
            if (mGridSize.equals(grid1x2)) {
                setDescription(position);
            } else if (mGridSize.equals(grid2x3)) {
                setDescription(position);
                mOwner.setTextSize(context.getResources().getInteger(R.integer.text_size_17));
                mTitle.setTextSize(context.getResources().getInteger(R.integer.text_size_15));
                mTitle.setMaxLines(Constants.ONE);
            }
        }

        private void setDescription(int position) {
            mOwner.setText(mItems.get(position).getOwnerName());
            String title = mItems.get(position).getTitle();
            if (title.equals(Constants.EMPTY)) {
                mTitle.setText(itemView.getContext().getString(R.string.text_empty_title));
            } else {
                mTitle.setText(title);
            }
            mDescription.setVisibility(View.VISIBLE);
        }

        private void glideLoading(String url) {
            if (mIsAnimationOn) {
                Glide.with(itemView.getContext())
                        .load(url)
                        .error(R.mipmap.ic_error)
                        .animate(R.anim.anim_scale_picture)
                        .into(mPicture);
            } else {
                Glide.with(itemView.getContext())
                        .load(url)
                        .error(R.mipmap.ic_error)
                        .into(mPicture);
            }
        }

        public void highlightPicture() {
            Context context = itemView.getContext();
            final Animation show = AnimationUtils.loadAnimation(context, R.anim.anim_alpha_show);
            final Animation hide = AnimationUtils.loadAnimation(context, R.anim.anim_alpha_hide);
            show.setAnimationListener(new AnimationEnd() {
                @Override
                public void onAnimationEnd(Animation animation) {
                    mHighlight.startAnimation(hide);
                }
            });
            mHighlight.startAnimation(show);
        }
    }
}