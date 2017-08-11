package com.choliy.igor.flickrgallery.tool;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.choliy.igor.flickrgallery.FlickrConstants;
import com.choliy.igor.flickrgallery.model.GalleryItem;
import com.choliy.igor.flickrgallery.util.PrefUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class FlickrFetch {

    private static final String TAG = FlickrFetch.class.getSimpleName();
    private static final int BUFFER_SIZE = 1024;

    public List<GalleryItem> downloadGallery(Context context, int pageNumber) {
        List<GalleryItem> items = new ArrayList<>();
        try {
            String url = getUrl(context, pageNumber);
            String jsonString = getJsonString(url);
            JSONObject jsonBody = new JSONObject(jsonString);
            parseItems(items, jsonBody);
        } catch (JSONException je) {
            Log.e(TAG, "Failed to parse JSON", je);
        } catch (IOException ioe) {
            Log.e(TAG, "Failed to fetch items", ioe);
        }

        return items;
    }

    private String getUrl(Context context, int pageNumber) {

        // Flickr API:
        // https://www.flickr.com/services/api/flickr.photos.getRecent.html
        String fetchMethod;
        String searchText = PrefUtils.getStoredQuery(context);
        if (searchText == null) fetchMethod = FlickrConstants.METHOD_GET_RECENT;
        else fetchMethod = FlickrConstants.METHOD_SEARCH;

        String url = Uri.parse("https://api.flickr.com/services/rest/")
                .buildUpon()
                .appendQueryParameter("method", fetchMethod)
                .appendQueryParameter("api_key", FlickrConstants.API_KEY)
                .appendQueryParameter("format", "json")
                .appendQueryParameter("nojsoncallback", "1")
                .appendQueryParameter("extras",
                        "owner_name,date_taken,description,url_sq,url_s,url_n,url_z,url_l,url_o")
                .appendQueryParameter("per_page", PrefUtils.getPictureSettings(context))
                .appendQueryParameter("page", String.valueOf(pageNumber))
                .build().toString();

        Uri.Builder uriBuilder = Uri.parse(url).buildUpon();
        if (searchText != null) uriBuilder.appendQueryParameter("text", searchText);

        String finalUrl = uriBuilder.build().toString();
        Log.i(TAG, "Request URL: " + finalUrl);

        return finalUrl;
    }

    private String getJsonString(String stringUrl) throws IOException {
        URL url = new URL(stringUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        try {
            InputStream inputStream = connection.getInputStream();
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK)
                throw new IOException(connection.getResponseMessage() + ": with " + stringUrl);

            int bytesRead;
            byte[] buffer = new byte[BUFFER_SIZE];
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            while ((bytesRead = inputStream.read(buffer)) > FlickrConstants.INT_ZERO) {
                outputStream.write(buffer, FlickrConstants.INT_ZERO, bytesRead);
            }

            outputStream.close();
            byte[] jsonBytes = outputStream.toByteArray();
            return new String(jsonBytes);
        } finally {
            connection.disconnect();
        }
    }

    private void parseItems(List<GalleryItem> items, JSONObject jsonBody)
            throws IOException, JSONException {

        JSONObject baseJsonObject = jsonBody.getJSONObject(FlickrConstants.JSON_BASE);
        JSONArray photoJsonArray = baseJsonObject.getJSONArray(FlickrConstants.JSON_ARRAY);

        for (int i = FlickrConstants.INT_ZERO; i < photoJsonArray.length(); i++) {
            JSONObject photoJsonObject = photoJsonArray.getJSONObject(i);

            // If there is no picture URL - iterate another object
            if (!photoJsonObject.has(FlickrConstants.JSON_LIST_PICTURE_URL)) continue;

            GalleryItem item = new GalleryItem();
            item.setUserId(photoJsonObject.getString(FlickrConstants.JSON_ID));
            item.setTitle(photoJsonObject.getString(FlickrConstants.JSON_TITLE));
            item.setDate(photoJsonObject.getString(FlickrConstants.JSON_DATE));
            item.setOwnerId(photoJsonObject.getString(FlickrConstants.JSON_OWNER_ID));
            item.setOwnerName(photoJsonObject.getString(FlickrConstants.JSON_OWNER_NAME));
            item.setDescription(photoJsonObject.getString(FlickrConstants.JSON_DESCRIPTION));
            item.setSmallListPicUrl(photoJsonObject.getString(FlickrConstants.JSON_SMALL_LIST_PICTURE_URL));
            item.setListPicUrl(photoJsonObject.getString(FlickrConstants.JSON_LIST_PICTURE_URL));

            // Extra small picture
            if (photoJsonObject.has(FlickrConstants.JSON_EXTRA_SMALL_PICTURE_URL))
                item.setExtraSmallPicUrl(photoJsonObject.getString(FlickrConstants.JSON_EXTRA_SMALL_PICTURE_URL));
            else
                item.setExtraSmallPicUrl(FlickrConstants.JSON_NO_SUCH_SIZE);

            // Small picture
            if (photoJsonObject.has(FlickrConstants.JSON_SMALL_PICTURE_URL))
                item.setSmallPicUrl(photoJsonObject.getString(FlickrConstants.JSON_SMALL_PICTURE_URL));
            else
                item.setSmallPicUrl(FlickrConstants.JSON_NO_SUCH_SIZE);

            // Medium picture
            if (photoJsonObject.has(FlickrConstants.JSON_MEDIUM_PICTURE_URL))
                item.setMediumPicUrl(photoJsonObject.getString(FlickrConstants.JSON_MEDIUM_PICTURE_URL));
            else
                item.setMediumPicUrl(FlickrConstants.JSON_NO_SUCH_SIZE);

            // Big picture
            if (photoJsonObject.has(FlickrConstants.JSON_BIG_PICTURE_URL))
                item.setBigPicUrl(photoJsonObject.getString(FlickrConstants.JSON_BIG_PICTURE_URL));
            else
                item.setBigPicUrl(FlickrConstants.JSON_NO_SUCH_SIZE);

            items.add(item);
        }
    }
}