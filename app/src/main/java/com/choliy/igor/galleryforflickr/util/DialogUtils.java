package com.choliy.igor.galleryforflickr.util;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.choliy.igor.galleryforflickr.R;
import com.choliy.igor.galleryforflickr.async.PicDeleteTask;
import com.choliy.igor.galleryforflickr.async.SaveHistoryTask;
import com.choliy.igor.galleryforflickr.event.PrefRestoreEvent;
import com.choliy.igor.galleryforflickr.fragment.AboutFragment;
import com.choliy.igor.galleryforflickr.fragment.HistoryFragment;
import com.choliy.igor.galleryforflickr.fragment.SecretFragment;

import org.greenrobot.eventbus.EventBus;

public final class DialogUtils {

    private DialogUtils() {}

    public static void clearDialog(final Context context, final SaveHistoryTask asyncTask) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final View view = View.inflate(context, R.layout.dialog_clear, null);
        final TextView yes = view.findViewById(R.id.btn_yes);
        final TextView no = view.findViewById(R.id.btn_no);
        final AlertDialog dialog = builder.setView(view).show();

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                asyncTask.execute(context);
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

    public static void deleteDialog(final Context context, final PicDeleteTask asyncTask) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final View view = View.inflate(context, R.layout.dialog_delete, null);
        final TextView yes = view.findViewById(R.id.btn_yes);
        final TextView no = view.findViewById(R.id.btn_no);
        final AlertDialog dialog = builder.setView(view).show();

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                asyncTask.execute(context);
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
        final TextView yes = view.findViewById(R.id.btn_yes);
        final TextView no = view.findViewById(R.id.btn_no);
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

    public static void secretDialog(Context context) {
        FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
        new SecretFragment().show(fragmentManager, SecretFragment.TAG);
    }

    static void historyDialog(Context context) {
        FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
        new HistoryFragment().show(fragmentManager, HistoryFragment.TAG);
    }

    static void aboutDialog(Context context) {
        FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
        new AboutFragment().show(fragmentManager, AboutFragment.TAG);
    }
}