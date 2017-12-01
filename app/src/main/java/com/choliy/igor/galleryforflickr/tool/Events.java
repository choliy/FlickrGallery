package com.choliy.igor.galleryforflickr.tool;

import com.choliy.igor.galleryforflickr.model.GalleryItem;

import java.util.List;

public final class Events {

    private Events() {}

    public static class AnimPrefEvent {

        private boolean mIsAnimationOn;

        public AnimPrefEvent(boolean isAnimationOn) {
            setAnimationOn(isAnimationOn);
        }

        public boolean isAnimationOn() {
            return mIsAnimationOn;
        }

        private void setAnimationOn(boolean animationOn) {
            mIsAnimationOn = animationOn;
        }
    }

    public static class BackPressedEvent {}

    public static class BaseEvent {}

    public static class DeleteFinishEvent {}

    public static class DeleteStartEvent {}

    public static class FetchFinishEvent {

        private List<GalleryItem> mPictures;

        public FetchFinishEvent(List<GalleryItem> pictures) {
            setPictures(pictures);
        }

        public List<GalleryItem> getPictures() {
            return mPictures;
        }

        private void setPictures(List<GalleryItem> pictures) {
            mPictures = pictures;
        }
    }

    public static class FetchStartEvent {}

    public static class HistoryStartEvent {}

    public static class HistoryTitleEvent {

        private String mHistoryTitle;

        public HistoryTitleEvent(String historyTitle) {
            setHistoryTitle(historyTitle);
        }

        public String getHistoryTitle() {
            return mHistoryTitle;
        }

        private void setHistoryTitle(String historyTitle) {
            mHistoryTitle = historyTitle;
        }
    }

    public static class ItemLastEvent {}

    public static class ItemPositionEvent {

        private int mPosition;

        public ItemPositionEvent(int position) {
            setPosition(position);
        }

        public int getPosition() {
            return mPosition;
        }

        private void setPosition(int position) {
            mPosition = position;
        }
    }

    public static class PictureClickEvent {

        private String mPicUrl;
        private byte[] mBytes;

        public PictureClickEvent(String picUrl, byte[] bytes) {
            setPicUrl(picUrl);
            setBytes(bytes);
        }

        public String getPicUrl() {
            return mPicUrl;
        }

        private void setPicUrl(String picUrl) {
            mPicUrl = picUrl;
        }

        public byte[] getBytes() {
            return mBytes;
        }

        private void setBytes(byte[] bytes) {
            mBytes = bytes;
        }
    }

    public static class PrefRestoreEvent {}

    public static class RemovePicEvent {}

    public static class RestoreFinishEvent {}

    public static class RestoreStartEvent {}

    public static class SaveFinishEvent {

        private boolean mClearHistoryBase;

        public SaveFinishEvent(boolean clearHistoryBase) {
            setClearHistoryBase(clearHistoryBase);
        }

        public boolean isClearHistoryBase() {
            return mClearHistoryBase;
        }

        private void setClearHistoryBase(boolean clearHistoryBase) {
            mClearHistoryBase = clearHistoryBase;
        }
    }

    public static class SaveStartEvent {}

    public static class SwipePositionEvent {

        private int mPosition;

        public SwipePositionEvent(int position) {
            setPosition(position);
        }

        public int getPosition() {
            return mPosition;
        }

        private void setPosition(int position) {
            mPosition = position;
        }
    }

    public static class ToolbarTypeEvent {}

    public static class ToolbarVisibilityEvent {

        private boolean mShowToolbar;

        public ToolbarVisibilityEvent(boolean showToolbar) {
            setShowToolbar(showToolbar);
        }

        public boolean isShowToolbar() {
            return mShowToolbar;
        }

        private void setShowToolbar(boolean showToolbar) {
            mShowToolbar = showToolbar;
        }
    }

    public static class TopListEvent {}
}