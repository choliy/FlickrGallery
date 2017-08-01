package com.choliy.igor.flickrgallery.event;

public class PrefRestoreEvent {

    private boolean mRestoreSettings;

    public PrefRestoreEvent(boolean restoreSettings) {
        setRestoreSettings(restoreSettings);
    }

    public boolean isRestoreSettings() {
        return mRestoreSettings;
    }

    private void setRestoreSettings(boolean restoreSettings) {
        mRestoreSettings = restoreSettings;
    }
}