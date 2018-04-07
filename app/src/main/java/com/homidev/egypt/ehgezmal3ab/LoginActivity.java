package com.homidev.egypt.ehgezmal3ab;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    RecyclerView allVenuesRecyclerView;
    RecyclerView.Adapter recyclerAdapter;
    ConnectionManager connectionManager;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Create connection manager
        connectionManager = ConnectionManager.getConnectionManager();

        //Check if there is internet connection
        if (!connectionManager.isConnectedToInternet())
        {
            setContentView(R.layout.no_internet_connection);
            return;
        }


        // Set the content of the activity to use the activity_main.xml layout file
        setContentView(R.layout.welcome_layout);

        //initialize recycler view
        allVenuesRecyclerView = findViewById(R.id.recyclerView);

        //set fixed size for recycler view
        allVenuesRecyclerView.setHasFixedSize(true);
        //Layout manager is responsible for positioning item views (venues for now) within the allVenuesRecyclerView
        allVenuesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        //create an adapter, automatically fires a GET request to get all venues(for now)
        recyclerAdapter = new VenueItemAdapter(this);
        //set VenueItemAdapter to adapt allVenuesRecyclerView for displaying the venues(for now)
        allVenuesRecyclerView.setAdapter(recyclerAdapter);

        // Find the view pager that will allow the user to swipe between fragments
        ViewPager loginAndRegisterViewPager = (ViewPager) findViewById(R.id.loginViewpager);

        // Create an adapter that knows which fragment should be shown on each page
        LoginAndRegisterAdapter adapter = new LoginAndRegisterAdapter( getSupportFragmentManager());

        // Set the adapter onto the view pager
        loginAndRegisterViewPager.setAdapter(adapter);

        // Find the tab layout that shows the tabs
        TabLayout tabLayout = (TabLayout) findViewById(R.id.loginRegisterTab);

        tabLayout.setupWithViewPager(loginAndRegisterViewPager);

        setListeners();

    }

    protected void setListeners () {
        closeLoginAndRegisterBtnListener();
        registerBtnListener();
        loginBtnListener();
    }


    protected void closeLoginAndRegisterBtnListener()
        {
            final Context root = getBaseContext();

            ImageButton closeButton = (ImageButton) findViewById(R.id.closeLoginAndRegisterBtn);

            closeButton.setOnClickListener(new View.OnClickListener()
            {
                public void onClick(View v)
                {
                    RelativeLayout logoLayout =  (RelativeLayout) findViewById(R.id.logoLayout);
                    logoLayout.setVisibility(View.INVISIBLE);

                    final LinearLayout loginAndRegisterLayout =  (LinearLayout) findViewById(R.id.loginAndRegisterLayout);

                    Animation animation = AnimationUtils.loadAnimation(root, R.anim.slide_down);
                    //use this to make it longer:  animation.setDuration(1000);
                    animation.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {}

                        @Override
                        public void onAnimationRepeat(Animation animation) {}

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            loginAndRegisterLayout.setVisibility(View.INVISIBLE);
                        }
                    });

                    loginAndRegisterLayout.startAnimation(animation);
                }
            });

    }


    protected void attachShowLoginAndRegister(int btnNo)
    {
        Button btn=null;

        if(btnNo==0){ btn = (Button)findViewById(R.id.registerBtn);}
        else if (btnNo==1){ btn = (Button)findViewById(R.id.loginBtn);}

        btn.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                final LinearLayout loginAndRegisterLayout =  (LinearLayout) findViewById(R.id.loginAndRegisterLayout);
                final RelativeLayout logoLayout =  (RelativeLayout) findViewById(R.id.logoLayout);

                loginAndRegisterLayout.setVisibility(View.VISIBLE);

                TranslateAnimation slideUpAnimation = createSlideUpAnimation(loginAndRegisterLayout);

                loginAndRegisterLayout.startAnimation(slideUpAnimation);

                logoLayout.setVisibility(View.VISIBLE);




            }
        });
    }

    protected void registerBtnListener()
    {
        attachShowLoginAndRegister(0);
    }

    protected void loginBtnListener()
    {
        attachShowLoginAndRegister(1);
    }

    protected TranslateAnimation createSlideUpAnimation(View animatedLayout)
    {
        TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
                animatedLayout.getHeight(),  // fromYDelta
                0);                // toYDelta
        animate.setDuration(500);
        animate.setFillAfter(true);
        return animate;
    }


}