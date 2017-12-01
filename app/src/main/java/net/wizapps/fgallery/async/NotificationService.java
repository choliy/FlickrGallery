package net.wizapps.fgallery.async;

import android.app.Activity;
import android.app.IntentService;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;

import net.wizapps.fgallery.R;
import net.wizapps.fgallery.model.GalleryItem;
import net.wizapps.fgallery.tool.Constants;
import net.wizapps.fgallery.tool.FlickrFetch;
import net.wizapps.fgallery.util.AlarmUtils;
import net.wizapps.fgallery.util.InfoUtils;
import net.wizapps.fgallery.util.PrefUtils;

import java.util.List;

public class NotificationService extends IntentService {

    public NotificationService() {
        super(NotificationService.class.getSimpleName());
    }

    public static Intent newIntent(Context context) {
        return new Intent(context, NotificationService.class);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (!InfoUtils.isNetworkConnected(getApplicationContext())) return;

        String query = PrefUtils.getStoredQuery(this);
        if (query == null) return;

        List<GalleryItem> gallery = new FlickrFetch().downloadGallery(
                getApplicationContext(),
                Constants.ZERO);
        if (gallery.isEmpty()) return;

        String lastResultId = PrefUtils.getLastResultId(this);
        String resultId = gallery.get(Constants.ZERO).getUserId();
        if (!resultId.equals(lastResultId)) {
            Context context = getApplicationContext();
            String contentText = getString(R.string.text_new_pictures, query);
            showNotification(AlarmUtils.getNotification(context, contentText));
            PrefUtils.setLastResultId(this, resultId);
        }
    }

    private void showNotification(Notification notification) {
        Intent intent = new Intent(getString(R.string.broadcast_action_name));
        intent.putExtra(Constants.NOTIFICATION_KEY, notification);
        sendOrderedBroadcast(
                intent,
                getString(R.string.broadcast_permission),
                null,
                null,
                Activity.RESULT_OK,
                null,
                null);
    }
}