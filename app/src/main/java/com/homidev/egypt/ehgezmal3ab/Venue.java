package com.homidev.egypt.ehgezmal3ab;

/**
 * Created by engineer on 27/03/18.
 * model class for Venue
 */

import com.google.gson.annotations.*;

public class Venue {
    @SerializedName("venueName")
    private String venueTitle;
    @SerializedName("phoneNumber")
    String phoneNumber;
    @SerializedName("area")
    String area;
    @SerializedName("street")
    String street;
    int venueImageRes;
    @SerializedName("longitude")
    float longitude;
    @SerializedName("lattitude")
    float latitude;
    @SerializedName("venueID")
    String venueID;
    @SerializedName("imageLink")
    String imageLink;

    //average rating of all of the venue pitches.
    float venueRating;

    public Venue(String venueTitle, String phoneNumber,
                 String area, String street, int venueImageRes,
                 float longitude, float latitude, String venueID, String imageLink) {
        this.venueTitle = venueTitle;
        this.phoneNumber = phoneNumber;
        this.area = area;
        this.street = street;
        this.venueImageRes = venueImageRes;
        this.longitude = longitude;
        this.latitude = latitude;
        this.venueID = venueID;
        this.setImageLink(imageLink);
    }

    public Venue(String venueTitle, String phoneNumber,
                 String area, String street, int venueImageRes,
                 float longitude, float latitude, String venueID,
                 String imageLink, float venueRating) {
        this.venueTitle = venueTitle;
        this.phoneNumber = phoneNumber;
        this.area = area;
        this.street = street;
        this.venueImageRes = venueImageRes;
        this.longitude = longitude;
        this.latitude = latitude;
        this.venueID = venueID;
        this.imageLink = imageLink;
        this.venueRating = venueRating;
    }

    public Venue(String venueTitle, String phoneNumber, String area, String street,
                 String longitude, String latitude, String venueID) {
        this.venueID = venueID;
        this.phoneNumber = phoneNumber;
        this.area = area;
        this.street = street;
        this.latitude = (float) Double.parseDouble(latitude);
        this.longitude = (float)Double.parseDouble(longitude);
        this.venueTitle = venueTitle;
    }

    public Venue(String venueTitle, String phoneNumber, String area, String street) {
        this.venueTitle = venueTitle;
        this.phoneNumber = phoneNumber;
        this.area = area;
        this.street = street;
    }

    public String getVenueTitle() {
        return venueTitle;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getArea() {
        return area;
    }

    public String getStreet() {
        return street;
    }

    public int getVenueImageRes() {
        return venueImageRes;
    }

    public float getLongitude() {
        return longitude;
    }

    public float getLatitude() {
        return latitude;
    }

    public String getVenueID() {
        return venueID;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }
}
