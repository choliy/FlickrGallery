package com.choliy.igor.galleryforflickr.event;

public class AnimPrefEvent {

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