package net.wizapps.fgallery.async;

import android.content.Context;
import android.os.AsyncTask;

import net.wizapps.fgallery.data.FlickrLab;
import net.wizapps.fgallery.tool.Events;

import org.greenrobot.eventbus.EventBus;

public class PicDeleteTask extends AsyncTask<Context, Void, Void> {

    @Override
    protected void onPreExecute() {
        EventBus.getDefault().post(new Events.DeleteStartEvent());
    }

    @Override
    protected Void doInBackground(Context... contexts) {
        FlickrLab.getInstance(contexts[0]).deleteAllPictures();
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        EventBus.getDefault().post(new Events.DeleteFinishEvent());
    }
}