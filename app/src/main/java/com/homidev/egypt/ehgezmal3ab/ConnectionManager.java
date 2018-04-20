package com.homidev.egypt.ehgezmal3ab;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.prefs.Preferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.Context.CLIPBOARD_SERVICE;
import static android.content.Context.MODE_PRIVATE;

public class ConnectionManager {

    private static ConnectionManager instance = null;
    private OkHttpClient connectionClient;
    private MainActivity mainActivity;

    //private constructor to implement a singleton pattern, initiates the connection client
    private ConnectionManager()
    {
        connectionClient = new OkHttpClient();
    }

    //The public interface for getting the connection manager
    public static ConnectionManager getConnectionManager()
    {
        if ( instance==null)
        {
            instance = new ConnectionManager();
        }
        return instance;
    }


    /*
    This function sends a register request to the server, handles the response and return the proper
    message
     */
    public String registerPlayer(Player player)
    {

        JSONObject playerJson = createJsonToRegisterPlayer(player);

        RequestBody registerPlayerRequestBody = createPlayerRequestBody(playerJson);

        final Request registerPlayerRequest = createRegisterPlayerRequest(registerPlayerRequestBody);

        //This is to hack the final-assigning-innerclass problem
        final Response[] response = new Response[1];
        final String[] responseString = new String[1];



        //Run the request on another thread
        Thread registerThread = new Thread(new Runnable() {
            public void run() {

                try {
                    response[0] = connectionClient.newCall(registerPlayerRequest).execute();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if(response[0].code()==201){
                    responseString[0] = "You have registered successfully";
                    return;
                }

                //Get the response's text
                try {
                    responseString[0] = response[0].body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                //Construct the json response then extracts the message
                JSONObject responseJson;
                try {
                    responseJson = new JSONObject(responseString[0]);
                    responseString[0] = responseJson.getString("text");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

        registerThread.start();

        //join the thread on the UI, to get the response
        try {
            registerThread.join();
        }
        catch(InterruptedException e) {
            e.printStackTrace();
        }


        return responseString[0];


    }



    /*
    This function sends a login request to the server, handles the response and then calls the proper
    UI message display
    */
    public void loginPlayer(Player playerToLogin,final LogInFragment loginFragment)
    {
        JSONObject playerTologinJson = createJsonToLoginPlayer(playerToLogin);

        RequestBody loginPlayerRequestBody = createPlayerRequestBody(playerTologinJson);

        final Request loginPlayerRequest = createLoginPlayerRequest(loginPlayerRequestBody);

        connectionClient.newCall(loginPlayerRequest).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();

            }

            @Override
            public void onResponse(Call call, final Response response) {

                    int code = response.code();

                    //temps is used to hack the final-assignment-innerClass problem
                    boolean temp;

                temp = code == 200;

                    final boolean isSuccessful = temp;


                    //The handler refers to the main loop (UI), as the UI must be updated
                    //On UI thread only
                    Handler handler = new Handler(Looper.getMainLooper());

                    if(isSuccessful) storeUserToken(response);

                    handler.post(new Runnable() {
                        public void run() {

                            loginFragment.showLoginResponseMessage(isSuccessful);

                            if(isSuccessful) {
                                mainActivity.loggedIn();

                            }
                        }
                    });
                }


            });
    }


    /*
    This function creates a JSON object of a player who wants to login
     */
    protected JSONObject createJsonToLoginPlayer(Player playerToLogin)
    {
        JSONObject playerJson = new JSONObject();

        try {
            playerJson.put("userName", playerToLogin.getUsername());
            playerJson.put("password", playerToLogin.getPassword());
        }catch (JSONException e)
        {
            e.printStackTrace();
        }
        return playerJson;
    }



    /*
    This function creates a JSON object of a player who wants to register
     */
    protected JSONObject createJsonToRegisterPlayer(Player player)
    {
        JSONObject playerJson = new JSONObject();

        try {
            playerJson.put("userName", player.getUsername());
            playerJson.put("password", player.getPassword());
            playerJson.put("email", player.getEmail());
            playerJson.put("phoneNumber", player.getNumber());
            playerJson.put("name", player.getName());

        }catch (JSONException e)
        {
            e.printStackTrace();
        }
        return playerJson;
    }

    //fires HTTP GET request to get all venues, returns it in an ArrayList<Venue>
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    protected List<Venue> getAllVenues () {

        //assigned as final to access inner class (Thread)
        //the list that will be returned.
        final List<Venue> venuesList = new ArrayList<>();
        //create a GET request to get all venues
        final Request getVenuesRequest = createGetAllVenueRequest();
        //store the response here for further processing
        final Response[] response = new Response[1];

        //make the GET request, parsing of JSON string, and adding venues to the list in a separate thread
        Thread getAllVenuesThread = new Thread(new Runnable() {
            public void run() {

                try {
                    //execute the request and store it in response[0]
                    response[0] = connectionClient.newCall(getVenuesRequest).execute();
                try {
                    //parsing the JSONArray returned
                    JSONArray venuesResponse = null;
                    try {
                        venuesResponse = new JSONArray(response[0].body().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    //parse each JSONObject in the JSONArray and add it to the venueList
                    for(int i = 0; i < venuesResponse.length(); i++) {
                        JSONObject venueObject = venuesResponse.getJSONObject(i);
                        venuesList.add(new Venue(
                                venueObject.getString("venueName"),
                                venueObject.getString("phoneNumber"),
                                venueObject.getString("area"),
                                venueObject.getString("street"),
                                venueObject.getString("longitude"),
                                venueObject.getString("lattitude"),
                                venueObject.getString("venueID")
                        ));
                    }
                }catch(JSONException e) {
                    e.printStackTrace();
                }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

        });

        //start running the thread
        getAllVenuesThread.start();

        try {
            //await main thread to join this thread to be able to update interface with data retrieve
            getAllVenuesThread .join();
        }
        catch(InterruptedException e) {
            e.printStackTrace();
        }

        return venuesList;
    }

    public void getVenuePitches(String id, final PitchItemAdapter adapter){
        EhgezMal3abAPI ehgezMal3abAPI = createEhgezMal3abService();
        final ArrayList<Pitch>[] pitchList = new ArrayList[1];
        ehgezMal3abAPI.getVenuePitches(id).enqueue(new retrofit2.Callback<ArrayList<Pitch>>(){

            @Override
            public void onResponse(retrofit2.Call<ArrayList<Pitch>> call, retrofit2.Response<ArrayList<Pitch>> response) {
                if(response.code() == 200){
                    pitchList[0] = response.body();
                    adapter.setPitchList(pitchList[0]);
                }else{
                    adapter.setPitchList(new ArrayList<Pitch>());
                }
            }

            @Override
            public void onFailure(retrofit2.Call<ArrayList<Pitch>> call, Throwable t) {

            }
        });
    }



    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void getReservations(final ReservationItemAdapter adapter) {
        EhgezMal3abAPI ehgezMal3abAPI = createEhgezMal3abService();
        String token = mainActivity.getSharedPreferences("myprefs", MODE_PRIVATE).getString("token", "");
        if (token == "") {
            return;
        }
        final ArrayList<Reservation>[] reservationsList = new ArrayList[1];
        ehgezMal3abAPI.getMyReservations("Bearer " + token).enqueue(new retrofit2.Callback<ArrayList<Reservation>>() {
            @Override
            public void onResponse(retrofit2.Call<ArrayList<Reservation>> call, retrofit2.Response<ArrayList<Reservation>> response) {
                if (response.isSuccessful()) {
                    reservationsList[0] = response.body();
                    adapter.setReservationList(reservationsList[0]);
                } else if (response.code() == 401) {
                    reservationsList[0] = null;
                }
            }

            @Override
            public void onFailure(retrofit2.Call<ArrayList<Reservation>> call, Throwable t) {

            }
        });
    }

    public void cancelReservation(final Reservation reservation, final ReservationsFragment fragment)
    {
        EhgezMal3abAPI ehgezMal3abAPI = createEhgezMal3abService();
        String token = mainActivity.getSharedPreferences("myprefs", MODE_PRIVATE).getString("token", "");
        if (token == "") {
            return;
        }
        ehgezMal3abAPI.cancelReservation("Bearer " + token, reservation).enqueue(new retrofit2.Callback<Error>() {
            @Override
            public void onResponse(retrofit2.Call<Error> call, retrofit2.Response<Error> response) {
                boolean cancelled = false;
                if(response.code() == 200){
                    //reservation is cancelled.
                    cancelled = true;
                }else if(response.code() == 400){
                    //reservation is not cancelled.
                    cancelled = false;
                }
                Error text = response.body();
                fragment.showToasts(text, cancelled);
            }

            @Override
            public void onFailure(retrofit2.Call<Error> call, Throwable t) {

            }
        });
    }


    public void getReservationShareLink(final Reservation reservation)
    {
        EhgezMal3abAPI ehgezMal3abAPI = createEhgezMal3abService();
        ehgezMal3abAPI.getReservationShareLink(reservation).enqueue(new retrofit2.Callback<JsonObject>() {
            @Override
            public void onResponse(retrofit2.Call<JsonObject> call, retrofit2.Response<JsonObject> response) {
                if(response.code() == 200)
                {
                    ClipboardManager clipboard = (ClipboardManager) mainActivity.getSystemService(CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("Share reservation link", response.body().get("link").getAsString());
                    clipboard.setPrimaryClip(clip);
                }
            }

            @Override
            public void onFailure(retrofit2.Call<JsonObject> call, Throwable t) {

            }
        });
    }

    /*
    Create a request body for a player from a JSON player object
     */
    protected RequestBody createPlayerRequestBody(JSONObject playerJson)
    {
        //Standard way of constructing the request body
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON,playerJson.toString());
        return body;
    }


    //creates a GET HTTP request to retrieve all venues.
    protected Request createGetAllVenueRequest() {
        return new Request.Builder()
                .url("http://192.168.1.105:56718/api/venues")
                .get()
                .addHeader("Content-Type", "application/json")
                .build();
    }

    /*
    Create a player register request from a register request body
     */
    protected Request createRegisterPlayerRequest(RequestBody registerRequestBody)
    {
        //constructing the request
        return  new Request .Builder()
                .url("http://10.0.2.2:56718/api/users/register")
                .post(registerRequestBody)
                .build();
    }

    /*
    Create a player login request from a register request body
     */
    protected Request createLoginPlayerRequest(RequestBody loginRequestBody)
    {
        //constructing the request
        return  new Request .Builder()
                .url("http://10.0.2.2:56718/api/token")
                .post(loginRequestBody)
                .build();
    }

    /*
    A function that checks if the application is connected to the internet by calling a dummy
    api and check if the connection was successful
    */
    public boolean isConnectedToInternet() {

        //Run the check on another thread
        final boolean isCOnnected[] = new boolean[1];

        Thread t = new Thread(new Runnable() {
            public void run() {
                try {
                    InetAddress ipAddr = InetAddress.getByName("google.com");
                    isCOnnected[0] = (!ipAddr.equals(""));
                } catch (Exception e) {
                    isCOnnected[0] = false;
                }
            }
        });

        t.start();
        try {
            t.join();
        }
        catch(InterruptedException e) {
            e.printStackTrace();
        }


        return isCOnnected[0];
    }

    protected Request createGetPitchesRequest(int venueID) {
        return new Request.Builder()
                .url("http://192.168.1.105:56718/api/pitches/" + venueID)
                .get()
                .build();
    }

    /*
   gets reference to the main activity
    */
    public void setMainActivity(MainActivity f_mainActivity)
    {
        mainActivity = f_mainActivity;
    }


    /*
    Gets the token from the login response and store it
     */

    protected void storeUserToken(Response response)
    {
        String responseString=null;
        try {
            responseString = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Construct the json response then extracts the message
        JSONObject responseJson;
        try {
            responseJson = new JSONObject(responseString);
            responseString = responseJson.getString("token");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //Store in shared preferences
        SharedPreferences preferences = mainActivity.getSharedPreferences("myprefs",MODE_PRIVATE);
        preferences.edit().putString("token", responseString).commit();
    }

    public List<Reservation> getPlayerReservations()
    {
        final List<Reservation> reservationsList = new ArrayList<>();

        final Request getPlayerReservationsRequest = createGetPlayerReservationsRequest();

        final Response[] response = new Response[1];
        Thread getPitchesThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //execute the request and store it in response[0]
                    response[0] = connectionClient.newCall(getPlayerReservationsRequest ).execute();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    //parsing the JSONArray returned
                    JSONArray reservationsResponse = null;
                    try {
                        reservationsResponse  = new JSONArray(response[0].body().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    //parse each JSONObject in the JSONArray and add it to the venueList
                    for(int i = 0; i < reservationsResponse .length(); i++) {
                        JSONObject reservationObject = reservationsResponse.getJSONObject(i);
                        reservationsList.add(new Reservation(
                                reservationObject.getString("username"),
                                reservationObject.getString("startsOn"),
                                reservationObject.getString("endsOn"),
                                reservationObject.getString("venueID"),
                                reservationObject.getString("pitchName"),
                                reservationObject.getString("status")


                        ));
                    }
                }catch(JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        getPitchesThread.start();

        try {
            //await main thread to join this thread to be able to update interface with data retrieve
            getPitchesThread.join();
        }
        catch(InterruptedException e) {
            e.printStackTrace();
        }

        return reservationsList;
    }

    protected Request createGetPlayerReservationsRequest()
    {
        return new Request.Builder()
                .url("http://192.168.1.105:56718/api/reservations")
                .get()
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization","Bearer "+ mainActivity.getSharedPreferences("myprefs",MODE_PRIVATE).getString("token",""))
                .build();
    }

    public EhgezMal3abAPI createEhgezMal3abService(){
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(EhgezMal3abAPI.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        return retrofit.create(EhgezMal3abAPI.class);

    }

}
