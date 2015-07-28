package com.sri.csl.cortical.watchauth;

class TrialEvent {
    int[] sequence;
    int repetitions;
    String seqString;

    TrialEvent(String sequence, int repetitions) {
        this.repetitions = repetitions;

        String[] seqStrings = sequence.split(";");
        this.sequence = new int[seqStrings.length];
        this.seqString = "";

        for (int i = 0; i < seqStrings.length; i++) {
            this.sequence[i] = Integer.parseInt(seqStrings[i]);
            if(i > 0) { this.seqString += ","; }
            this.seqString += FingerNames.fingerName(this.sequence[i]);
        }
    }
}
