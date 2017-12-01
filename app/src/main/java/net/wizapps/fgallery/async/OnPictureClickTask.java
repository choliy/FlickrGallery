package net.wizapps.fgallery.async;

import android.graphics.Bitmap;
import android.os.AsyncTask;

import net.wizapps.fgallery.model.GalleryItem;
import net.wizapps.fgallery.tool.Events;
import net.wizapps.fgallery.tool.ImageSaver;
import net.wizapps.fgallery.util.FabUtils;

import org.greenrobot.eventbus.EventBus;

import java.io.ByteArrayOutputStream;

public class OnPictureClickTask extends AsyncTask<Void, Void, byte[]> {

    private final Bitmap mBitmap;
    private final GalleryItem mItem;

    public OnPictureClickTask(Bitmap bitmap, GalleryItem item) {
        mBitmap = bitmap;
        mItem = item;
    }

    @Override
    protected byte[] doInBackground(Void... params) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        mBitmap.compress(Bitmap.CompressFormat.JPEG, ImageSaver.QUALITY_MID, stream);
        return stream.toByteArray();
    }

    @Override
    protected void onPostExecute(byte[] bytes) {
        String picUrl = FabUtils.getPictureUrl(mItem, Boolean.TRUE);
        EventBus.getDefault().post(new Events.PictureClickEvent(picUrl, bytes));
    }
}