package com.sri.csl.cortical.watchauth;

class TrialEvent {
    int[] sequence;
    int repetitions;
    String seqString;

    TrialEvent(String sequence, int repetitions) {
        this.repetitions = repetitions;

        String[] seqStrings = sequence.split(";");
        this.sequence = new int[seqStrings.length];

        for (int i = 0; i < seqStrings.length; i++) {
            this.sequence[i] = Integer.parseInt(seqStrings[i]);
        }

        this.seqString = sequence.replace(";", ", then ");
    }
}
