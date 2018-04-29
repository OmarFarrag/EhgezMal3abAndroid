package com.homidev.egypt.ehgezmal3ab;

import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.*;

public interface EhgezMal3abAPI {

    String BASE_URL = "http://192.168.1.3:56718/api/";

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

    @GET("friends")
    Call<ArrayList<Friend>> getAllFriends(@Header("Authorization") String token, @Query("status") String status);

    @GET ("Reservations/pitchSchedule/{VenueID}/{pitch}/{start}")
    Call<ArrayList<TimeSlot>> getPitchSchedule (@Header("Authorization") String token, @Path("VenueID") String venueID,
                                            @Path("pitch") String pitchName, @Path("start") String startDate);

    @POST("Reservations")
    Call<JsonObject> reserve(@Header("Authorization") String token, @Body Reservation reservation);

    @GET("venues/venueByID/{username}")
    Call<ArrayList<Venue>> getVenueByAdminID(@Header("Authorization") String token , @Path("username") String username);
}
