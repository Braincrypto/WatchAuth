package com.sri.csl.cortical.watchauth.logging;

import android.content.Context;
import android.hardware.Sensor;

import com.squareup.otto.Subscribe;
import com.sri.csl.cortical.watchauth.wear.RemoteSensorManager;
import com.sri.csl.cortical.watchauth.wear.events.BusProvider;
import com.sri.csl.cortical.watchauth.wear.events.SensorUpdatedEvent;

public class WearSensorLogger extends SensorLogger {
    RemoteSensorManager manager;
    public WearSensorLogger(Context ctx) {
        super("wear.csv");
        out.println("androidTime,wearTime,v0,v1,v2");

        manager = RemoteSensorManager.getInstance(ctx);
    }

    public void startLogging() {
        manager.startMeasurement();
        BusProvider.getInstance().register(this);
    }

    public void stopLogging() {
        manager.stopMeasurement();
        BusProvider.getInstance().unregister(this);
    }

    @Subscribe
    public void onSensorUpdatedEvent(SensorUpdatedEvent event) {
        if(event.getSensor().getId() == Sensor.TYPE_ACCELEROMETER) {
            hasRecordedData = true;
            logAccelerometer(event.getDataPoint().getTimestamp(), event.getDataPoint().getValues());
        }
    }

    private void logAccelerometer(long timestamp, float[] values) {
        out.printf("%d,%d,%f,%f,%f\n", System.nanoTime(), timestamp, values[0], values[1], values[2]);
    }
}
