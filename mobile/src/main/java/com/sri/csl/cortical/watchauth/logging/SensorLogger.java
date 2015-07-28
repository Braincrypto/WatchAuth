package com.sri.csl.cortical.watchauth.logging;

import android.util.Log;

import java.io.File;
import java.io.PrintWriter;

public abstract class SensorLogger {
    protected volatile boolean hasRecordedData = false;
    protected PrintWriter out;

    protected SensorLogger(String logfile) {
        hasRecordedData = false;
        try {
            out = new PrintWriter(new File(Logger.sessionDirectory(), logfile));
        } catch (Exception e) {
            Log.d("SensorLogger", e.toString());
        }
    }

    public boolean hasRecordedData() {
        return hasRecordedData;
    }

    abstract public void startLogging();
    abstract public void stopLogging();

    public void close() {
        out.close();
    }
}
