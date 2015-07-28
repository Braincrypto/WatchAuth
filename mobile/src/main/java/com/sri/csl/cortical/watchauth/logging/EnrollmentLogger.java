package com.sri.csl.cortical.watchauth.logging;

import android.graphics.RectF;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class EnrollmentLogger {
    public static void logEnrollment(ArrayList<RectF> rects) {
        PrintWriter out = null;
        try {
            out = new PrintWriter(new File(Logger.sessionDirectory(), "enrollment.txt"));
        } catch (FileNotFoundException e) {
        }

        for(RectF rect : rects) {
            out.println(rect.toString());
        }
        out.close();
    }
}
