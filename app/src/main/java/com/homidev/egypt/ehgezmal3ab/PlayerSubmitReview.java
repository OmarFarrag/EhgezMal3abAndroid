package com.homidev.egypt.ehgezmal3ab;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by engineer on 08/05/18.
 */

public class PlayerSubmitReview {
    @SerializedName("ReviewingPlayer")
    String reviewingPlayer;

    @SerializedName("VenueID")
    String venueID;

    @SerializedName("PitchName")
    String pitchName;

    @SerializedName("Rating")
    float rating;

    public PlayerSubmitReview(String reviewingPlayer, String venueID, String pitchName, float rating) {
        this.reviewingPlayer = reviewingPlayer;
        this.venueID = venueID;
        this.pitchName = pitchName;
        this.rating = rating;
    }
}
