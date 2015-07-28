package com.sri.csl.cortical.watchauth;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class EnrollmentView extends View {

    public interface EnrollmentListener {
        void onEnrollmentComplete(View v, ArrayList<RectF> rects);
    }

    private SparseArray<TouchBox> touches;
    private EnrollmentListener listener;

    // The amount of time (3 seconds) to hold position before treating a touch as fixed.
    public static final long ENROLLMENT_TIME = 3000000000L;

    // The amount you can wiggle a finger (in pixels) before it counts as having moved.
    public static final float WIGGLE_ROOM = 50;

    // The amount of time to hold a finger before movement stops being continuous
    public static final long WIGGLE_TIME = 100000000L;

    public EnrollmentView(Context context, AttributeSet attrs) {
        super(context, attrs);
        reset();
    }

    public void setEnrollmentListener(EnrollmentListener listener) {
        this.listener = listener;
    }

    public void reset() {
        touches = new SparseArray<>(5);
    }

    public boolean checkFinished() {
        if(touches.size() != 5) { return false; }
        long time = System.nanoTime();

        for(int i = 0; i < touches.size(); i++) {
            TouchBox touch = touches.valueAt(i);
            if(time - touch.lastMoved < ENROLLMENT_TIME) { return false; }
            if(touch.overlapping) { return false; }
        }

        notifyComplete();
        return true;
    }

    private void notifyComplete() {
        ArrayList<RectF> rectArray = new ArrayList<>(touches.size());
        for(int i = 0; i < touches.size(); i++) {
            rectArray.add(touches.get(i).rect);
        }
        Collections.sort(rectArray, new Comparator<RectF>() {
            public int compare(RectF a, RectF b) {
                if (a.left < b.left) { return -1; }
                else if (a.left > b.left) { return 1; }
                else { return 0; }
            }
        });
        listener.onEnrollmentComplete(this, rectArray);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int pointerIndex = event.getActionIndex();
        int pointerId = event.getPointerId(pointerIndex);
        int maskedAction = event.getActionMasked();

        TouchBox touch;
        switch(maskedAction) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                touch = new TouchBox(event.getX(pointerIndex), event.getY(pointerIndex));
                touches.put(pointerId, touch);
                break;
            case MotionEvent.ACTION_MOVE:
                int count = event.getPointerCount();
                for(int i = 0; i < count; i++) {
                    touch = touches.get(event.getPointerId(i));
                    updateBox(touch, event.getX(i), event.getY(i));
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
            case MotionEvent.ACTION_CANCEL:
                touches.remove(pointerId);
                break;
        }

        checkOverlaps();
        invalidate();
        return true;
    }

    private void checkOverlaps() {
        for(int i = 0; i < touches.size(); i++) {
            touches.valueAt(i).overlapping = false;
        }

        for(int i = 0; i < touches.size(); i++) {
            TouchBox t1 = touches.valueAt(i);
            for(int j = i+1; j < touches.size(); j++) {
                TouchBox t2 = touches.valueAt(j);

                if(t1.rect.intersect(t2.rect)) {
                    t1.overlapping = t2.overlapping = true;
                }
            }
        }
    }

    private void updateBox(TouchBox touch, float x, float y) {
        long timeSinceMove = System.nanoTime() - touch.lastMoved;
        double distanceMoved = PointF.length(touch.x - x, touch.y - y);

        if (timeSinceMove < WIGGLE_TIME || distanceMoved > WIGGLE_ROOM) {
            touch.move(x, y);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.LTGRAY);
        long time = System.nanoTime();
        int color;

        for(int i = 0; i < touches.size(); i++) {
            TouchBox touch = touches.get(i);
            if (touch == null) continue;

            if(time - touch.lastMoved < WIGGLE_TIME || touch.overlapping) {
                color = Color.RED;
            } else if (time - touch.lastMoved < ENROLLMENT_TIME) {
                color = Color.YELLOW;
            } else {
                color = Color.GREEN;
            }

            CommonDrawing.drawBox(canvas, CommonDrawing.makeRect(touch.x, touch.y), false, color);
        }
    }
}
