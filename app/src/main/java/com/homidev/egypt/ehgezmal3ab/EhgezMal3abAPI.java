package com.homidev.egypt.ehgezmal3ab;

import com.google.gson.JsonObject;
import com.homidev.egypt.ehgzemal3ab.notifers.DeviceToken;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.http.*;

public interface EhgezMal3abAPI {

    String BASE_URL = "http://192.168.1.5:56718/api/";

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
    Call<Error> reserve(@Header("Authorization") String token, @Body Reservation reservation);

    @POST("Reservations/insertReservation")
    Call<Error> reserveByAdmin(@Header("Authorization") String token, @Body Reservation reservation);

    @GET("venues/venueByID/{username}")
    Call<ArrayList<Venue>> getVenueByAdminID(@Header("Authorization") String token , @Path("username") String username);

    @PUT("reservations/accept")
    Call<Error> acceptReservation(@Header("Authorization") String token , @Body Reservation reservation);

    @PUT("reservations/decline")
    Call<JsonObject> declineReservation(@Header("Authorization") String token , @Body Reservation reservation);

    @PATCH("users/password/reset")
    Call<Error> changePassword(@Header("Authorization") String token, @Body ChangePasswordRequest requestBody);

   /* @POST("api/Reviews")
    Call<>*/

   @PUT("users/update")
   Call<Player> updatePlayer(@Header("Authorization") String token, @Body Player player);

   @GET("users/generalAdminPhone")
   Call<Error> getGeneralAdminPhone();

   @POST("friends/request")
   Call<JsonObject> addFriend(@Header("Authorization") String token, @Query("friendUsername") String friendUsername);

   @POST("notifications/device")
    Call<JSONObject> registerDevice(@Header("Authorization") String token, @Body DeviceToken deviceToken);

   @GET("friends/request")
    Call<ArrayList<Friend>> getMyFriendRequests(@Header("Authorization") String token, @Query("requestFrom") String query);

   @PATCH("friends")
    Call<Friend> acceptOrDecline(@Header("Authorization") String token,@Body Friend friend, @Query("status") String query);

   @POST("/api/reviews")
    Call<Double> submitReview(@Header("Authorization") String token, @Body PlayerSubmitReview playerSubmitReview);

   @PUT("venues/{id}")
    Call<Error> updateVenueInfo(@Header("Authorization") String token, @Path("id") String venueID, @Body Venue venue);
}
