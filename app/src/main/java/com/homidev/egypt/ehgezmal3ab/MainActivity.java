package com.homidev.egypt.ehgezmal3ab;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    ConnectionManager connectionManager;
    ViewPager loginAndRegisterViewPager;
    LoginAndRegisterAdapter logRegAdapter;
    TabLayout logRegTab;
    BottomNavigationView mainBNV;


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Create connection manager
        connectionManager = ConnectionManager.getConnectionManager();

        //Check if there is internet connection
       /*if (!connectionManager.isConnectedToInternet())
        {
            setContentView(R.layout.no_internet_connection);
            return;
        }*/


        // Set the content of the activity to use the activity_main.xml layout file
        setContentView(R.layout.welcome_layout);

        initMainBNV();

        initializeLoginAndRegisterPage();

        setListeners();

        setWindowSlideUpOnKeyboard();

        connectionManager.setMainActivity(this);

    }

    /*
    This function calls the listeners setters for all the interactive components in the main
    activity
     */

    protected void setListeners () {
        closeLoginAndRegisterBtnListener();
        registerBtnListener();
        loginBtnListener();
    }


    /*
    This function sets the action that happens after clicking on the close button of
    the login and register layout
     */
    protected void closeLoginAndRegisterBtnListener()
        {
            ImageButton closeButton = (ImageButton) findViewById(R.id.closeLoginAndRegisterBtn);

            closeButton.setOnClickListener(new View.OnClickListener()
            {
                public void onClick(View v)
                {
                    closeLoginAndRegister();

                }
            });

    }


    /*
    This function attaches the action of showing login/register layout to both register and login
    button of the main activity
     */
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

    /*
    Sets the listener for the register button of the main activity by calling the attach function
     */
    protected void registerBtnListener()
    {
        attachShowLoginAndRegister(0);
    }

    /*
    Sets the listener for the login button of the main activity by calling the attach function
     */
    protected void loginBtnListener()
    {
        attachShowLoginAndRegister(1);
    }

    /*
    returns a full slide up animation
     */
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




    /*
    This function initializes the login/register layout by initiating the viewpager, adapter and tab,
    then links the three together
     */
    protected void initializeLoginAndRegisterPage()
    {
        loginAndRegisterViewPager = (ViewPager) findViewById(R.id.loginViewpager);

        // Create an adapter that knows which fragment should be shown on each page
        logRegAdapter = new LoginAndRegisterAdapter( getSupportFragmentManager());

        loginAndRegisterViewPager.setAdapter(logRegAdapter );

        logRegTab = (TabLayout) findViewById(R.id.loginRegisterTab);

        logRegTab.setupWithViewPager(loginAndRegisterViewPager);
    }

    /*
    Initialize the window slide up when keyboard is visible
     */

    protected void setWindowSlideUpOnKeyboard()
    {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }

    /*
    Close the login and register fragments by sliding down
     */
    public void closeLoginAndRegister()
    {
        final Context root = getBaseContext();

        RelativeLayout logoLayout =  (RelativeLayout) findViewById(R.id.logoLayout);
        logoLayout.setVisibility(View.INVISIBLE);

        final LinearLayout loginAndRegisterLayout =  (LinearLayout) findViewById(R.id.loginAndRegisterLayout);

        Animation animation = AnimationUtils.loadAnimation(root, R.anim.slide_down);

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

    /*
    A function called by the connection manager in case of successful login
     */
    public void loggedIn()
    {
        discardLoginAndRegisterButtons();

        showMainBNV();
    }


    /*
    Shows the main bottom navigation bar
     */
    private void showMainBNV()
    {
        mainBNV.setVisibility(View.VISIBLE);
    }


    /*
    Discard login/register buttons after login
    */
    private void discardLoginAndRegisterButtons()
    {
        LinearLayout loginAndRegisterBottom = (LinearLayout) findViewById(R.id.loginAndRegisterBottom);
        loginAndRegisterBottom.setVisibility(View.INVISIBLE);
    }

    /*
    Initialize the main bottom navigation bar with the fragment layout
     */
    protected void initMainBNV()
    {
        mainBNV = (BottomNavigationView) findViewById(R.id.mainBNV);
        mainBNV.setOnNavigationItemReselectedListener(new BottomNavigationView.OnNavigationItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull MenuItem item) {

                android.support.v4.app.Fragment selectedFragment=null;

                switch (item.getItemId())
                {
                    case R.id.allVenuesBNVItem:
                        selectedFragment = new AllVenuesFragment();
                        break;
                }

                android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.mainFrameLayout,selectedFragment);
                transaction.commit();
                return ;
            }
        });

        //Manually displaying the first fragment - one time only
        android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.mainFrameLayout, new AllVenuesFragment());
        transaction.commit();
    }




}