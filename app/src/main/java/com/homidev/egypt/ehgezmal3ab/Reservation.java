package com.homidev.egypt.ehgezmal3ab;

import java.util.Date;

/**
 * Created by User on 18/04/2018.
 */

public class Reservation {

    private String username;
    private String startsOn;
    private String endsOn;
    private String venueID;
    private String pitchName;
    private String status;

    public Reservation(String f_username, String f_startsOn, String f_endsOn, String f_VenueID, String f_pitchName, String f_status)
    {
        username=f_username;
        startsOn=f_startsOn;
        endsOn=f_endsOn;
        venueID=f_VenueID;
        pitchName=f_pitchName;
        status=f_status;
    }

    public String getEndsOn() {
        return endsOn;
    }

    public String getStartsOn() {
        return startsOn;
    }

    public String getPitchName() {
        return pitchName;
    }

    public String getStatus() {
        return status;
    }

    public String getUsername() {
        return username;
    }

    public String getVenueID() {
        return venueID;
    }

}
