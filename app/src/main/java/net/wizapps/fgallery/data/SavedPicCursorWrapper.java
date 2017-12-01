package net.wizapps.fgallery.data;

import android.database.Cursor;
import android.database.CursorWrapper;

import net.wizapps.fgallery.model.GalleryItem;

class SavedPicCursorWrapper extends CursorWrapper {

    SavedPicCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    GalleryItem getPictureItem() {
        String dbId = getString(getColumnIndex(FlickrContract._ID));
        String userId = getString(getColumnIndex(FlickrContract.COLUMN_PICTURE_USER_ID));
        String title = getString(getColumnIndex(FlickrContract.COLUMN_PICTURE_TITLE));
        String date = getString(getColumnIndex(FlickrContract.COLUMN_PICTURE_DATE));
        String ownerId = getString(getColumnIndex(FlickrContract.COLUMN_PICTURE_OWNER_ID));
        String ownerName = getString(getColumnIndex(FlickrContract.COLUMN_PICTURE_OWNER_NAME));
        String description = getString(getColumnIndex(FlickrContract.COLUMN_PICTURE_DESCRIPTION));
        String smallListUrl = getString(getColumnIndex(FlickrContract.COLUMN_PICTURE_SMALL_LIST_URL));
        String listUrl = getString(getColumnIndex(FlickrContract.COLUMN_PICTURE_LIST_URL));
        String extraSmallUrl = getString(getColumnIndex(FlickrContract.COLUMN_PICTURE_EXTRA_SMALL_URL));
        String smallUrl = getString(getColumnIndex(FlickrContract.COLUMN_PICTURE_SMALL_URL));
        String mediumUrl = getString(getColumnIndex(FlickrContract.COLUMN_PICTURE_MEDIUM_URL));
        String bigUrl = getString(getColumnIndex(FlickrContract.COLUMN_PICTURE_BIG_URL));

        GalleryItem item = new GalleryItem();
        item.setDbId(dbId);
        item.setUserId(userId);
        item.setTitle(title);
        item.setDate(date);
        item.setOwnerId(ownerId);
        item.setOwnerName(ownerName);
        item.setDescription(description);
        item.setSmallListPicUrl(smallListUrl);
        item.setListPicUrl(listUrl);
        item.setExtraSmallPicUrl(extraSmallUrl);
        item.setSmallPicUrl(smallUrl);
        item.setMediumPicUrl(mediumUrl);
        item.setBigPicUrl(bigUrl);

        return item;
    }
}