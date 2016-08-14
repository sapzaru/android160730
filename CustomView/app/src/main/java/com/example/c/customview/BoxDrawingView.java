package com.example.c.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
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

        mBackgroundPaint = new Paint();
        mBackgroundPaint.setColor(0xfff8efe0);

        mBoxPaint = new Paint();
        mBoxPaint.setColor(0x22ff0000);
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
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
                mCurrentBox = null;
                break;
        }

        // 이런것을 활용하면 TextView 같은것도 터치 이번트로 FrameLayout 같은
        // ViewGroup 내에서 드래그 앤 드롭으로 포지션을 바꿔서 위치를
        // 이동시킬 수 있지 않을까 생각해봄

        // true를 리턴해 줘야 처리가 된다.
        //return super.onTouchEvent(event);
        return true;
    }

    private Paint mBoxPaint;
    private Paint mBackgroundPaint;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawPaint(mBackgroundPaint);

        for (Box box : mBoxes) {
            float left = Math.min(box.getOrigin().x, box.getCurrent().x);
            float right = Math.max(box.getOrigin().x, box.getCurrent().x);
            float top = Math.min(box.getOrigin().y, box.getCurrent().y);
            float bottom = Math.max(box.getOrigin().y, box.getCurrent().y);

            canvas.drawRect(left, top, right, bottom, mBoxPaint);
        }
    }
}
