package com.example.c.photogallery;

import android.os.HandlerThread;
import android.util.Log;

/**
 * Created by c on 2016-08-13.
 */
public class ThumbnailDownloader extends HandlerThread {
    private static final String TAG = "ThumbnailDownloader";
    public ThumbnailDownloader() {
        super(TAG);
    }

    public void queueThumbnail(String url) {
        Log.d(TAG, "Thumbnail url : " + url);
    }
}
