package com.sri.csl.cortical.watchauth;

import android.graphics.RectF;

import java.io.Serializable;

public class TouchBox implements Serializable {
    public float x, y;
    public long lastMoved;
    public long started;
    public long finished;
    public int touchedRect = -1;
    public RectF rect;
    public boolean overlapping = false;

    public TouchBox(float x, float y) {
        this.started = System.nanoTime();
        this.move(x, y);
    }

    public void move(float x, float y) {
        lastMoved = System.nanoTime();
        this.x = x;
        this.y = y;
        this.rect = CommonDrawing.makeRect(x, y);
    }

    public String toCsv() {
        return String.format("%f,%f,%d,%d,%d", x, y, started, finished, touchedRect);
    }
}
