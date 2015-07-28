package com.sri.csl.cortical.watchauth;

public class TrialPlayer {

    private Trial trial;
    private int currentEvent;
    private int placeInSequence;
    private int repetitionsCompleted;

    public TrialPlayer(Trial trial) {
        this.trial = trial;
        this.currentEvent = -1;
        this.placeInSequence = 0;
        this.repetitionsCompleted = 0;
    }

    public int nextTarget() {
        if(!started() || done()) { return -1; }
        return trial.events[currentEvent].sequence[placeInSequence];
    }

    public void hitTarget(int target) {
        if(!started() || done()) { return; }
        TrialEvent event = trial.events[currentEvent];

        if(target == event.sequence[placeInSequence]) {
            // RECORD CORRECT BOX
            placeInSequence++;

            if (placeInSequence == event.sequence.length) {
                // RECORD SEQUENCE COMPLETE
                placeInSequence = 0;
                repetitionsCompleted++;

                if(repetitionsCompleted == event.repetitions) {
                    // RECORD EVENT COMPLETE
                    repetitionsCompleted = 0;
                    currentEvent++;
                }
            }
        } else {
            // RECORD WRONG BOX
            placeInSequence = 0;
        }
    }

    public void ready() {
        if(!started()) {
            currentEvent = 0;
        }
    }

    public boolean started() {
        return currentEvent >= 0;
    }
    public boolean done() {
        return currentEvent >= trial.events.length;
    }

    public String prompt() {
        if(done()) {
            return "All done!";
        }

        if(!started()) {
            return "Waiting for sensor data.";
        }

        TrialEvent event = trial.events[currentEvent];
        return "Please tap the sequence " + event.seqString + ", "
                + (event.repetitions - repetitionsCompleted) + " more times.";
    }
}
