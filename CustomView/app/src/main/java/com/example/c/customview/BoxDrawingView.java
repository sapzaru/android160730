package com.example.c.customview;

import android.content.Context;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by c on 2016-08-14.
 */
public class BoxDrawingView extends View {

    private Box mCurrentBox;
    private ArrayList<Box> mBoxes = new ArrayList<>();

    public BoxDrawingView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BoxDrawingView(Context context) {
        super(context);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        PointF current = new PointF(event.getX(), event.getY());
        Log.d("position", "current x : " + current.x + " y : " + current.y);

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mCurrentBox = new Box(current);
                mBoxes.add(mCurrentBox);
                break;
            case MotionEvent.ACTION_MOVE:
                if (mCurrentBox != null) {
                    mCurrentBox.setCurrent(current);
                }
                break;
            case MotionEvent.ACTION_UP:
                mCurrentBox = null;
                break;
        }

        return super.onTouchEvent(event);
    }
}
