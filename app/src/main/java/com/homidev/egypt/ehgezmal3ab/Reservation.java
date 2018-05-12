package com.homidev.egypt.ehgezmal3ab;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class    Reservation {

    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("startsOn")
    @Expose
    private String startsOn;
    @SerializedName("endsOn")
    @Expose
    private String endsOn;
    @SerializedName("venueID")
    @Expose
    private String venueID;
    @SerializedName("pitchName")
    @Expose
    private String pitchName;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("price")
    @Expose
    private int price;
    @SerializedName("promoCode")
    @Expose
    private String promoCode;
    @SerializedName("venuename")
    private String venueName;

    /**
     * No args constructor for use in serialization
     *
     */
    public Reservation() {
    }

    public Reservation(String username, String startsOn, String endsOn, String venueID, String pitchName) {
        super();
        this.username = username;
        this.startsOn = startsOn;
        this.endsOn = endsOn;
        this.venueID = venueID;
        this.pitchName = pitchName;
        this.status="";
    }

    /**
     *
     * @param username
     * @param promoCode
     * @param pitchName
     * @param endsOn
     * @param startsOn
     * @param venueID
     */
    public Reservation(String username, String startsOn, String endsOn, String venueID, String pitchName, String promoCode) {
        super();
        this.username = username;
        this.startsOn = startsOn;
        this.endsOn = endsOn;
        this.venueID = venueID;
        this.pitchName = pitchName;
        this.promoCode = promoCode;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getStartsOn() {
        return startsOn;
    }

    public void setStartsOn(String startsOn) {
        this.startsOn = startsOn;
    }

    public String getEndsOn() {
        return endsOn;
    }

    public void setEndsOn(String endsOn) {
        this.endsOn = endsOn;
    }

    public String getVenueID() {
        return venueID;
    }

    public void setVenueID(String venueID) {
        this.venueID = venueID;
    }

    public String getPitchName() {
        return pitchName;
    }

    public void setPitchName(String pitchName) {
        this.pitchName = pitchName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getPrice(){return price;}

    public String getVenueName() {
        return venueName;
    }

    public void setVenueName(String venueName) {
        this.venueName = venueName;
    }
}