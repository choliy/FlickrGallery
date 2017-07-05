package com.choliy.igor.flickrgallery;

import android.net.Uri;
import android.util.Log;

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

    public List<GalleryItem> downloadGallery(String searchText, int pageNumber) {
        List<GalleryItem> items = new ArrayList<>();
        try {
            String url = buildUrl(searchText, pageNumber);
            String jsonString = getJsonString(url);
            JSONObject jsonBody = new JSONObject(jsonString);
            parseItems(items, jsonBody);
            //Log.i(TAG, "Response JSON: " + jsonString);
        } catch (JSONException je) {
            Log.e(TAG, "Failed to parse JSON", je);
        } catch (IOException ioe) {
            Log.e(TAG, "Failed to fetch items", ioe);
        }

        return items;
    }

    private String buildUrl(String searchText, int pageNumber) {
        String stringUrl = getUrl(searchText, pageNumber);
        Uri.Builder uriBuilder = Uri.parse(stringUrl).buildUpon();
        if (searchText != null) uriBuilder.appendQueryParameter("text", searchText);
        return uriBuilder.build().toString();
    }

    private String getUrl(String searchText, int pageNumber) {

        // Flickr API:
        // https://www.flickr.com/services/api/flickr.photos.getRecent.html
        String fetchMethod;
        if (searchText == null) fetchMethod = FlickrConstants.METHOD_GET_RECENT;
        else fetchMethod = FlickrConstants.METHOD_SEARCH;

        String url = Uri.parse("https://api.flickr.com/services/rest/")
                .buildUpon()
                .appendQueryParameter("method", fetchMethod)
                .appendQueryParameter("api_key", FlickrConstants.API_KEY)
                .appendQueryParameter("format", "json")
                .appendQueryParameter("nojsoncallback", "1")
                .appendQueryParameter("extras", "date_upload,owner_name,url_s")
                .appendQueryParameter("page", String.valueOf(pageNumber))
                .build().toString();

        Log.i(TAG, "Request URL: " + url);
        return url;
    }

    private String getJsonString(String stringUrl) throws IOException {
        URL url = new URL(stringUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        try {
            InputStream inputStream = connection.getInputStream();
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK)
                throw new IOException(connection.getResponseMessage() + ": with " + stringUrl);

            int bytesRead;
            byte[] buffer = new byte[1024];
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            while ((bytesRead = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, bytesRead);
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

        for (int i = 0; i < photoJsonArray.length(); i++) {
            JSONObject photoJsonObject = photoJsonArray.getJSONObject(i);

            // If there is no picture URL - iterate another object
            if (!photoJsonObject.has(FlickrConstants.JSON_PICTURE_URL)) continue;

            GalleryItem item = new GalleryItem();
            item.setId(photoJsonObject.getString(FlickrConstants.JSON_ID));
            item.setTitle(photoJsonObject.getString(FlickrConstants.JSON_TITLE));
            item.setUploadDate(photoJsonObject.getString(FlickrConstants.JSON_UPLOAD_DATE));
            item.setOwnerName(photoJsonObject.getString(FlickrConstants.JSON_OWNER_NAME));
            item.setPictureUrl(photoJsonObject.getString(FlickrConstants.JSON_PICTURE_URL));

            items.add(item);
        }
    }
}