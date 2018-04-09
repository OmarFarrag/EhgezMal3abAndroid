package com.homidev.egypt.ehgezmal3ab;

/**
 * Created by engineer on 09/04/18.
 */

public class Pitch {
    private String pitchTitle;
    private String pitchDescription;

    public Pitch(String pitchTitle, String pitchDescription) {

        this.pitchTitle = pitchTitle;

        this.pitchDescription = pitchDescription;
    }

    public String getPitchTitle() {
        return pitchTitle;
    }


    public void setPitchTitle(String pitchTitle) {
        this.pitchTitle = pitchTitle;
    }


    public String getPitchDescription() {
        return pitchDescription;
    }

    public void setPitchDescription(String pitchDescription) {
        this.pitchDescription = pitchDescription;
    }
}
