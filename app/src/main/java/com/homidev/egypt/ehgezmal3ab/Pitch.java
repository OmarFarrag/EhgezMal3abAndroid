package com.homidev.egypt.ehgezmal3ab;

/**
 * Created by engineer on 09/04/18.
 */

public class Pitch {
    private String pitchTitle;
    private String type;
    private int capacity;
    private Double price;
    private String venueID;

    public Pitch(String pitchTitle, String venueID) {

        this.pitchTitle = pitchTitle;
        this.venueID = venueID;
    }

    public Pitch(String pitchTitle, String type, int capacity, Double price, String venueID) {
        this.pitchTitle = pitchTitle;
        this.type = type;
        this.capacity = capacity;
        this.price = price;
        this.venueID = venueID;
    }


    public Pitch(String pitchTitle) {
        this.pitchTitle = pitchTitle;
    }

    public String getPitchTitle() {
        return pitchTitle;
    }

    public void setPitchTitle(String pitchTitle) {
        this.pitchTitle = pitchTitle;
    }

    public String getType() {
        return type;
    }

    public int getCapacity() {
        return capacity;
    }

    public Double getPrice() {
        return price;
    }

    public String getVenueID() {
        return venueID;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public void setVenueID(String venueID) {
        this.venueID = venueID;
    }
}
