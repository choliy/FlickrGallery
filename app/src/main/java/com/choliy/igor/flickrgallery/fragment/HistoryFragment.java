package com.choliy.igor.flickrgallery.fragment;

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.choliy.igor.flickrgallery.FlickrConstants;
import com.choliy.igor.flickrgallery.R;
import com.choliy.igor.flickrgallery.adapter.HistoryAdapter;
import com.choliy.igor.flickrgallery.data.FlickrLab;
import com.choliy.igor.flickrgallery.model.HistoryItem;
import com.choliy.igor.flickrgallery.async.HistoryLoader;
import com.choliy.igor.flickrgallery.util.FlickrUtils;
import com.choliy.igor.flickrgallery.util.NavUtils;
import com.choliy.igor.flickrgallery.util.PrefUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HistoryFragment extends DialogFragment implements
        HistoryAdapter.OnHistoryClickListener,
        LoaderManager.LoaderCallbacks<List<HistoryItem>> {

    private HistoryAdapter mHistoryAdapter;
    private List<HistoryItem> mSavedHistory;
    private OnHistoryDialogClickListener mStartClickListener;

    @BindView(R.id.rv_history) RecyclerView mRvHistory;
    @BindView(R.id.btn_history_clear) TextView mBtnClear;
    @BindView(R.id.layout_no_history) LinearLayout mNoHistory;

    public interface OnHistoryDialogClickListener {

        void onStartClick();

        void onHistoryClick(String historyTitle);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mStartClickListener = (OnHistoryDialogClickListener) context;
        } catch (ClassCastException e) {
            Log.e(HistoryFragment.class.getSimpleName(), e.getMessage());
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(Boolean.TRUE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_history, container, Boolean.FALSE);
        ButterKnife.bind(this, view);
        getActivity()
                .getSupportLoaderManager()
                .restartLoader(HistoryLoader.HISTORY_LOADER_ID, null, this);
        return view;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new Dialog(getActivity(), getTheme()) {
            @Override
            public void onBackPressed() {
                closeHistoryDialog();
            }
        };
    }

    @Override
    public Loader<List<HistoryItem>> onCreateLoader(int id, Bundle args) {
        return new HistoryLoader(getActivity());
    }

    @Override
    public void onLoadFinished(Loader<List<HistoryItem>> loader, List<HistoryItem> historyItems) {
        setupUi(historyItems);
    }

    @Override
    public void onLoaderReset(Loader<List<HistoryItem>> loader) {
    }

    @Override
    public void onHistoryClick(String historyTitle) {
        closeHistoryDialog();
        mStartClickListener.onHistoryClick(historyTitle);
    }

    @OnClick({R.id.btn_history_start, R.id.btn_history_clear, R.id.btn_history_close})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_history_start:
                closeHistoryDialog();
                mStartClickListener.onStartClick();
                break;
            case R.id.btn_history_clear:
                clearDialog();
                break;
            case R.id.btn_history_close:
                closeHistoryDialog();
                break;
        }
    }

    private void setupUi(List<HistoryItem> historyItems) {
        mHistoryAdapter = new HistoryAdapter(getActivity(), historyItems, this);
        mRvHistory.setAdapter(mHistoryAdapter);
        mRvHistory.setHasFixedSize(Boolean.TRUE);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());

        // set current property to false for avoid listItem width issue
        layoutManager.setAutoMeasureEnabled(Boolean.FALSE);
        mRvHistory.setLayoutManager(layoutManager);
        ItemTouchHelper touchHelper = new ItemTouchHelper(new OnHistorySwipeCallback());
        touchHelper.attachToRecyclerView(mRvHistory);
        checkHistory();
    }

    private void checkHistory() {
        if (mHistoryAdapter.getItemCount() > 0) {
            mNoHistory.setVisibility(View.GONE);
            mBtnClear.setVisibility(View.VISIBLE);
        } else {
            mNoHistory.setVisibility(View.VISIBLE);
            mBtnClear.setVisibility(View.INVISIBLE);
        }
    }

    private void clearDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final View view = View.inflate(getActivity(), R.layout.dialog_clear, null);
        final TextView yes = (TextView) view.findViewById(R.id.btn_clear_yes);
        final TextView no = (TextView) view.findViewById(R.id.btn_clear_no);
        final AlertDialog dialog = builder.setView(view).show();

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SaveHistoryAsyncTask().execute(Boolean.TRUE);
                dialog.dismiss();
            }
        });

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
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
                FlickrUtils.showInfo(mBtnClear, getString(R.string.dialog_restore_restored_single));
                checkHistory();
            }
        });
        snackbar.show();
    }

    private void closeHistoryDialog() {
        NavUtils.sIsHistoryDialogShown = Boolean.FALSE;
        getDialog().dismiss();
    }

    private class SaveHistoryAsyncTask extends AsyncTask<Boolean, Void, Void> {

        private boolean mClearHistoryBase;

        @Override
        protected Void doInBackground(Boolean... bool) {
            mClearHistoryBase = bool[FlickrConstants.INT_ZERO];
            if (mClearHistoryBase) {
                FlickrLab.getInstance(getActivity()).deleteAllHistory();
                PrefUtils.setStoredQuery(getActivity(), null);
            } else {
                FlickrLab.getInstance(getActivity()).restoreHistory(mSavedHistory);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (mClearHistoryBase) {
                mSavedHistory = mHistoryAdapter.getHistory();
                mHistoryAdapter.updateHistory(new ArrayList<HistoryItem>());
                restoreHistory();
            } else {
                mHistoryAdapter.updateHistory(mSavedHistory);
                FlickrUtils.showInfo(mBtnClear, getString(R.string.dialog_restore_restored));
            }
            checkHistory();
        }
    }

    private class OnHistorySwipeCallback extends ItemTouchHelper.SimpleCallback {

        private int mRemovedPosition;
        private HistoryItem mSingleHistory;

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
            String id = (String) viewHolder.itemView.getTag();
            for (HistoryItem historyItem : mHistoryAdapter.getHistory()) {
                if (historyItem.getId().equals(id)) mSingleHistory = historyItem;
            }
            mRemovedPosition = mHistoryAdapter.removeItem(mSingleHistory);
            FlickrLab.getInstance(getActivity()).deleteSingleHistory(id);
            checkHistory();
            restoreSingleHistory(mRemovedPosition, mSingleHistory);
        }
    }
}