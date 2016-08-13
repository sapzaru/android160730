package com.example.c.photogallery;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by c on 2016-08-13.
 */
public class ThumbnailDownloader<T> extends HandlerThread {
    private static final String TAG = "ThumbnailDownloader";

    private static final int MESSAGE_DOWNLOAD = 100;
    // 핸들러 쓰레드는 루퍼만 가지고 있으므로 핸들러를 따로 만들어 준다.
    private Handler mRequestHandler;
    private Handler mResponseHandler;

    public interface ThumbnailLoadListener<T> {
        void onThumbnailDownloaded(T target, Bitmap thumbnail);
    }

    public void setThumbnailLoadListener(ThumbnailLoadListener<T> listener)
    {
        mThumbnailLoadListener = listener;
    }
    private ThumbnailLoadListener<T> mThumbnailLoadListener;

    private ConcurrentMap<T, String> mRequestMap = new ConcurrentHashMap<>();

    public ThumbnailDownloader(Handler handler) {
        super(TAG);
        mResponseHandler = handler;
    }

    @Override
    protected void onLooperPrepared() {
        super.onLooperPrepared();
        mRequestHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == MESSAGE_DOWNLOAD) {
                    T target = (T) msg.obj;
                    handlerRequest(target);
                }
            }
        };
    }

    private void handlerRequest(final T target) {
        final String url = mRequestMap.get(target);
        if (url == null)
            return;

        try {
            byte[] bitmapBytes = new FlickrFetchr().getUrlBytes(url);
            final Bitmap bitmap = BitmapFactory.decodeByteArray(bitmapBytes, 0, bitmapBytes.length);
            mResponseHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (mRequestMap.get(target) != url) {
                        return;
                    }
                    mRequestMap.remove(target);
                    mThumbnailLoadListener.onThumbnailDownloaded(target, bitmap);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void queueThumbnail(T target, String url) {
        Log.d(TAG, "Thumbnail url : " + url);
        if (url == null) {
            mRequestMap.remove(target);
        } else {
            mRequestMap.put(target, url);
            mRequestHandler.obtainMessage(MESSAGE_DOWNLOAD, target).sendToTarget();

            // 위의 한줄과 같은 내용
            /*
            Message msg = mRequestHandler.obtainMessage();
            msg.what = MESSAGE_DOWNLOAD;
            msg.obj = target;
            mRequestHandler.sendMessage(msg);
            */
        }
    }

    public void clearQueue() {
        mRequestHandler.removeMessages(MESSAGE_DOWNLOAD);
    }
}
