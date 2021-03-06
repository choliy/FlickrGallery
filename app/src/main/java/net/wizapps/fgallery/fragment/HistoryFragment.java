package net.wizapps.fgallery.fragment;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;
import android.view.View;

import com.wang.avi.AVLoadingIndicatorView;

import net.wizapps.fgallery.R;
import net.wizapps.fgallery.adapter.HistoryAdapter;
import net.wizapps.fgallery.async.SaveHistoryTask;
import net.wizapps.fgallery.base.BaseDialog;
import net.wizapps.fgallery.data.FlickrLab;
import net.wizapps.fgallery.loader.HistoryLoader;
import net.wizapps.fgallery.model.HistoryItem;
import net.wizapps.fgallery.tool.Constants;
import net.wizapps.fgallery.tool.Events;
import net.wizapps.fgallery.util.DialogUtils;
import net.wizapps.fgallery.util.InfoUtils;
import net.wizapps.fgallery.view.SwipeCallback;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class HistoryFragment extends BaseDialog implements
        LoaderManager.LoaderCallbacks<List<HistoryItem>> {

    @BindView(R.id.progress_view) AVLoadingIndicatorView mProgress;
    @BindView(R.id.rv_history) RecyclerView mRvHistory;
    @BindView(R.id.btn_history_clear) View mBtnClear;
    @BindView(R.id.layout_no_history) View mNoHistory;

    public static final String TAG = HistoryFragment.class.getSimpleName();
    private List<HistoryItem> mSavedHistory;
    private HistoryAdapter mHistoryAdapter;

    @Override
    protected int layoutRes() {
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
    public void onLoaderReset(Loader<List<HistoryItem>> loader) {}

    @Subscribe
    public void onEvent(Events.HistoryTitleEvent event) {
        if (!TextUtils.isEmpty(event.getHistoryTitle())) dismiss();
    }

    @Subscribe
    public void onEvent(Events.SwipePositionEvent event) {
        HistoryItem item = mHistoryAdapter.removeItem(event.getPosition());
        FlickrLab.getInstance(getActivity()).deleteHistory(item.getId());
        restoreSingleHistory(event.getPosition(), item);
        checkHistory();
    }

    @Subscribe
    public void onEvent(Events.SaveStartEvent event) {
        mProgress.smoothToShow();
    }

    @Subscribe
    public void onEvent(Events.SaveFinishEvent event) {
        if (event.isClearHistoryBase()) {
            mSavedHistory = mHistoryAdapter.getHistory();
            mHistoryAdapter.setHistory(new ArrayList<HistoryItem>());
            restoreHistory();
        } else {
            mHistoryAdapter.setHistory(mSavedHistory);
            InfoUtils.showShack(view(), getString(R.string.dialog_restore_restored));
        }
        mProgress.smoothToHide();
        checkHistory();
    }

    @OnClick({R.id.btn_history_start, R.id.btn_history_clear, R.id.btn_history_close})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_history_start:
                EventBus.getDefault().post(new Events.HistoryStartEvent());
                dismiss();
                break;
            case R.id.btn_history_clear:
                DialogUtils.clearDialog(getActivity(), new SaveHistoryTask(Boolean.TRUE, mSavedHistory));
                break;
            case R.id.btn_history_close:
                dismiss();
                break;
        }
    }

    private void setupUi() {
        mHistoryAdapter = new HistoryAdapter();
        mRvHistory.setAdapter(mHistoryAdapter);
        mRvHistory.setHasFixedSize(Boolean.TRUE);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());

        // set current property to false for avoid listItem width issue
        layoutManager.setAutoMeasureEnabled(Boolean.FALSE);
        mRvHistory.setLayoutManager(layoutManager);
        ItemTouchHelper touchHelper = new ItemTouchHelper(new SwipeCallback());
        touchHelper.attachToRecyclerView(mRvHistory);
    }

    private void checkHistory() {
        if (mHistoryAdapter.getItemCount() > Constants.ZERO) {
            mNoHistory.setVisibility(View.GONE);
            mBtnClear.setVisibility(View.VISIBLE);
        } else {
            mNoHistory.setVisibility(View.VISIBLE);
            mBtnClear.setVisibility(View.INVISIBLE);
        }
    }

    private void restoreHistory() {
        String text = getActivity().getString(R.string.dialog_restore_cleaned);
        Snackbar snackbar = Snackbar.make(view(), text, Snackbar.LENGTH_LONG);
        snackbar.setAction(R.string.dialog_undo_btn, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SaveHistoryTask(Boolean.FALSE, mSavedHistory).execute(getActivity());
            }
        });
        snackbar.show();
    }

    private void restoreSingleHistory(final int position, final HistoryItem item) {
        String text = getActivity().getString(R.string.dialog_restore_removed);
        Snackbar snackbar = Snackbar.make(view(), text, Snackbar.LENGTH_LONG);
        snackbar.setAction(R.string.dialog_undo_btn, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mHistoryAdapter.restoreItem(position, item);
                FlickrLab.getInstance(getActivity()).addHistory(item, Boolean.TRUE);
                InfoUtils.showShack(view(), getString(R.string.dialog_restore_restored_single));
                checkHistory();
            }
        });
        snackbar.show();
    }

    private View view() {
        return getView();
    }
}