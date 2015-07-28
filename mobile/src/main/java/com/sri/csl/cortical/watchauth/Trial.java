package com.sri.csl.cortical.watchauth;

public class Trial {

    TrialEvent[] events;

    public Trial(String fileContents) {
        String[] lines = fileContents.split("\n");
        events = new TrialEvent[lines.length];

        for(int i = 0; i < lines.length; i++) {
            events[i] = parseLine(lines[i]);
        }
    }

    TrialEvent parseLine(String line) {
        String[] components = line.split(",");
        String sequence = components[0];
        int repetitions = Integer.parseInt(components[1]);

        return new TrialEvent(sequence, repetitions);
    }

}
