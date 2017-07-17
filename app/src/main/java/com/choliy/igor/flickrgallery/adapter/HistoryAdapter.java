package com.choliy.igor.flickrgallery.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.choliy.igor.flickrgallery.R;
import com.choliy.igor.flickrgallery.model.HistoryItem;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryHolder> {

    private Context mContext;
    private List<HistoryItem> mHistory;
    private OnHistoryClickListener mListener;

    public interface OnHistoryClickListener {
        void onHistoryClick(String historyTitle);
    }

    public HistoryAdapter(Context context,
                          List<HistoryItem> history,
                          OnHistoryClickListener listener) {
        mContext = context;
        mHistory = history;
        mListener = listener;
    }

    @Override
    public HistoryAdapter.HistoryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_item_history, parent, Boolean.FALSE);
        return new HistoryHolder(view);
    }

    @Override
    public void onBindViewHolder(HistoryAdapter.HistoryHolder holder, int position) {
        holder.bindHistory(position);
    }

    @Override
    public int getItemCount() {
        return mHistory.size();
    }

    public void updateHistory(List<HistoryItem> history) {
        mHistory = history;
        notifyDataSetChanged();
    }

    public List<HistoryItem> getHistory() {
        return mHistory;
    }

    public int removeItem(HistoryItem item) {
        int position = mHistory.indexOf(item);
        mHistory.remove(position);
        notifyItemRemoved(position);

        return position;
    }

    public void restoreItem(int position, HistoryItem item) {
        mHistory.add(position, item);
        notifyItemInserted(position);
    }

    class HistoryHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.text_history_title) TextView mTextTitle;
        @BindView(R.id.text_history_date) TextView mTextDate;
        @BindView(R.id.text_history_time) TextView mTextTime;

        HistoryHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mListener.onHistoryClick(mHistory.get(getAdapterPosition()).getHistoryTitle());
        }

        void bindHistory(int position) {
            HistoryItem item = mHistory.get(position);
            itemView.setTag(item.getId());
            mTextTitle.setText(item.getHistoryTitle());
            mTextDate.setText(item.getHistoryDate());
            mTextTime.setText(item.getHistoryTime());
        }
    }
}