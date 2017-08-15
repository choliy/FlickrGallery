package com.choliy.igor.flickrgallery.data;

import android.provider.BaseColumns;

final class FlickrContract implements BaseColumns {

    // History Table
    static final String TABLE_HISTORY = "table_history";
    static final String COLUMN_HISTORY_TITLE = "column_history_title";
    static final String COLUMN_HISTORY_DATE = "column_history_date";
    static final String COLUMN_HISTORY_TIME = "column_history_time";

    // Saved Table
    static final String TABLE_SAVED = "table_saved";
    static final String COLUMN_PICTURE_USER_ID = "column_saved_user_id";
    static final String COLUMN_PICTURE_TITLE = "column_saved_title";
    static final String COLUMN_PICTURE_DATE = "column_saved_date";
    static final String COLUMN_PICTURE_OWNER_ID = "column_saved_owner_id";
    static final String COLUMN_PICTURE_OWNER_NAME = "column_saved_owner_name";
    static final String COLUMN_PICTURE_DESCRIPTION = "column_saved_description";
    static final String COLUMN_PICTURE_SMALL_LIST_URL = "column_saved_small_list_url";
    static final String COLUMN_PICTURE_LIST_URL = "column_saved_list_url";
    static final String COLUMN_PICTURE_EXTRA_SMALL_URL = "column_saved_extra_small_url";
    static final String COLUMN_PICTURE_SMALL_URL = "column_saved_small_url";
    static final String COLUMN_PICTURE_MEDIUM_URL = "column_saved_medium_url";
    static final String COLUMN_PICTURE_BIG_URL = "column_saved_big_url";
}