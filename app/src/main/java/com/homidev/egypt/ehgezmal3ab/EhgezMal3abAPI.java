package com.homidev.egypt.ehgezmal3ab;

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

}
