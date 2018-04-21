package com.homidev.egypt.ehgezmal3ab;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toolbar;

/**
 * Created by User on 18/04/2018.
 */

public class AppUserProfileFragment extends android.support.v4.app.Fragment {

    private Toolbar menuToolBar;

    private ConnectionManager connectionManager = ConnectionManager.getConnectionManager();
    private EhgezMal3abAPI ehgezMal3abAPI = connectionManager.createEhgezMal3abService();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View UserProfileView = inflater.inflate(R.layout.app_user_profile, container, false);
        return UserProfileView;
    }

    private void getUser() {

        Context context = getActivity();
        SharedPreferences sharedPref = context.getSharedPreferences(
                getString(R.string.PREFERENCE_FILE_KEY), Context.MODE_PRIVATE);

        sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);


    }

    public void saveUserInfo(View view) {
        SharedPreferences sharedPref = getActivity().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
    }

    public void setToolbar(Toolbar mainToolBar) {
        this.menuToolBar = mainToolBar;
    }

    public void setupLayout(View view, Player player) {
        TextView welcomeFullName = view.findViewById(R.id.userProfileWelcome);
        TextView balance = view.findViewById(R.id.userProfileBalance);
        TextView phoneNumber = view.findViewById(R.id.userProfilePhone);
        TextView email = view.findViewById(R.id.userProfileEmail);
        welcomeFullName.setText("Hey, " + player.getName());
        balance.setText(player.getBalance());
        email.setText(player.getEmail());
        phoneNumber.setText(player.getNumber());
    }

    private void setListeners() {

    }
}


