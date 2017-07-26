package com.choliy.igor.flickrgallery;

public interface FlickrConstants {

    // Keys
    String NOTIFICATION_KEY = "notification_key";
    String TOOLBAR_KEY = "toolbar_key";
    String TITLE_KEY = "title_key";
    String ITEM_KEY = "item_key";
    String URI_KEY = "uri_key";

    // Empty values
    String STRING_EMPTY = "";
    int INT_ZERO = 0;

    // Default values
    int DEFAULT_LIST_POSITION = 0;
    int DEFAULT_PAGE_NUMBER = 1;
    String DEFAULT_GRID = "35";
    String DEFAULT_STYLE = "simple";
    String DEFAULT_PICTURE = "100";
    boolean DEFAULT_ANIMATION = Boolean.TRUE;
    boolean DEFAULT_NOTIFICATION = Boolean.FALSE;

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
    String JSON_OWNER_ID = "owner";
    String JSON_OWNER_NAME = "ownername";
    String JSON_DESCRIPTION = "description";
    String JSON_LIST_PICTURE_URL = "url_s";
    String JSON_EXTRA_SMALL_PICTURE_URL = "url_n";
    String JSON_SMALL_PICTURE_URL = "url_z";
    String JSON_MEDIUM_PICTURE_URL = "url_l";
    String JSON_BIG_PICTURE_URL = "url_o";
    String JSON_NO_SUCH_SIZE = "no_such_size";
}