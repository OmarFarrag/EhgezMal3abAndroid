package com.homidev.egypt.ehgezmal3ab;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;


public class VenueAdminMainActivity extends AppCompatActivity {

    FrameLayout adminMainFrame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.venue_admin_main_activity);

        adminMainFrame = findViewById(R.id.adminMainFrame);

        android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.adminMainFrame,new ViewVenueFragment());
        transaction.commit();




    }
}
