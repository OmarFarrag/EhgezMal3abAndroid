package com.homidev.egypt.ehgezmal3ab;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.FrameLayout;


public class VenueAdminMainActivity extends AppCompatActivity {

    FrameLayout adminMainFrame;
    BottomNavigationView adminBNV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.venue_admin_main_activity);

        adminMainFrame = findViewById(R.id.adminMainFrame);

        initAdminBNV();


        ConnectionManager.getConnectionManager().setVenueAdminMainActivity(this);



    }

    /*
   Initialize the main bottom navigation bar with the fragment layout
    */
    protected void initAdminBNV()
    {


        adminBNV = (BottomNavigationView) findViewById(R.id.adminBNV);

        adminBNV.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                android.support.v4.app.Fragment selectedFragment=null;

                switch (item.getItemId())
                {
                    case R.id.myVenueBNVItem:
                        selectedFragment = new ViewVenueFragment();
                        break;

                    case R.id.reservationsBNVItem:
                        selectedFragment = new ReservationsFragment();
                        break;

                    case R.id.userProfileBNVItem:
                        selectedFragment = new AppUserProfileFragment();
                        break;

                }

                android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.adminMainFrame,selectedFragment);
                transaction.commit();
                return true;
            }
        });

        //Manually displaying the first fragment - one time only
        android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.adminMainFrame, new ViewVenueFragment());
        transaction.commit();
    }
}
