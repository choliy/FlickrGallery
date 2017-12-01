package net.wizapps.fgallery.util;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import net.wizapps.fgallery.R;
import net.wizapps.fgallery.async.PicDeleteTask;
import net.wizapps.fgallery.async.SaveHistoryTask;
import net.wizapps.fgallery.fragment.AboutFragment;
import net.wizapps.fgallery.fragment.HistoryFragment;
import net.wizapps.fgallery.fragment.SecretFragment;
import net.wizapps.fgallery.tool.Events;

import org.greenrobot.eventbus.EventBus;

public final class DialogUtils {

    private DialogUtils() {}

    public static void clearDialog(final Context context, final SaveHistoryTask asyncTask) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final View view = View.inflate(context, R.layout.dialog_clear, null);
        final View yes = view.findViewById(R.id.btn_yes);
        final View no = view.findViewById(R.id.btn_no);
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
        final View yes = view.findViewById(R.id.btn_yes);
        final View no = view.findViewById(R.id.btn_no);
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
        final View yes = view.findViewById(R.id.btn_yes);
        final View no = view.findViewById(R.id.btn_no);
        final AlertDialog dialog = builder.setView(view).show();

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventBus.getDefault().post(new Events.PrefRestoreEvent());
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