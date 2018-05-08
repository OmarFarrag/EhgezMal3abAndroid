package com.homidev.egypt.ehgezmal3ab;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.RequiresApi;
import android.widget.Toast;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

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
    private String IP="192.168.1.18";

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
    * This function sends a login request to the server, handles the response and then calls the proper
    * UI message display
    */
    public void loginPlayer(final Player playerToLogin, final LogInFragment loginFragment)
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

                    final String[] userType = new String[1];

                    if(isSuccessful)
                    {
                        String responseString = getLoginStringFromJsonResponse(response);
                        userType[0] = getUserTypeFromStringResponse(responseString);
                        storeUserToken(responseString,userType[0],playerToLogin.getUsername());
                    }

                    handler.post(new Runnable() {
                        public void run() {

                            loginFragment.showLoginResponseMessage(isSuccessful);

                            if(isSuccessful) {
                                mainActivity.loggedIn(userType[0]);

                            }
                        }
                    });
                }


            });
    }

    /*
    * Function that gets the response string from okhttp response object
     */

    protected String getLoginStringFromJsonResponse(Response response)
    {
        String responseString = null;

        try {
            responseString = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return responseString;

    }

    /*
    * Function that gets the userType from the login response
     */

    private String getUserTypeFromStringResponse(String response)
    {
        //Construct the json response then extracts the message
        JSONObject responseJson;
        try {
            response = new JSONObject(response).getJSONObject("user").getString("userType");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return response;
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
        String token = mainActivity.getSharedPreferences("appUserPrefs", MODE_PRIVATE).getString("token", "");
        if (token == "") {
            token = mainActivity.getSharedPreferences("venAdminPrefs", MODE_PRIVATE).getString("token", "");
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
                return;
            }
        });
    }



    public void cancelReservation(final Reservation reservation, final ReservationsFragment fragment)
    {
        EhgezMal3abAPI ehgezMal3abAPI = createEhgezMal3abService();
        String token = mainActivity.getSharedPreferences("appUserPrefs", MODE_PRIVATE).getString("token", "");
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
                fragment.notifyDataChange();
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
                if (response.code() == 200) {
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



    public void getPlayer(final View v, final AppUserProfileFragment fragment) {
        EhgezMal3abAPI ehgezMal3abAPI = createEhgezMal3abService();
        String token = mainActivity.getSharedPreferences("appUserPrefs", MODE_PRIVATE).getString("token", "");
        if (token == "") {
             token = mainActivity.getSharedPreferences("venAdminPrefs", MODE_PRIVATE).getString("token", "");
        }
        final Player[] player = new Player[1];

        ehgezMal3abAPI.getPlayerInfo("Bearer " + token).enqueue(new retrofit2.Callback<Player>() {
            @Override
            public void onResponse(retrofit2.Call<Player> call, retrofit2.Response<Player> response) {
                if (response.code() == 200) {
                    player[0] = response.body();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("player", player[0]);

                    AppUserProfileFragment.setPlayer(player[0]);
                    fragment.setupLayout(v);
                }
                else {
                    player[0] = null;
                }
            }
            @Override
            public void onFailure(retrofit2.Call<Player> call, Throwable t) {

            }
        });
    }




    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void logoutUser(Player user){
        EhgezMal3abAPI ehgezMal3abAPI = createEhgezMal3abService();
        String token = mainActivity.getSharedPreferences("appUserPrefs", MODE_PRIVATE).getString("token", "");
        if (token == "") {
            return;
        }
        ehgezMal3abAPI.logoutUser("Bearer " + token, user.getUsername()).enqueue(new retrofit2.Callback<JsonObject>() {
            @Override
            public void onResponse(retrofit2.Call<JsonObject> call, retrofit2.Response<JsonObject> response) {
                if(response.code() == 400){
                    JsonObject object = response.body();

                }else if(response.code() == 200){

                    mainActivity.logOut();
                    removeUserToken();

                }
            }

            @Override
            public void onFailure(retrofit2.Call<JsonObject> call, Throwable t) {

            }
        });
    }

    /*
     * Gets the time slots of the schedule of a specific pitch for a specific day
     * gets a respones of an array of time slots
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void getPitchSchedule(String venueID, String pitchName, String startsOn, final PitchActivity pitchActivity) {
        EhgezMal3abAPI ehgezMal3abAPI = createEhgezMal3abService();
        String token = mainActivity.getSharedPreferences("appUserPrefs", MODE_PRIVATE).getString("token", "");
        if (token == "") {
            token = mainActivity.getSharedPreferences("venAdminPrefs", MODE_PRIVATE).getString("token", "");
        }

        final TimeSlot[] timeSlots = new TimeSlot[1];
        ehgezMal3abAPI.getPitchSchedule("Bearer " + token, venueID, pitchName, startsOn).enqueue(new retrofit2.Callback<ArrayList<TimeSlot>>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(retrofit2.Call<ArrayList<TimeSlot>> call, retrofit2.Response<ArrayList<TimeSlot>> response) {
                if(response.code() == 400){


                }else if(response.code() == 200){

                    pitchActivity.ShowSchedule(response.body());

                }
            }

            @Override
            public void onFailure(retrofit2.Call<ArrayList<TimeSlot>> call, Throwable t) {

            }
        });
    }

    /*
     * Function called by update info fragment when a player wants to update his info
     * sends the request to the server then notifies the fragment with the response
     */
    public void     updatePlayerInfo(Player player, final UpdateInfoFragment updateInfoFragment)
    {
        EhgezMal3abAPI ehgezMal3abAPI = createEhgezMal3abService();
        String token = mainActivity.getSharedPreferences("appUserPrefs", MODE_PRIVATE).getString("token", "");
        if (token == "") {
            return;
        }


        ehgezMal3abAPI.updatePlayer("Bearer " + token, player).enqueue(new retrofit2.Callback<Player>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(retrofit2.Call<Player> call, retrofit2.Response<Player> response) {
                if (response.code() == 200) {

                    updateInfoFragment.updatedSuccessfully();

                }else{

                    updateInfoFragment.updateError();

                }
            }

            @Override
            public void onFailure(retrofit2.Call<Player> call, Throwable t) {

            }
        });
    }

    /*
     * This function is called by the pitch activity, it sends a reserve request to the server,
     * gets the json response
     */
    public void reserve(Reservation reservation, final PitchActivity pitchActivity)
    {
        EhgezMal3abAPI ehgezMal3abAPI = createEhgezMal3abService();
        String token = mainActivity.getSharedPreferences("appUserPrefs", MODE_PRIVATE).getString("token", "");
        if (token == "") {
            token = mainActivity.getSharedPreferences("venAdminPrefs", MODE_PRIVATE).getString("token", "");
        }
        ehgezMal3abAPI.reserve("Bearer " + token, reservation).enqueue(new retrofit2.Callback<Error>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(retrofit2.Call<Error> call, retrofit2.Response<Error> response) {
                if(response.code() == 200){
                    pitchActivity.successfulReservation();

                }else if(response.code() == 400){
                   // pitchActivity.unsuccessfulReservation();
                    try {
                        pitchActivity.showToasMessage(response.errorBody().string().split("\"")[3]);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(retrofit2.Call<Error> call, Throwable t) {

            }
        });
    }

    /*
     * This function is called by the pitch activity, it places a request by the admin,
     * gets the json response
     */
    public void reserveByAdmin(Reservation reservation, final PitchActivity pitchActivity)
    {
        EhgezMal3abAPI ehgezMal3abAPI = createEhgezMal3abService();
        String token = mainActivity.getSharedPreferences("venAdminPrefs", MODE_PRIVATE).getString("token", "");
        if (token == "") {
            return;
        }
        ehgezMal3abAPI.reserveByAdmin("Bearer " + token, reservation).enqueue(new retrofit2.Callback<Error>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(retrofit2.Call<Error> call, retrofit2.Response<Error> response) {
                if(response.code() == 200){
                    pitchActivity.successfulReservation();

                }else if(response.code() == 400){
                    // pitchActivity.unsuccessfulReservation();
                    try {
                        pitchActivity.showToasMessage(response.errorBody().string().split("\"")[3]);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(retrofit2.Call<Error> call, Throwable t) {

            }
        });
    }

    /*
     * This function is called when the admin accepts a reservation request for one of his pitches
     */
    public void acceptReservation(final ReservationsFragment reservationsFragment, Reservation reservation) {
        EhgezMal3abAPI ehgezMal3abAPI = createEhgezMal3abService();
        String token = mainActivity.getSharedPreferences("venAdminPrefs", MODE_PRIVATE).getString("token", "");

        if (token == "") {
            return;
        }
        ehgezMal3abAPI.acceptReservation("Bearer " + token, reservation).enqueue(new retrofit2.Callback<String>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(retrofit2.Call<String> call, retrofit2.Response<String> response) {
                if (response.code() == 200) {
                    reservationsFragment.showToasMessage(response.body().toString());

                }else {
                    reservationsFragment.showToasMessage(response.body().toString());
                }
                reservationsFragment.notifyDataChange();
            }

            @Override
            public void onFailure(retrofit2.Call<String> call, Throwable t) {

            }
        });
    }


    /*
     * This function is called to send a change password request to the server
     */
    public void changePassword(ChangePasswordRequest requestBody, final ChangePasswordFragment callerFragment)
    {
        EhgezMal3abAPI ehgezMal3abAPI = createEhgezMal3abService();
        String token = mainActivity.getSharedPreferences("appUserPrefs", MODE_PRIVATE).getString("token", "");
        if (token == "") {
            token = mainActivity.getSharedPreferences("venAdminPrefs", MODE_PRIVATE).getString("token", "");;
        }

        ehgezMal3abAPI.changePassword("Bearer " + token, requestBody).enqueue(new retrofit2.Callback<Error>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            public void onResponse(retrofit2.Call<Error> call, retrofit2.Response<Error> response) {
                if (response.code() == 200) {
                    callerFragment.showToastMessage(response.body().getText());
                    callerFragment.returnToParent();
                } else {
                    callerFragment.showToastMessage(mainActivity.getString(R.string.wrongPassword));
                }
            }

            @Override
            public void onFailure(retrofit2.Call<Error> call, Throwable t) {

            }
        });
    }


    /*
     * This function is called when the admin accepts a reservation request for one of his pitches
     */
    public void declineReservation(final ReservationsFragment reservationsFragment, Reservation reservation)
    {
        EhgezMal3abAPI ehgezMal3abAPI = createEhgezMal3abService();
        String token = mainActivity.getSharedPreferences("venAdminPrefs",MODE_PRIVATE).getString("token","");

        if (token == "") {
            return;
        }
        ehgezMal3abAPI.declineReservation("Bearer " +token,reservation).enqueue(new retrofit2.Callback<JsonObject>()
        {
            @RequiresApi(api = Build.VERSION_CODES.N)
            public void onResponse(retrofit2.Call<JsonObject> call, retrofit2.Response<JsonObject> response) {
                if(response.code() == 200){
                    reservationsFragment.showToasMessage(mainActivity.getResources().getString(R.string.reservationDeclined));


                }else {
                    reservationsFragment.showToasMessage(mainActivity.getResources().getString(R.string.error));
                }
                reservationsFragment.notifyDataChange();
            }

            @Override
            public void onFailure(retrofit2.Call<JsonObject> call, Throwable t) {

            }
        });
    }


    public void getMyFriends (String status,final FriendItemAdapter adapter){
        EhgezMal3abAPI ehgezMal3abAPI = createEhgezMal3abService();
        String token = mainActivity.getSharedPreferences("appUserPrefs", MODE_PRIVATE).getString("token", "");
        if (token == "") {
            return;
        }
        ehgezMal3abAPI.getAllFriends("Bearer " + token, status).enqueue(new retrofit2.Callback<ArrayList<Friend>>() {
            @Override
            public void onResponse(retrofit2.Call<ArrayList<Friend>> call, retrofit2.Response<ArrayList<Friend>> response) {
                if (response.code() == 200) {
                    ArrayList<Friend> friends = response.body();
                    adapter.setFriendsList(friends);
                } else if (response.code() == 204) {
                    //Means that there are no friends, and you're all alone.
                    adapter.setFriendsList(null);
                }
            }

            @Override
            public void onFailure(retrofit2.Call<ArrayList<Friend>> call, Throwable t) {

            }
        });
    }

    /*
     * Function that removes the stored token of the user in the shared preferences after logout
     */
    protected void removeUserToken()
    {
        SharedPreferences preferences;

        String appUserToken = mainActivity.getSharedPreferences("appUserPrefs", MODE_PRIVATE).getString("token", "");
        String venAdminToken = mainActivity.getSharedPreferences("venAdminPrefs", MODE_PRIVATE).getString("token", "");

        if( appUserToken != "")
        {
            preferences = mainActivity.getSharedPreferences("appUserPrefs", MODE_PRIVATE);
            preferences.edit().remove("token").commit();
            preferences.edit().remove("username").commit();
        }
        else if (venAdminToken != "")
        {
            preferences = mainActivity.getSharedPreferences("venAdminPrefs", MODE_PRIVATE);
            preferences.edit().remove("token").commit();
            preferences.edit().remove("username").commit();
        }
    }


        public void addFriend (String friendUsername,final Context fragment){
            EhgezMal3abAPI ehgezMal3abAPI = createEhgezMal3abService();
            String token = mainActivity.getSharedPreferences("appUserPrefs", MODE_PRIVATE).getString("token", "");
            if (token == "") {
                return;
            }
            ehgezMal3abAPI.addFriend("Bearer " + token, friendUsername).enqueue(new retrofit2.Callback<JsonObject>() {
                @Override
                public void onResponse(retrofit2.Call<JsonObject> call, retrofit2.Response<JsonObject> response) {
                    if (response.code() == 200) {
                        Toast.makeText(fragment, "Succesfully sent your request", Toast.LENGTH_LONG).show();
                    } else if (response.code() == 400) {
                        try {
                            JSONObject object = new JSONObject(response.errorBody().string());
                            Toast.makeText(fragment, object.get("text").toString(), Toast.LENGTH_LONG).show();
                        } catch (Exception e) {
                        }
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
                .url("http://"+IP+":56718/api/venues")
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
                .url("http://"+IP+":56718/api/users/register")
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
                .url("http://"+IP+":56718/api/token")
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
                .url("http://"+IP+":56718/api/pitches/" + venueID)
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

    protected void storeUserToken(String response, String userType, String username)
    {


        //Construct the json response then extracts the message
        JSONObject responseJson;
        try {
            responseJson = new JSONObject(response);
            response = responseJson.getString("token");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //Store in shared preferences
        if(userType.toLowerCase().equals("appuser")) {
            SharedPreferences preferences = mainActivity.getSharedPreferences("appUserPrefs", MODE_PRIVATE);
            preferences.edit().putString("token", response).commit();
            preferences.edit().putString("username", username).commit();
        }
        else if (userType.toLowerCase().equals("venadmin"))
        {
            SharedPreferences preferences = mainActivity.getSharedPreferences("venAdminPrefs", MODE_PRIVATE);
            preferences.edit().putString("token", response).commit();
            preferences.edit().putString("username", username).commit();
        }
    }


    /*
     * This function is called when a user wants to contact the general admin
     * to be a venue admin
     * This function requests the general admin number from the server
     */
    public void getGeneralAdminPhone(final RegisterFragment registerFragment)
    {
        EhgezMal3abAPI ehgezMal3abAPI = createEhgezMal3abService();

        ehgezMal3abAPI.getGeneralAdminPhone().enqueue(new retrofit2.Callback<Error>()
        {
            @RequiresApi(api = Build.VERSION_CODES.N)
            public void onResponse(retrofit2.Call<Error> call, retrofit2.Response<Error> response) {
                if(response.code() == 200){
                    registerFragment.callGeneralAdmin(response.body().getText());

                }else {
                    registerFragment.showRegisterResponseMessage(response.body().getText());
                }

            }

            @Override
            public void onFailure(retrofit2.Call<Error> call, Throwable t) {

            }
        });
    }
/*
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

    */

    /*
     * This function is called by the venue fragment of the admin to get his venue ID
     */
    public void getVenueByAdminID(final ViewVenueFragment callerFragment)
    {
        EhgezMal3abAPI ehgezMal3abAPI = createEhgezMal3abService();
        String token = mainActivity.getSharedPreferences("venAdminPrefs", MODE_PRIVATE).getString("token", "");
        String username = mainActivity.getSharedPreferences("venAdminPrefs", MODE_PRIVATE).getString("username", "");
        if (token == "") {
            return;
        }
        ehgezMal3abAPI.getVenueByAdminID("Bearer " + token, username).enqueue(new retrofit2.Callback<ArrayList<Venue>>() {
            @Override
            public void onResponse(retrofit2.Call<ArrayList<Venue>> call, retrofit2.Response<ArrayList<Venue>> response) {
                if(response.code() == 200){
                    callerFragment.setVenueID(response.body().get(0).getVenueID(),response.body().get(0).getVenueTitle() );

                }else if(response.code() == 204){
                    //TODO: handle error
                }
            }

            @Override
            public void onFailure(retrofit2.Call<ArrayList<Venue>> call, Throwable t) {
            return;
            }
        });
    }




    protected Request createGetPlayerReservationsRequest()
    {
        return new Request.Builder()
                .url("http://"+IP+":56718/api/reservations")
                .get()
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization","Bearer "+ mainActivity.getSharedPreferences("appUserPrefs",MODE_PRIVATE).getString("token",""))
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
