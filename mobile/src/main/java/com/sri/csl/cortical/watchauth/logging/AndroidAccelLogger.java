package com.sri.csl.cortical.watchauth.logging;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class AndroidAccelLogger extends SensorLogger implements SensorEventListener {
    private Context ctx;
    private SensorManager sensorManager;
    private Sensor accelSensor;

    public AndroidAccelLogger(Context ctx) {
        super("accel.csv");
        out.println("androidtime,x,y,z");
        this.ctx = ctx;
        this.sensorManager = (SensorManager) ctx.getSystemService(Context.SENSOR_SERVICE);
        this.accelSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

    }
    @Override
    public void startLogging() {
        sensorManager.registerListener(this, accelSensor, SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    public void stopLogging() {
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        hasRecordedData = true;
        out.printf("%d,%d,%f,%f,%f\n", event.timestamp, event.accuracy,
                event.values[0], event.values[1], event.values[2]);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
