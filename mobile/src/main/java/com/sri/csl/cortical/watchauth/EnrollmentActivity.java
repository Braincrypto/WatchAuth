package com.sri.csl.cortical.watchauth;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PointF;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.sri.csl.cortical.watchauth.logging.EnrollmentLogger;

import java.util.ArrayList;


public class EnrollmentActivity extends Activity implements EnrollmentView.EnrollmentListener {
    private EnrollmentView view;
    private Runnable updateView;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enrollment);

        view = (EnrollmentView) this.findViewById(R.id.enrollment_view);
        view.setEnrollmentListener(this);

        handler = new Handler();
        updateView = new Runnable() {
            @Override
            public void run() {
                if(!view.checkFinished()) {
                    handler.postDelayed(updateView, 1000/30);
                }
                view.invalidate();
            }
        };

    }

    public void onEnrollmentComplete(View v, ArrayList<RectF> rects) {
        EnrollmentLogger.logEnrollment(rects);
        Intent intent = new Intent(this, TapActivity.class);
        intent.putParcelableArrayListExtra(TapActivity.TOUCH_BOXES, rects);
        startActivity(intent);
    }

    public void onResume() {
        super.onResume();
        view.reset();
        handler.post(updateView);
    }

    public void onPause() {
        super.onPause();
        handler.removeCallbacks(updateView);
    }
}
