package com.sri.csl.cortical.watchauth.wear.events;

import com.sri.csl.cortical.watchauth.wear.data.Sensor;
import com.sri.csl.cortical.watchauth.wear.data.SensorDataPoint;

public class SensorUpdatedEvent {
    private Sensor sensor;
    private SensorDataPoint sensorDataPoint;

    public SensorUpdatedEvent(Sensor sensor, SensorDataPoint sensorDataPoint) {
        this.sensor = sensor;
        this.sensorDataPoint = sensorDataPoint;
    }

    public Sensor getSensor() {
        return sensor;
    }

    public SensorDataPoint getDataPoint() {
        return sensorDataPoint;
    }
}
