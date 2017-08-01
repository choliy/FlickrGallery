package com.choliy.igor.flickrgallery.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.choliy.igor.flickrgallery.R;
import com.choliy.igor.flickrgallery.event.HistoryTitleEvent;
import com.choliy.igor.flickrgallery.model.HistoryItem;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryHolder> {

    private List<HistoryItem> mHistory = new ArrayList<>();

    @Override
    public HistoryAdapter.HistoryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_item_history, parent, Boolean.FALSE);
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

    public void setHistory(List<HistoryItem> history) {
        mHistory = history;
        notifyDataSetChanged();
    }

    public List<HistoryItem> getHistory() {
        return mHistory;
    }

    public HistoryItem removeItem(int position) {
        HistoryItem item = mHistory.get(position);
        mHistory.remove(position);
        notifyItemRemoved(position);
        return item;
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
            String historyTitle = mHistory.get(getAdapterPosition()).getHistoryTitle();
            EventBus.getDefault().post(new HistoryTitleEvent(historyTitle));
        }

        void bindHistory(int position) {
            HistoryItem item = mHistory.get(position);
            mTextTitle.setText(item.getHistoryTitle());
            mTextDate.setText(item.getHistoryDate());
            mTextTime.setText(item.getHistoryTime());
        }
    }
}