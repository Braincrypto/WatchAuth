package com.sri.csl.cortical.watchauth;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

public class TapView extends View {
    private static final long MAX_TOUCH_TIME_NANO = 300000000L;
    private RectF[] fingerRects;
    private SparseArray<TouchBox> touches;
    private int rectToHighlight;
    private TapListener listener;

    public interface TapListener {
        void onTap(TouchBox box);
        void onMotionEvent(MotionEvent event);
    }

    public TapView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        fingerRects = null;
        touches = new SparseArray<>(5);
        rectToHighlight = -1;
    }

    public void setTouchBoxes(ArrayList<RectF> rects) {
        fingerRects = rects.toArray(new RectF[rects.size()]);
    }

    public void setRectToHighlight(int rect) {
        rectToHighlight = rect;
        invalidate();
    }
    
    public void setTapListener(TapListener listener) {
        this.listener = listener;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int pointerId = event.getPointerId(event.getActionIndex());
        int maskedAction = event.getActionMasked();

        TouchBox touch;
        switch(maskedAction) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                addTouch(event);
                break;
            case MotionEvent.ACTION_MOVE:
                int count = event.getPointerCount();
                for(int i = 0; i < count; i++) {
                    updateTouch(event.getPointerId(i), event.getX(i), event.getY(i));
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
            case MotionEvent.ACTION_CANCEL:
                finishTouch(pointerId);
                break;
        }

        listener.onMotionEvent(event);
        invalidate();
        return true;
    }

    private int touchInTheBox(TouchBox touch) {
        for(int i = 0; i < fingerRects.length; i++) {
            if (fingerRects[i].contains(touch.x, touch.y)) {
                return i;
            }
        }

        return -1;
    }

    private void addTouch(MotionEvent event) {
        int pointerIndex = event.getActionIndex();
        TouchBox touch = new TouchBox(event.getX(pointerIndex), event.getY(pointerIndex));
        int rect = touchInTheBox(touch);

        if(rect != -1) {
            touch.touchedRect = rect;
            int pointerId = event.getPointerId(pointerIndex);
            touches.put(pointerId, touch);
            //Log.d("TapView", "Started touch in rect " + rect + ".");
        } else {
            //Log.d("TapView", "Touch outside of rects. Discarding.");
        }
    }

    private void finishTouch(int pointerId) {
        TouchBox touch = touches.get(pointerId);
        if(touch == null) { return; }
        touch.finished = System.nanoTime();
        touches.remove(pointerId);
        
        if(touch.finished - touch.started > MAX_TOUCH_TIME_NANO) {
            //Log.d("TapView", "Touch took too long, discarding.");
        } else {
            //Log.d("TapView", "WE GOT ONE BOYS: " + touch.touchedRect);
            listener.onTap(touch);
        }
    }

    private void updateTouch(int pointerId, float x, float y) {
        long time = System.nanoTime();
        TouchBox touch = touches.get(pointerId);
        if (touch == null) { return; }

        if(fingerRects[touch.touchedRect].contains(x, y)) {
            touch.x = x;
            touch.y = y;
            touch.lastMoved = time;
        } else {
            //Log.d("TapView", "Touch in rect " + touch.touchedRect + " from pointer " + pointerId + " moved to " + x + "," + y + " out of bounds, discarding.");
            touches.remove(pointerId);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        for (int i = 0; i < fingerRects.length; i++) {
            CommonDrawing.drawBox(canvas, fingerRects[i], (i == rectToHighlight), Color.BLACK);
        }
    }
}
