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

            for(int i = 0; i < data.size(); i++) {
                byte[] bytes = data.getBytes(i);
                if(bytes != null) {
                    PebbleAccelData accel = new PebbleAccelData(bytes);
                    delegate.logAccelData(accel);
                }
            }
        }
    }

    private void logAccelData(PebbleAccelData accel) {
        hasRecordedData = true;
        out.printf("%d,%d,%d,%d,%d\n", System.nanoTime(), accel.timestamp, accel.x, accel.y, accel.z);
    }

    private PebbleKit.PebbleDataReceiver receiver;
    private UUID uuid = UUID.fromString("fad113ca-4433-4469-aba4-d75ebd5dc2dd");
    private Context context;

    public PebbleSensorLogger(Context ctx) {
        super("pebble.csv");
        out.println("androidtime,pebbletime,x,y,z");
        receiver = new PebbleSensorReceiver(uuid, this);
        context = ctx;
    }

    public void startLogging() {
        PebbleKit.registerReceivedDataHandler(context, receiver);
        PebbleKit.startAppOnPebble(context, uuid);

        sendStartMessageToApp();
    }

    private void sendStartMessageToApp() {
        PebbleDictionary dict = new PebbleDictionary();
        dict.addInt32(0, 0);
        PebbleKit.sendDataToPebble(context, uuid, dict);
    }

    @Override
    public boolean hasRecordedData() {
        if(!hasRecordedData) {
            sendStartMessageToApp();
        }
        return super.hasRecordedData();
    }

    public void stopLogging() {
        PebbleKit.closeAppOnPebble(context, uuid);
        context.unregisterReceiver(receiver);
    }
}
