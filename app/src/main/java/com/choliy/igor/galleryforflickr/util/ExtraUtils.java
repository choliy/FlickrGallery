package com.choliy.igor.galleryforflickr.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.choliy.igor.galleryforflickr.R;
import com.choliy.igor.galleryforflickr.tool.Constants;

import java.util.Calendar;

public final class ExtraUtils {

    private ExtraUtils() {}

    public static void hideKeyboard(Context context) {
        View view = ((AppCompatActivity) context).getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) imm.hideSoftInputFromWindow(view.getWindowToken(), Constants.ZERO);
            view.clearFocus();
        }
    }

    public static void setupDevDate(Context context, NavigationView view) {
        View header = view.getHeaderView(Constants.ZERO);
        TextView developer = header.findViewById(R.id.nav_text_developer);
        int year = Calendar.getInstance().get(Calendar.YEAR);
        String text = context.getString(R.string.dialog_developer, String.valueOf(year));
        developer.setText(text);
    }

    public static String parseDescription(Context context, String description) {
        String defaultDescription = context.getString(R.string.text_def_description);
        String emptyDescription = context.getString(R.string.text_empty_description);
        if (description.equals(defaultDescription)) {
            return emptyDescription;
        } else {
            description = description.replace(
                    context.getString(R.string.text_replace_chars),
                    Constants.EMPTY);

            description = description.substring(
                    Constants.ZERO,
                    description.length() - Constants.TWO);

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                description = Html.fromHtml(description, Html.FROM_HTML_MODE_LEGACY).toString();
            } else {
                description = Html.fromHtml(description).toString();
            }

            description = description.replaceAll("\\\\n", Constants.EMPTY);
            description = description.replaceAll("\\\\/", Constants.EMPTY);

            return description;
        }
    }

    public static void loadPicture(
            Context context,
            String url,
            ImageView image,
            RequestListener<String, Bitmap> listener) {

        if (PrefUtils.getAnimationSettings(context)) {
            Glide.with(context)
                    .load(url)
                    .asBitmap()
                    .error(R.mipmap.ic_error)
                    .listener(listener)
                    .animate(R.anim.anim_scale_picture)
                    .into(image);
        } else {
            Glide.with(context)
                    .load(url)
                    .asBitmap()
                    .error(R.mipmap.ic_error)
                    .listener(listener)
                    .into(image);
        }
    }
}