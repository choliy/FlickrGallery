package com.choliy.igor.flickrgallery;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.choliy.igor.flickrgallery.model.GalleryItem;
import com.choliy.igor.flickrgallery.tool.FlickrFetch;
import com.choliy.igor.flickrgallery.util.AlarmUtils;
import com.choliy.igor.flickrgallery.util.FlickrUtils;
import com.choliy.igor.flickrgallery.util.PrefUtils;

import java.util.List;

public class NotificationService extends IntentService {

    private static final String TAG = NotificationService.class.getSimpleName();

    public NotificationService() {
        super(TAG);
    }

    public static Intent newIntent(Context context) {
        return new Intent(context, NotificationService.class);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (!FlickrUtils.isNetworkConnected(getApplicationContext())) return;

        String query = PrefUtils.getStoredQuery(this);
        if (query.isEmpty()) return;

        String lastResultId = PrefUtils.getLastResultId(this);
        List<GalleryItem> gallery = new FlickrFetch()
                .downloadGallery(getApplicationContext(), query, FlickrConstants.NUMBER_ZERO);
        if (gallery.isEmpty()) return;

        String resultId = gallery.get(0).getId();
        if (!resultId.equals(lastResultId)) {
            String contentText = getString(R.string.text_new_pictures, query);
            AlarmUtils.showNotification(getApplicationContext(), contentText);
            PrefUtils.setLastResultId(this, resultId);
        }
    }
}