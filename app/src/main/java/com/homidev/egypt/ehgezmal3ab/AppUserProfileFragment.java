package com.homidev.egypt.ehgezmal3ab;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by User on 18/04/2018.
 */

public class AppUserProfileFragment extends android.support.v4.app.Fragment {

    private EhgezMal3abAPI ehgezMal3abAPI;

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
}
