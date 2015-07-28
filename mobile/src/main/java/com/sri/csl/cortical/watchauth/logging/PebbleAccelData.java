package com.sri.csl.cortical.watchauth.logging;

public class PebbleAccelData {
    final public int x;
    final public int y;
    final public int z;

    final private boolean didVibrate;
    public long timestamp = 0;

    public static final int NUM_BYTES=15;

    public PebbleAccelData(byte[] data) {
        this(data, 0);
    }

    public PebbleAccelData(byte[] data, int index) {
        index = index * NUM_BYTES;
        x = (data[index+0] & 0xff) | (data[index+1] << 8);
        y = (data[index+2] & 0xff) | (data[index+3] << 8);
        z = (data[index+4] & 0xff) | (data[index+5] << 8);

        didVibrate = data[index+6] != 0;
        for (int i = 0; i < 8; i++) {
            timestamp |= ((long)(data[index+i+7] & 0xff)) << (i * 8);
        }
    }
}
