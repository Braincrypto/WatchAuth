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

        out.println("left,top,right,bottom");
        for(RectF rect : rects) {
            out.printf("%f,%f,%f,%f\n", rect.left, rect.top, rect.right, rect.bottom);
        }
        out.close();
    }
}
