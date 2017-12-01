package com.choliy.igor.galleryforflickr.tool;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.choliy.igor.galleryforflickr.BuildConfig;
import com.choliy.igor.galleryforflickr.model.GalleryItem;
import com.choliy.igor.galleryforflickr.util.PrefUtils;

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
        if (searchText.equals(Constants.EMPTY)) {
            fetchMethod = Constants.METHOD_GET_RECENT;
        } else {
            fetchMethod = Constants.METHOD_SEARCH;
        }

        String url = Uri.parse("https://api.flickr.com/services/rest/")
                .buildUpon()
                .appendQueryParameter("method", fetchMethod)
                .appendQueryParameter("api_key", Constants.API_KEY)
                .appendQueryParameter("format", "json")
                .appendQueryParameter("nojsoncallback", "1")
                .appendQueryParameter("extras", "owner_name,date_taken,description,url_sq,url_s,url_n,url_z,url_l,url_o")
                .appendQueryParameter("per_page", PrefUtils.getPictureSettings(context))
                .appendQueryParameter("page", String.valueOf(pageNumber))
                .build().toString();

        Uri.Builder uriBuilder = Uri.parse(url).buildUpon();
        if (searchText.equals(Constants.EMPTY)) uriBuilder.appendQueryParameter("text", searchText);

        String finalUrl = uriBuilder.build().toString();
        if (BuildConfig.DEBUG) Log.i(TAG, "Request URL: " + finalUrl);

        return finalUrl;
    }

    private String getJsonString(String stringUrl) throws IOException {
        URL url = new URL(stringUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        try {
            InputStream inputStream = connection.getInputStream();
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new IOException(connection.getResponseMessage() + ": with " + stringUrl);
            }

            int bytesRead;
            byte[] buffer = new byte[BUFFER_SIZE];
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            while ((bytesRead = inputStream.read(buffer)) > Constants.ZERO) {
                outputStream.write(buffer, Constants.ZERO, bytesRead);
            }

            outputStream.close();
            byte[] jsonBytes = outputStream.toByteArray();
            return new String(jsonBytes);
        } finally {
            connection.disconnect();
        }
    }

    private void parseItems(List<GalleryItem> items, JSONObject jsonBody) throws JSONException {
        JSONObject baseJsonObject = jsonBody.getJSONObject(Constants.JSON_BASE);
        JSONArray photoJsonArray = baseJsonObject.getJSONArray(Constants.JSON_ARRAY);

        for (int i = Constants.ZERO; i < photoJsonArray.length(); i++) {
            JSONObject photoJsonObject = photoJsonArray.getJSONObject(i);

            // If there is no picture URL - iterate another object
            if (!photoJsonObject.has(Constants.JSON_LIST_PICTURE_URL)) continue;

            GalleryItem item = new GalleryItem();
            item.setUserId(photoJsonObject.getString(Constants.JSON_ID));
            item.setTitle(photoJsonObject.getString(Constants.JSON_TITLE));
            item.setDate(photoJsonObject.getString(Constants.JSON_DATE));
            item.setOwnerId(photoJsonObject.getString(Constants.JSON_OWNER_ID));
            item.setOwnerName(photoJsonObject.getString(Constants.JSON_OWNER_NAME));
            item.setDescription(photoJsonObject.getString(Constants.JSON_DESCRIPTION));
            item.setSmallListPicUrl(photoJsonObject.getString(Constants.JSON_SMALL_LIST_PICTURE_URL));
            item.setListPicUrl(photoJsonObject.getString(Constants.JSON_LIST_PICTURE_URL));

            // Extra small picture
            if (photoJsonObject.has(Constants.JSON_EXTRA_SMALL_PICTURE_URL)) {
                item.setExtraSmallPicUrl(photoJsonObject.getString(Constants.JSON_EXTRA_SMALL_PICTURE_URL));
            } else {
                item.setExtraSmallPicUrl(Constants.JSON_NO_SUCH_SIZE);
            }

            // Small picture
            if (photoJsonObject.has(Constants.JSON_SMALL_PICTURE_URL)) {
                item.setSmallPicUrl(photoJsonObject.getString(Constants.JSON_SMALL_PICTURE_URL));
            } else {
                item.setSmallPicUrl(Constants.JSON_NO_SUCH_SIZE);
            }

            // Medium picture
            if (photoJsonObject.has(Constants.JSON_MEDIUM_PICTURE_URL)) {
                item.setMediumPicUrl(photoJsonObject.getString(Constants.JSON_MEDIUM_PICTURE_URL));
            } else {
                item.setMediumPicUrl(Constants.JSON_NO_SUCH_SIZE);
            }

            // Big picture
            if (photoJsonObject.has(Constants.JSON_BIG_PICTURE_URL)) {
                item.setBigPicUrl(photoJsonObject.getString(Constants.JSON_BIG_PICTURE_URL));
            } else {
                item.setBigPicUrl(Constants.JSON_NO_SUCH_SIZE);
            }

            items.add(item);
        }
    }
}