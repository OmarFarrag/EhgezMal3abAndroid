package com.homidev.egypt.ehgezmal3ab;

/**
 * Created by engineer on 27/03/18.
 * model class for Venue
 */

public class Venue {
    String venueTitle;
    String phoneNumber;
    String area;
    String street;
    int venueImageRes;
    float longitude;
    float latitude;
    String venueID;

    public Venue(String venueTitle, String phoneNumber,
                 String area, String street, int venueImageRes,
                 float longitude, float latitude, String venueID) {
        this.venueTitle = venueTitle;
        this.phoneNumber = phoneNumber;
        this.area = area;
        this.street = street;
        this.venueImageRes = venueImageRes;
        this.longitude = longitude;
        this.latitude = latitude;
        this.venueID = venueID;
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
}
