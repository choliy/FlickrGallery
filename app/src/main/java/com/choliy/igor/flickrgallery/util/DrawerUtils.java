package com.choliy.igor.flickrgallery.util;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.TextView;

import com.choliy.igor.flickrgallery.R;

import java.util.Calendar;

public final class DrawerUtils {

    public static boolean sIsAboutDialogShown;

    public static void aboutDialog(final Context context) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final View view = View.inflate(context, R.layout.dialog_info, null);
        final TextView developer = (TextView) view.findViewById(R.id.developer_url);

        int year = Calendar.getInstance().get(Calendar.YEAR);
        String text = context.getString(R.string.dialog_developer, String.valueOf(year));
        developer.setText(text);

        developer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String developerUrl = context.getString(R.string.dialog_url);
                Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(developerUrl));
                checkIntentBeforeLaunching(context, webIntent);
            }
        });

        builder.setNegativeButton(R.string.dialog_close_btn, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });

        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                sIsAboutDialogShown = false;
            }
        });

        sIsAboutDialogShown = true;
        builder.setView(view);
        builder.show();
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
        Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        checkIntentBeforeLaunching(context, webIntent);
    }

    private static void checkIntentBeforeLaunching(Context context, Intent intent) {
        if (intent.resolveActivity(context.getPackageManager()) != null)
            context.startActivity(intent);
    }
}