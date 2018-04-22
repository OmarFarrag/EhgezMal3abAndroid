package com.homidev.egypt.ehgezmal3ab;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

/**
 * Created by User on 18/04/2018.
 */

public class AppUserProfileFragment extends android.support.v4.app.Fragment {

    private Toolbar menuToolBar;

    private static ConnectionManager connectionManager = ConnectionManager.getConnectionManager();
    private EhgezMal3abAPI ehgezMal3abAPI = connectionManager.createEhgezMal3abService();
    private static Player player;

    public static void setPlayer(Player player) {
        AppUserProfileFragment.player = player;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        connectionManager.getPlayer();
        View userProfileView = inflater.inflate(R.layout.app_user_profile, container, false);
        setupLayout(userProfileView);
        setListeners(userProfileView);
        try {
            setPlayer((Player) getArguments().getSerializable("player"));
        }catch (NullPointerException e) {
            e.printStackTrace();
        }
        return userProfileView;
    }

    public static void getUser() {
        connectionManager.getPlayer();
    }
    /*public void saveUserInfo(View view) {
        SharedPreferences sharedPref = getActivity().getSharedPreferences("appUserPrefs", Context.MODE_PRIVATE);
        String token = sharedPref.getString("token", "");
        if(token == "") {
            return;
        }
    }*/

    public void setToolbar(Toolbar mainToolBar) {
        this.menuToolBar = mainToolBar;
    }

    public void setupLayout(View view) {
        if(player != null) {
            TextView welcomeFullName = view.findViewById(R.id.userProfileWelcome);
            TextView balance = view.findViewById(R.id.userProfileBalance);
            TextView phoneNumber = view.findViewById(R.id.userProfilePhone);
            TextView email = view.findViewById(R.id.userProfileEmail);
            welcomeFullName.setText("Hey, " + player.getName());
            balance.setText(player.getBalance());
            email.setText(player.getEmail());
            phoneNumber.setText(player.getNumber());
        }
    }

    private void setListeners(View view) {

        View.OnClickListener updateInfoListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        };

        View.OnClickListener resetPassListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        };

        View.OnClickListener viewFriendsListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                connectionManager.getMyFriends("Pending");
            }
        };

        View.OnClickListener logoutListener = new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("appUserPrefs", Context.MODE_PRIVATE);
                sharedPreferences.edit().remove("token");
                Toast.makeText(getContext(), "logged out", Toast.LENGTH_SHORT);
                connectionManager.logoutUser(player);
            }
        };

        CardView updateInfo = view.findViewById(R.id.updateInfoCV);
        CardView resetPass = view.findViewById(R.id.resetPassCV);
        CardView viewFriends = view.findViewById(R.id.friendsCV);
        CardView logout = view.findViewById(R.id.logoutCV);
        updateInfo.setOnClickListener(updateInfoListener);
        resetPass.setOnClickListener(resetPassListener);
        viewFriends.setOnClickListener(viewFriendsListener);
        logout.setOnClickListener(logoutListener);
    }
}


