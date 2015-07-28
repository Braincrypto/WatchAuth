package com.sri.csl.cortical.watchauth;

public class FingerNames {
    public enum HANDS {RIGHT, LEFT};

    public static HANDS hand = HANDS.RIGHT;

    public static final String[] fingers = {
            "Thumb",
            "Index",
            "Middle",
            "Ring",
            "Pinky"
    };

    public static String fingerName(int finger) {
        if(hand == HANDS.LEFT) {
            finger = 4-finger;
        }

        return fingers[finger];
    }
}
