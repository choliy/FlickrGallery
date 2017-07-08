package com.choliy.igor.flickrgallery;

public interface FlickrConstants {

    // Keys
    String TOOLBAR_TYPE = "toolbar_type";
    String PREF_SEARCH_QUERY = "pref_search_query";
    String GRID_KEY = "grid_value_key";
    String PICTURE_KEY = "picture_value_key";
    String ANIMATION_KEY = "animation_value_key";
    int REQUEST_CODE = 777;

    // Empty values
    String STRING_EMPTY = "";
    int NUMBER_ZERO = 0;

    // Default values
    int DEFAULT_LIST_POSITION = 0;
    int DEFAULT_PAGE_NUMBER = 1;
    String DEFAULT_GRID = "35";
    String DEFAULT_PICTURE = "100";
    boolean DEFAULT_ANIMATION = Boolean.TRUE;

    // Date & Time format constants
    String DATE_FORMAT = "d MMMM yyyy";
    String TIME_FORMAT_UK = "HH:mm";
    String TIME_FORMAT_US = "h:mm a";

    // Flickr API
    String API_KEY = "780abee617798d7558c97719e544db7f";
    String METHOD_GET_RECENT = "flickr.photos.getRecent";
    String METHOD_SEARCH = "flickr.photos.search";
    String JSON_BASE = "photos";
    String JSON_ARRAY = "photo";
    String JSON_ID = "id";
    String JSON_TITLE = "title";
    String JSON_UPLOAD_DATE = "dateupload";
    String JSON_OWNER_NAME = "ownername";
    String JSON_PICTURE_URL = "url_s";
}