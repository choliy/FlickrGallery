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

import com.choliy.igor.flickrgallery.FlickrConstants;
import com.choliy.igor.flickrgallery.R;
import com.choliy.igor.flickrgallery.activity.SettingsActivity;
import com.choliy.igor.flickrgallery.fragment.HistoryFragment;

import java.util.Calendar;

public final class NavUtils {

    public static boolean sIsHistoryDialogShown;
    public static boolean sIsAboutDialogShown;

    public static void showHistory(Context context) {
        sIsHistoryDialogShown = Boolean.TRUE;
        FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
        new HistoryFragment().show(fragmentManager, context.getString(R.string.dialog_history_tag));
    }

    public static void startSettings(Activity activity) {
        Intent intent = new Intent(activity.getApplicationContext(), SettingsActivity.class);
        activity.startActivityForResult(intent, FlickrConstants.REQUEST_CODE);
    }

    public static void aboutDialog(final Context context) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final View view = View.inflate(context, R.layout.dialog_info, null);
        final TextView developer = (TextView) view.findViewById(R.id.developer_url);
        final TextView email = (TextView) view.findViewById(R.id.btn_dialog_email);
        final TextView feedback = (TextView) view.findViewById(R.id.btn_dialog_feedback);
        final TextView close = (TextView) view.findViewById(R.id.btn_about_close);

        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                sIsAboutDialogShown = Boolean.FALSE;
            }
        });

        sIsAboutDialogShown = Boolean.TRUE;
        builder.setView(view);
        final AlertDialog dialog = builder.show();

        int year = Calendar.getInstance().get(Calendar.YEAR);
        String text = context.getString(R.string.dialog_developer, String.valueOf(year));
        developer.setText(text);

        developer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String developerUrl = context.getString(R.string.dialog_url);
                Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(developerUrl));
                checkIntentBeforeLaunching(context, webIntent);
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
    }

    public static void shareIntent(Activity activity) {
        ShareCompat.IntentBuilder
                .from(activity)
                .setType("text/plain")
                .setChooserTitle(activity.getString(R.string.app_name))
                .setText(activity.getString(R.string.app_url))
                .startChooser();
    }

    public static void emailIntent(Context context) {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse("mailto:" + context.getString(R.string.text_email)));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.text_subject));
        emailIntent.putExtra(Intent.EXTRA_TEXT, context.getString(R.string.text_hello));
        checkIntentBeforeLaunching(context, emailIntent);
    }

    public static void feedbackIntent(Context context) {
        String url = context.getString(R.string.app_url);
        Intent feedbackIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        checkIntentBeforeLaunching(context, feedbackIntent);
    }

    public static void appsIntent(Context context) {
        String url = context.getString(R.string.apps_url);
        Intent appsIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        checkIntentBeforeLaunching(context, appsIntent);
    }

    private static void checkIntentBeforeLaunching(Context context, Intent intent) {
        if (intent.resolveActivity(context.getPackageManager()) != null)
            context.startActivity(intent);
    }
}