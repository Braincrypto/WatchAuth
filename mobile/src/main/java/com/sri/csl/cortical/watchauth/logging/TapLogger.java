package com.sri.csl.cortical.watchauth.logging;

import android.view.MotionEvent;

import com.sri.csl.cortical.watchauth.TouchBox;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class TapLogger {
    private PrintWriter taps;

    public TapLogger() {
        try {
            taps = new PrintWriter(new File(Logger.sessionDirectory(), "taps.csv"));
        }
        catch (FileNotFoundException e) {}
    }

    public void recordTap(TouchBox box) {
        taps.println(box.toCsv());
    }

    public void recordMotionEvent(MotionEvent event) {
        // TODO: Record event.
    }

    public void close() {
        taps.close();
    }
}
