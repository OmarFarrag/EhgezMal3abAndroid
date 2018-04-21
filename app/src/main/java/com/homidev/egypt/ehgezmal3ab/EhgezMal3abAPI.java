package com.homidev.egypt.ehgezmal3ab;

import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.*;

public interface EhgezMal3abAPI {

    String BASE_URL = "http://10.0.2.2:56719/api/";

    @GET("venues/")
    Call<List<Venue>> getAllVenues();

    @GET("reservations/")
    Call<ArrayList<Reservation>> getMyReservations(@Header("Authorization") String token);

    @GET("pitches/{id}")
    Call<ArrayList<Pitch>> getVenuePitches(@Path("id") String id);

    @PUT("reservations/cancel")
    Call<Error> cancelReservation(@Header("Authorization") String token, @Body Reservation reservation);

    @PATCH("reservations/sharelink")
    Call<JsonObject> getReservationShareLink(@Body Reservation reservation);

    @GET("users/")
    Call<Player> getPlayerInfo(@Header("Authorization") String token);

    @GET("token/logout")
    Call<JsonObject> logoutUser(@Header("Authorization") String token, @Query("username") String username);

}
