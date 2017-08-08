package com.choliy.igor.flickrgallery;

public interface FlickrConstants {

    // Keys
    String NOTIFICATION_KEY = "notification_key";
    String PICTURE_SAVED_KEY = "picture_saved_key";
    String PICTURE_DOWNLOADED_KEY = "picture_downloaded_key";
    String MENU_OPENED_KEY = "menu_key";
    String PICTURE_KEY = "picture_key";
    String TOOLBAR_KEY = "toolbar_key";
    String TITLE_KEY = "title_key";
    String SAVE_KEY = "save_key";
    String ITEM_KEY = "item_key";
    String URI_KEY = "uri_key";

    // Default variables
    String STRING_EMPTY = "";
    int INT_ZERO = 0;
    int INT_ONE = 1;
    int INT_TWO = 2;
    int INT_THREE = 3;

    // Default values
    int DEFAULT_LIST_POSITION = INT_ZERO;
    int DEFAULT_PAGE_NUMBER = INT_ONE;
    int DEFAULT_GRID_POSITION = INT_TWO;
    int DEFAULT_STYLE_POSITION = INT_ZERO;
    int DEFAULT_PICTURE_POSITION = INT_ONE;
    boolean DEFAULT_SPLASH = Boolean.TRUE;
    boolean DEFAULT_ANIMATION = Boolean.TRUE;
    boolean DEFAULT_NOTIFICATION = Boolean.FALSE;

    // Date & Time format constants
    String DATE_FORMAT = "d MMMM yyyy";
    String TIME_FORMAT_UK = "HH:mm";
    String TIME_FORMAT_US = "h:mm a";
    String TIME_FORMAT_DATE_TAKEN = "yyyy-MM-dd HH:mm:ss";
    String INFO_DATE_FORMAT_UK = DATE_FORMAT + " (" + TIME_FORMAT_UK + ")";
    String INFO_DATE_FORMAT_US = DATE_FORMAT + " (" + TIME_FORMAT_US + ")";

    // REST API
    String API_KEY = "780abee617798d7558c97719e544db7f";
    String METHOD_GET_RECENT = "flickr.photos.getRecent";
    String METHOD_SEARCH = "flickr.photos.search";
    String JSON_BASE = "photos";
    String JSON_ARRAY = "photo";
    String JSON_ID = "id";
    String JSON_TITLE = "title";
    String JSON_DATE = "datetaken";
    String JSON_OWNER_ID = "owner";
    String JSON_OWNER_NAME = "ownername";
    String JSON_DESCRIPTION = "description";
    String JSON_SMALL_LIST_PICTURE_URL = "url_sq";
    String JSON_LIST_PICTURE_URL = "url_s";
    String JSON_EXTRA_SMALL_PICTURE_URL = "url_n";
    String JSON_SMALL_PICTURE_URL = "url_z";
    String JSON_MEDIUM_PICTURE_URL = "url_l";
    String JSON_BIG_PICTURE_URL = "url_o";
    String JSON_NO_SUCH_SIZE = "no_such_size";
}