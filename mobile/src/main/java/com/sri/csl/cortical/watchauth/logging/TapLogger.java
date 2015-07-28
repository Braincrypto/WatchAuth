package com.sri.csl.cortical.watchauth.logging;

import android.view.MotionEvent;

import com.sri.csl.cortical.watchauth.TouchBox;
import com.sri.csl.cortical.watchauth.TrialPlayer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class TapLogger {
    private PrintWriter taps;

    public TapLogger() {
        try {
            taps = new PrintWriter(new File(Logger.sessionDirectory(), "taps.csv"));
            taps.println(csvHeader());
        }
        catch (FileNotFoundException e) {}
    }

    public void recordTap(TouchBox box, TrialPlayer player) {
        taps.print(box.toCsv());
        taps.print(",");
        taps.println(player.stateCsv());
    }

    public void recordMotionEvent(MotionEvent event) {
        // TODO: Record event.
    }

    public void close() {
        taps.close();
    }

    public static String csvHeader() {
        return "x,y,startTime,finishTime,rect,line,placeInSequence,repsCompleted";
    }
}
