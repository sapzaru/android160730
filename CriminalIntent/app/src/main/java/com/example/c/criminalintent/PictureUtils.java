package com.example.c.criminalintent;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;

/**
 * Created by c on 2016-08-07.
 */
public class PictureUtils {

    // 액티비티 화면 구해서 이미지를 그거보다 작게 해주도록..
    public static Bitmap getScaledBitmap(String path, Activity activity) {
        Point size = new Point();
        activity.getWindowManager().getDefaultDisplay().getSize(size);
        return getScaledBitmap(path, size.x, size.y);
    }

    // 메모리에 올리기 전에 가져올때부터 작게 만들어 버림
    public static Bitmap getScaledBitmap(String path, int destWidth, int destHeight) {
        // 이미지가 너무 큰 경우 아웃오브메모리 등.. 표시가 잘 안되는 경우가 있으므로 사이즈를 줄여준다.
        // 비트맵 크기 알아내기

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;  // 이걸하면 비트맵이 아니고 헤더정보들만 얻어온다.
        BitmapFactory.decodeFile(path, options);

        float srcWidth = options.outWidth;
        float srcHeight = options.outHeight;

        // 샘플링할 비율 알아오기
        int inSampleSize = 1;   // 1이면 1:1, 4면 4픽셀마다 하나씩 가져온다.
        if (srcHeight > destHeight || srcWidth > destWidth) {
            if (srcWidth > srcHeight) {
                inSampleSize = Math.round(srcHeight / destHeight);
            } else {
                inSampleSize = Math.round(srcWidth / destWidth);
            }
        }

        options = new BitmapFactory.Options();
        options.inSampleSize = inSampleSize;
        return BitmapFactory.decodeFile(path, options);
    }
}
