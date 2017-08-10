package com.choliy.igor.flickrgallery.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImageSaver {

    private static final String TAG = ImageSaver.class.getSimpleName();
    private static final String FORMAT_JPEG = ".jpeg";
    public static final int QUALITY = 100;

    private Context mContext;
    private String mDirectoryName;
    private String mFileName;
    private boolean mExternal;

    public ImageSaver(Context context) {
        mContext = context;
    }

    public ImageSaver setDirectoryName(String directoryName) {
        mDirectoryName = directoryName;
        return this;
    }

    public ImageSaver setFileName(String fileName) {
        mFileName = fileName + FORMAT_JPEG;
        return this;
    }

    public ImageSaver setExternal(boolean external) {
        mExternal = external;
        return this;
    }

    public void save(Bitmap bitmap) {
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(createFile());
            bitmap.compress(Bitmap.CompressFormat.JPEG, QUALITY, fileOutputStream);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (fileOutputStream != null) fileOutputStream.close();
            } catch (IOException e) {
                Log.e(TAG, e.getMessage());
                e.printStackTrace();
            }
        }
    }

    @NonNull
    private File createFile() {
        File directory;
        if (mExternal)
            directory = getAlbumStorageDir(mDirectoryName);
        else
            directory = mContext.getDir(mDirectoryName, Context.MODE_PRIVATE);

        Log.i(TAG, directory.getAbsolutePath() + File.separator + mFileName);
        return new File(directory, mFileName);
    }

    private File getAlbumStorageDir(String albumName) {
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), albumName);
        if (!file.mkdirs()) Log.i(TAG, "Directory not created");
        return file;
    }

    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    public static boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state);
    }

    public Bitmap load() {
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(createFile());
            return BitmapFactory.decodeStream(inputStream);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) inputStream.close();
            } catch (IOException e) {
                Log.e(TAG, e.getMessage());
                e.printStackTrace();
            }
        }
        return null;
    }
}