package com.choliy.igor.flickrgallery.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.choliy.igor.flickrgallery.R;
import com.choliy.igor.flickrgallery.event.PrefRestoreEvent;
import com.choliy.igor.flickrgallery.fragment.HistoryFragment;
import com.choliy.igor.flickrgallery.fragment.SavedFragment;

import org.greenrobot.eventbus.EventBus;

public final class InfoUtils {

    private InfoUtils() {
    }

    public static void showShortShack(View view, String infoText) {
        Snackbar.make(view, infoText, Snackbar.LENGTH_SHORT).show();
    }

    public static void showShortToast(Context context, String infoText) {
        Toast.makeText(context, infoText, Toast.LENGTH_SHORT).show();
    }

    public static void showLongToast(Context context, String infoText) {
        Toast.makeText(context, infoText, Toast.LENGTH_LONG).show();
    }

    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        boolean isNetworkAvailable = cm.getActiveNetworkInfo() != null;
        return isNetworkAvailable && cm.getActiveNetworkInfo().isConnected();
    }

    public static void clearDialog(Context context, final HistoryFragment.SaveHistoryAsyncTask asyncTask) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final View view = View.inflate(context, R.layout.dialog_clear, null);
        final TextView yes = (TextView) view.findViewById(R.id.btn_clear_yes);
        final TextView no = (TextView) view.findViewById(R.id.btn_clear_no);
        final AlertDialog dialog = builder.setView(view).show();

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                asyncTask.execute(Boolean.TRUE);
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

    public static void deleteDialog(Context context, final SavedFragment.DeletePicTask picTask) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final View view = View.inflate(context, R.layout.dialog_delete, null);
        final TextView yes = (TextView) view.findViewById(R.id.btn_delete_yes);
        final TextView no = (TextView) view.findViewById(R.id.btn_delete_no);
        final AlertDialog dialog = builder.setView(view).show();

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                picTask.execute();
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

    public static void restoreDialog(Context context) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final View view = View.inflate(context, R.layout.dialog_restore, null);
        final TextView yes = (TextView) view.findViewById(R.id.btn_restore_yes);
        final TextView no = (TextView) view.findViewById(R.id.btn_restore_no);
        final AlertDialog dialog = builder.setView(view).show();

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventBus.getDefault().post(new PrefRestoreEvent(Boolean.TRUE));
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
}