package com.choliy.igor.flickrgallery.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.choliy.igor.flickrgallery.FlickrConstants;
import com.choliy.igor.flickrgallery.R;
import com.choliy.igor.flickrgallery.adapter.HistoryAdapter;
import com.choliy.igor.flickrgallery.data.FlickrLab;
import com.choliy.igor.flickrgallery.event.HistoryStartEvent;
import com.choliy.igor.flickrgallery.event.HistoryTitleEvent;
import com.choliy.igor.flickrgallery.loader.HistoryLoader;
import com.choliy.igor.flickrgallery.model.HistoryItem;
import com.choliy.igor.flickrgallery.util.DialogUtils;
import com.choliy.igor.flickrgallery.util.InfoUtils;
import com.choliy.igor.flickrgallery.util.PrefUtils;
import com.wang.avi.AVLoadingIndicatorView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class HistoryFragment extends CustomFragment implements
        LoaderManager.LoaderCallbacks<List<HistoryItem>> {

    @BindView(R.id.progress_view) AVLoadingIndicatorView mProgress;
    @BindView(R.id.rv_history) RecyclerView mRvHistory;
    @BindView(R.id.btn_history_clear) TextView mBtnClear;
    @BindView(R.id.layout_no_history) LinearLayout mNoHistory;

    public static final String TAG = HistoryFragment.class.getSimpleName();
    private List<HistoryItem> mSavedHistory;
    private HistoryAdapter mHistoryAdapter;

    @Override
    int layoutRes() {
        return R.layout.dialog_history;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        setupUi();
        getActivity().getSupportLoaderManager().restartLoader(HistoryLoader.HISTORY_LOADER_ID, null, this);
    }

    @Override
    public Loader<List<HistoryItem>> onCreateLoader(int id, Bundle args) {
        return new HistoryLoader(getActivity());
    }

    @Override
    public void onLoadFinished(Loader<List<HistoryItem>> loader, List<HistoryItem> historyItems) {
        mHistoryAdapter.setHistory(historyItems);
        mProgress.smoothToHide();
        checkHistory();
    }

    @Override
    public void onLoaderReset(Loader<List<HistoryItem>> loader) {
    }

    @OnClick({R.id.btn_history_start, R.id.btn_history_clear, R.id.btn_history_close})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_history_start:
                closeHistoryDialog();
                EventBus.getDefault().post(new HistoryStartEvent(Boolean.TRUE));
                break;
            case R.id.btn_history_clear:
                DialogUtils.clearDialog(getActivity(), new SaveHistoryAsyncTask());
                break;
            case R.id.btn_history_close:
                closeHistoryDialog();
                break;
        }
    }

    @Subscribe
    public void onEvent(HistoryTitleEvent event) {
        if (!TextUtils.isEmpty(event.getHistoryTitle())) closeHistoryDialog();
    }

    private void setupUi() {
        mHistoryAdapter = new HistoryAdapter();
        mRvHistory.setAdapter(mHistoryAdapter);
        mRvHistory.setHasFixedSize(Boolean.TRUE);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());

        // set current property to false for avoid listItem width issue
        layoutManager.setAutoMeasureEnabled(Boolean.FALSE);
        mRvHistory.setLayoutManager(layoutManager);
        ItemTouchHelper touchHelper = new ItemTouchHelper(new OnHistorySwipeCallback());
        touchHelper.attachToRecyclerView(mRvHistory);
    }

    private void checkHistory() {
        if (mHistoryAdapter.getItemCount() > FlickrConstants.INT_ZERO) {
            mNoHistory.setVisibility(View.GONE);
            mBtnClear.setVisibility(View.VISIBLE);
        } else {
            mNoHistory.setVisibility(View.VISIBLE);
            mBtnClear.setVisibility(View.INVISIBLE);
        }
    }

    private void restoreHistory() {
        String text = getActivity().getString(R.string.dialog_restore_cleaned);
        Snackbar snackbar = Snackbar.make(mBtnClear, text, Snackbar.LENGTH_LONG);
        snackbar.setAction(R.string.dialog_undo_btn, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SaveHistoryAsyncTask().execute(Boolean.FALSE);
            }
        });
        snackbar.show();
    }

    private void restoreSingleHistory(final int position, final HistoryItem item) {
        String text = getActivity().getString(R.string.dialog_restore_removed);
        Snackbar snackbar = Snackbar.make(mBtnClear, text, Snackbar.LENGTH_LONG);
        snackbar.setAction(R.string.dialog_undo_btn, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mHistoryAdapter.restoreItem(position, item);
                FlickrLab.getInstance(getActivity()).addHistory(item, Boolean.TRUE);
                InfoUtils.showShack(mBtnClear, getString(R.string.dialog_restore_restored_single));
                checkHistory();
            }
        });
        snackbar.show();
    }

    private void closeHistoryDialog() {
        getDialog().dismiss();
    }

    public class SaveHistoryAsyncTask extends AsyncTask<Boolean, Void, Void> {

        private boolean mClearHistoryBase;

        @Override
        protected void onPreExecute() {
            mProgress.smoothToShow();
        }

        @Override
        protected Void doInBackground(Boolean... bool) {
            mClearHistoryBase = bool[FlickrConstants.INT_ZERO];
            if (mClearHistoryBase) {
                FlickrLab.getInstance(getActivity()).clearAllHistory();
                PrefUtils.setStoredQuery(getActivity(), null);
            } else
                FlickrLab.getInstance(getActivity()).restoreHistory(mSavedHistory);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (mClearHistoryBase) {
                mSavedHistory = mHistoryAdapter.getHistory();
                mHistoryAdapter.setHistory(new ArrayList<HistoryItem>());
                restoreHistory();
            } else {
                mHistoryAdapter.setHistory(mSavedHistory);
                InfoUtils.showShack(mBtnClear, getString(R.string.dialog_restore_restored));
            }
            mProgress.smoothToHide();
            checkHistory();
        }
    }

    private class OnHistorySwipeCallback extends ItemTouchHelper.SimpleCallback {

        private int mPosition;

        OnHistorySwipeCallback() {
            super(FlickrConstants.INT_ZERO, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        }

        @Override
        public boolean onMove(
                RecyclerView recyclerView,
                RecyclerView.ViewHolder viewHolder,
                RecyclerView.ViewHolder target) {
            return Boolean.FALSE;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            mPosition = viewHolder.getAdapterPosition();
            HistoryItem item = mHistoryAdapter.removeItem(mPosition);
            FlickrLab.getInstance(getActivity()).deleteHistory(item.getId());
            restoreSingleHistory(mPosition, item);
            checkHistory();
        }
    }
}