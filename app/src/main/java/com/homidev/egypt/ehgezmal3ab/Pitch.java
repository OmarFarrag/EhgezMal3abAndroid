package com.homidev.egypt.ehgezmal3ab;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Pitch implements Serializable{

    @SerializedName("venueID")
    @Expose
    private String venueID;
    @SerializedName("pitchName")
    @Expose
    private String pitchName;
    @SerializedName("capacity")
    @Expose
    private Integer capacity;
    @SerializedName("price")
    @Expose
    private Integer price;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("venue")
    @Expose
    private Object venue;

    /**
     * No args constructor for use in serialization
     *
     */
    public Pitch() {
    }

    /**
     *
     * @param price
     * @param pitchName
     * @param capacity
     * @param venue
     * @param type
     * @param venueID
     */
    public Pitch(String venueID, String pitchName, Integer capacity, Integer price, String type, Object venue) {
        super();
        this.venueID = venueID;
        this.pitchName = pitchName;
        this.capacity = capacity;
        this.price = price;
        this.type = type;
        this.venue = venue;
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

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Object getVenue() {
        return venue;
    }

    public void setVenue(Object venue) {
        this.venue = venue;
    }

}