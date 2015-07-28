package com.sri.csl.cortical.watchauth.logging;

import android.content.Context;

import com.getpebble.android.kit.PebbleKit;
import com.getpebble.android.kit.util.PebbleDictionary;

import java.util.UUID;

public class PebbleSensorLogger extends SensorLogger {
    class PebbleSensorReceiver extends PebbleKit.PebbleDataReceiver {
        private PebbleSensorLogger delegate;
        protected PebbleSensorReceiver(UUID subscribedUuid, PebbleSensorLogger delegate) {
            super(subscribedUuid);
            this.delegate = delegate;
        }

        @Override
        public void receiveData(Context context, int transactionId, PebbleDictionary data) {
            PebbleKit.sendAckToPebble(context, transactionId);

            for(int i = 0; i < NUM_SAMPLES; i++) {
                try {
                    int timestamp = data.getInteger(4 * i).intValue();
                    int x = data.getInteger(4 * i + 1).intValue();
                    int y = data.getInteger(4 * i + 2).intValue();
                    int z = data.getInteger(4 * i + 3).intValue();

                    delegate.logReading(timestamp, x, y, z);
                } catch (Exception e) {}
            }
        }
    }

    private static final int NUM_SAMPLES = 15;

    private PebbleKit.PebbleDataReceiver receiver;
    private UUID uuid = UUID.fromString("2893b0c4-2bca-4c83-a33a-0ef6ba6c8b17");
    private Context context;

    public PebbleSensorLogger(Context ctx) {
        super("pebble.csv");
        receiver = new PebbleSensorReceiver(uuid, this);
        context = ctx;
    }

    public void startLogging() {
        PebbleKit.registerReceivedDataHandler(context, receiver);
        PebbleKit.startAppOnPebble(context, uuid);
        PebbleDictionary dict = new PebbleDictionary();
        dict.addInt32(0, 0);
        PebbleKit.sendDataToPebble(context, uuid, dict);
    }

    public void stopLogging() {
        PebbleKit.closeAppOnPebble(context, uuid);
        context.unregisterReceiver(receiver);
    }

    public void logReading(int timestamp, int x, int y, int z) {
        hasRecordedData = true;
        out.printf("%d,%d,%d,%d\n", timestamp, x, y, z);
    }
}
