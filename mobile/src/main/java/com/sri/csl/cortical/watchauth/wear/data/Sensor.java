package com.sri.csl.cortical.watchauth.wear.data;

import java.util.LinkedList;

public class Sensor {
    private static final String TAG = "SDB/Sensor";
    private static final int MAX_DATA_POINTS = 10000000;

    private long id;
    private String name;
    private float minValue = Integer.MAX_VALUE;
    private float maxValue = Integer.MIN_VALUE;

    private LinkedList<SensorDataPoint> dataPoints = new LinkedList<SensorDataPoint>();

    public Sensor(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public float getMaxValue() {
        return maxValue;
    }

    public float getMinValue() {
        return minValue;
    }

    public synchronized LinkedList<SensorDataPoint> getDataPoints() {
        return (LinkedList<SensorDataPoint>) dataPoints.clone();
    }

    public synchronized void addDataPoint(SensorDataPoint dataPoint) {
        dataPoints.addLast(dataPoint);

        if (dataPoints.size() > MAX_DATA_POINTS) {
            dataPoints.removeFirst();
        }

        for (float value : dataPoint.getValues()) {
            if (value > maxValue) {
                maxValue = value;
            }
            if (value < minValue) {
                minValue = value;
            }
        }
    }

    public long getId() {
        return id;
    }
}
