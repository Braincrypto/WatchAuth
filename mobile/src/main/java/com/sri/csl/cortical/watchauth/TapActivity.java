package com.sri.csl.cortical.watchauth;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.sri.csl.cortical.watchauth.logging.Logger;
import com.sri.csl.cortical.watchauth.logging.PebbleSensorLogger;
import com.sri.csl.cortical.watchauth.logging.SensorLogger;
import com.sri.csl.cortical.watchauth.logging.TapLogger;
import com.sri.csl.cortical.watchauth.logging.WearSensorLogger;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;

public class TapActivity extends Activity implements TapView.TapListener {

    public enum LOGGING_TYPE { PEBBLE, WEAR}

    public static final String TOUCH_BOXES = "com.sri.csl.cortical.watchauth.TOUCH_BOXES";
    private static final String TAG = "TapActivity";
    public static LOGGING_TYPE loggingType = null;
    private TapView view;
    private TrialPlayer player;
    private TextView prompt;
    private TapLogger tapLogger;
    private SensorLogger sensorLogger;
    private Handler handler;
    private Runnable waitForSensor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        goImmersive();
        setContentView(R.layout.activity_tap);
        Intent intent = getIntent();
        ArrayList<RectF> rects = intent.getParcelableArrayListExtra(TOUCH_BOXES);

        prompt = (TextView) this.findViewById(R.id.textView);

        view = (TapView) this.findViewById(R.id.tap_view);
        view.setTouchBoxes(rects);
        view.setTapListener(this);

        try {
            loadTrial();
        } catch (IOException e) {
            Toast toast = Toast.makeText(this, "Trial file not found.", Toast.LENGTH_LONG);
            return;
        }

        updateView();

        tapLogger = new TapLogger();

        if(loggingType == LOGGING_TYPE.PEBBLE) {
            sensorLogger = new PebbleSensorLogger(this);
        } else {
            sensorLogger = new WearSensorLogger(this);
        }

        handler = new Handler();
        waitForSensor = new Runnable() {
            @Override
            public void run() {
                if (sensorLogger.hasRecordedData()) {
                    player.ready();
                    updateView();
                } else {
                    handler.postDelayed(waitForSensor, 1000 / 10);
                }
            }
        };
    }

    protected void onResume() {
        super.onResume();
        goImmersive();
        sensorLogger.startLogging();
        handler.postDelayed(waitForSensor, 1000/ 10);
    }

    protected void onPause() {
        super.onPause();
        sensorLogger.stopLogging();
        tapLogger.close();
        sensorLogger.close();
    }

    @Override
    public void onTap(TouchBox box) {
        player.hitTarget(box.touchedRect);
        tapLogger.recordTap(box, player);

        if(player.done()) {
            Intent intent = new Intent(this, FinishedActivity.class);
            startActivity(intent);
            return;
        }

        updateView();
    }

    private void updateView() {
        view.setRectToHighlight(player.nextTarget());
        prompt.setText(player.prompt());
    }

    @Override
    public void onMotionEvent(MotionEvent event) {
        tapLogger.recordMotionEvent(event);
    }

    void loadTrial() throws IOException {
        File userTrial = new File(Logger.LOG_DIR, "trial.csv");
        byte[] buffer;

        if(userTrial.canRead()) {
            RandomAccessFile f = new RandomAccessFile(userTrial, "r");
            buffer = new byte[(int)f.length()];
            f.read(buffer);
        } else {
            InputStream in;
            Resources resources = getResources();

            in = resources.openRawResource(R.raw.trial);
            buffer = new byte[in.available()];
            in.read(buffer);
        }

        player = new TrialPlayer(new Trial(new String(buffer)));
    }

    public void goImmersive() {
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }
}
