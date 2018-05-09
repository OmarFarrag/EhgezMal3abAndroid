package com.homidev.egypt.ehgzemal3ab.notifers;

import android.content.Context;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import com.homidev.egypt.ehgezmal3ab.EhgezMal3abAPI;
import com.homidev.egypt.ehgezmal3ab.ConnectionManager;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;

public class Notifier extends FirebaseInstanceIdService {

    private Context context;

    public Notifier()
    {
        context = getApplicationContext();
    }

    public  Notifier(Context context)
    {
        super();
        this.context = context;
    }

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(refreshedToken);
    }


    public void sendRegistrationToServer(String refreshedToken)
    {
        EhgezMal3abAPI api = ConnectionManager.getConnectionManager().createEhgezMal3abService();
        String token = context.getSharedPreferences("appUserPrefs", MODE_PRIVATE).getString("token","");
        if (token == "") {
            return;
        }
        DeviceToken deviceToken = new DeviceToken();
        deviceToken.setDevicetoken(refreshedToken);
        api.registerDevice("Bearer " + token, deviceToken).enqueue(new Callback<JSONObject>() {
            @Override
            public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {
                if(response.code() == 200)
                {
                    Log.d(TAG, "Sent this successfully.");
                }
            }

            @Override
            public void onFailure(Call<JSONObject> call, Throwable t) {
                Log.d(TAG, "Cannot send your token");
            }
        });
    }
}
