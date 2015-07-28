package com.sri.csl.cortical.watchauth.logging;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class Logger {
    public static final File LOG_DIR = new File(Environment.getExternalStorageDirectory(), "watchauth");
    private static final String SHARED_PREFERENCES_KEY = "com.sri.csl.cortical.watchauth.logging";

    protected static int sessionID = -1;

    public static File sessionDirectory() {
        return new File(LOG_DIR, "" + sessionID);
    }

    public static int newSession(Context ctx) {
        SharedPreferences prefs = ctx.getSharedPreferences(SHARED_PREFERENCES_KEY, 0);
        sessionID = prefs.getInt("lastSessionID", 10000) + 1;

        if(sessionDirectory().mkdirs()) {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("lastSessionID", sessionID);
            editor.apply();
            return sessionID;
        }

        return -1;
    }

    public static int currentSession()
    {
        return sessionID;
    }

    public static void logDemographics(String age, String gender, String hand) {
        PrintWriter out = null;
        try {
            out = new PrintWriter(new File(sessionDirectory(), "demographics.txt"));
        } catch (FileNotFoundException e) {
        }
        out.println("Age: " + age);
        out.println("Gender: " + gender);
        out.println("Handedness: " + hand);
        out.close();
    }
}
