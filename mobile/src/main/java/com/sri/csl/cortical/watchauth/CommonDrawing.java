package com.sri.csl.cortical.watchauth;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;

public class CommonDrawing {
    public static final int BOX_SIZE=160;

    public static void drawBox(Canvas canvas, RectF rect, boolean filled, int color) {
        Paint paint = new Paint();
        paint.setColor(color);
        paint.setStrokeWidth(10);
        paint.setStyle(filled ? Paint.Style.FILL_AND_STROKE : Paint.Style.STROKE);
        canvas.drawRect(rect, paint);
    }

    public static void drawBox(Canvas canvas, PointF point, boolean filled, int color) {
        drawBox(canvas, makeRect(point), filled, color);
    }

    public static RectF makeRect(float x, float y) {
        return makeRect(x, y, BOX_SIZE);
    }

    public static RectF makeRect(PointF point) {
        return makeRect(point.x, point.y);
    }

    public static RectF makeRect(float x, float y, int size) {
        return new RectF(
                x - size/2,
                y - size/2,
                x + size/2,
                y + size/2
        );
    }
}
