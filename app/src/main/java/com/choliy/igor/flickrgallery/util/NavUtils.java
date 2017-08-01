package com.choliy.igor.flickrgallery.util;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.choliy.igor.flickrgallery.R;
import com.choliy.igor.flickrgallery.activity.SavedActivity;
import com.choliy.igor.flickrgallery.activity.SettingsActivity;
import com.choliy.igor.flickrgallery.fragment.HistoryFragment;

import java.util.Calendar;

public final class NavUtils {

    public static final int REQUEST_CODE = 777;
    public static boolean sIsHistoryDialogShown;
    public static boolean sIsAboutDialogShown;

    private NavUtils() {
    }

    public static void onNavDrawerClicked(Context context, int id) {
        switch (id) {
            case R.id.nav_saved:
                showSavedPictures(context);
                break;
            case R.id.nav_history:
                showHistory(context);
                break;
            case R.id.nav_settings:
                startSettings((AppCompatActivity) context);
                break;
            case R.id.nav_about:
                aboutDialog(context);
                break;
            case R.id.nav_share:
                shareIntent((AppCompatActivity) context);
                break;
            case R.id.nav_email:
                emailIntent(context);
                break;
            case R.id.nav_feedback:
                feedbackIntent(context);
                break;
            case R.id.nav_apps:
                appsIntent(context);
                break;
        }
    }

    public static void showHistory(Context context) {
        FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
        new HistoryFragment().show(fragmentManager, context.getString(R.string.dialog_history_tag));
        sIsHistoryDialogShown = Boolean.TRUE;
    }

    public static void aboutDialog(final Context context) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final View view = View.inflate(context, R.layout.dialog_info, null);
        final TextView developer = (TextView) view.findViewById(R.id.developer_url);
        final TextView email = (TextView) view.findViewById(R.id.btn_dialog_email);
        final TextView feedback = (TextView) view.findViewById(R.id.btn_dialog_feedback);
        final TextView close = (TextView) view.findViewById(R.id.btn_about_close);
        final TextView version = (TextView) view.findViewById(R.id.view_version);
        final AlertDialog dialog = builder.setView(view).show();
        sIsAboutDialogShown = Boolean.TRUE;

        int year = Calendar.getInstance().get(Calendar.YEAR);
        String text = context.getString(R.string.dialog_developer, String.valueOf(year));
        developer.setText(text);

        developer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String developerUrl = context.getString(R.string.dialog_url);
                Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(developerUrl));
                checkBeforeLaunching(context, webIntent);
                dialog.dismiss();
            }
        });

        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                emailIntent(context);
                dialog.dismiss();
            }
        });

        feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                feedbackIntent(context);
                dialog.dismiss();
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        version.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                DialogUtils.secretDialog(context);
                dialog.dismiss();
                return Boolean.TRUE;
            }
        });

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                sIsAboutDialogShown = Boolean.FALSE;
            }
        });
    }

    private static void showSavedPictures(Context context) {
        context.startActivity(new Intent(context, SavedActivity.class));
    }

    private static void startSettings(Activity activity) {
        Intent intent = new Intent(activity.getApplicationContext(), SettingsActivity.class);
        activity.startActivityForResult(intent, REQUEST_CODE);
    }

    private static void shareIntent(Activity activity) {
        ShareCompat.IntentBuilder
                .from(activity)
                .setType("text/plain")
                .setChooserTitle(activity.getString(R.string.app_name))
                .setText(activity.getString(R.string.app_url))
                .startChooser();
    }

    private static void emailIntent(Context context) {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse("mailto:" + context.getString(R.string.text_email)));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.text_subject));
        emailIntent.putExtra(Intent.EXTRA_TEXT, context.getString(R.string.text_hello));
        checkBeforeLaunching(context, emailIntent);
    }

    private static void feedbackIntent(Context context) {
        String url = context.getString(R.string.app_url);
        Intent feedbackIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        checkBeforeLaunching(context, feedbackIntent);
    }

    private static void appsIntent(Context context) {
        String url = context.getString(R.string.apps_url);
        Intent appsIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        checkBeforeLaunching(context, appsIntent);
    }

    static void checkBeforeLaunching(Context context, Intent intent) {
        if (intent.resolveActivity(context.getPackageManager()) != null)
            context.startActivity(intent);
        else
            Toast.makeText(context, context.getString(R.string.text_no_browser), Toast.LENGTH_SHORT).show();
    }
}