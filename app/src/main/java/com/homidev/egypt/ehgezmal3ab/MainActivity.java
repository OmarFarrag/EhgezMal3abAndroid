package com.homidev.egypt.ehgezmal3ab;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentTransaction;

import com.google.firebase.iid.FirebaseInstanceId;
import com.homidev.egypt.ehgzemal3ab.notifers.Notifier;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.ToolbarWidgetWrapper;
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
        SharedPreferences preferences = getSharedPreferences("venAdminPrefs", MODE_PRIVATE);
        preferences.edit().remove("token").commit();
        preferences.edit().remove("username").commit();
        preferences = getSharedPreferences("appUserPrefs", MODE_PRIVATE);
        preferences.edit().remove("token").commit();
        preferences.edit().remove("username").commit();
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

        String appUserToken = getSharedPreferences("appUserPrefs",MODE_PRIVATE).getString("token","");
        if(appUserToken!="") loggedIn("appuser");

        String venAdminToken = getSharedPreferences("venAdminPrefs",MODE_PRIVATE).getString("token","");
        if(venAdminToken!="") loggedIn("venAdmin");
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

    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
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
    public void loggedIn(String userType)
    {
        if(userType.toLowerCase().equals("venadmin"))
        {
            launchAdminMainActivity();
            finish();
            return;
        }
        discardLoginAndRegisterButtons();
        String token = FirebaseInstanceId.getInstance().getToken();
        new Notifier(this).sendRegistrationToServer(token);

        showMainBNV();

        closeLoginAndRegister();
    }

    /*
    * Launches the main activity of the venue admin
     */
    protected void launchAdminMainActivity()
    {
        Intent intent = new Intent(this, VenueAdminMainActivity.class);
       // intent.putExtra("venueID", venueID);
        startActivity(intent);
    }


    /*
    Shows the main bottom navigation bar
     */
    private void showMainBNV()
    {
        mainBNV.setVisibility(View.VISIBLE);
    }

    /*
     * Hides the main bottom navigation bar
     */
    private void hideMainBNV() {mainBNV.setVisibility(View.GONE);}


    /*
    Discard login/register buttons after login
    */
    private void discardLoginAndRegisterButtons()
    {
        LinearLayout loginAndRegisterBottom = (LinearLayout) findViewById(R.id.loginAndRegisterBottom);
        loginAndRegisterBottom.setVisibility(View.INVISIBLE);
    }


    /*
     * Show login and register buttons
     * This function is called when the user logs out
     */
    protected void showLoginAndRegisterButtons()
    {
        LinearLayout loginAndRegisterBottom = (LinearLayout) findViewById(R.id.loginAndRegisterBottom);
        loginAndRegisterBottom.setVisibility(View.VISIBLE);
    }

    /*
    Initialize the main bottom navigation bar with the fragment layout
     */
    protected void initMainBNV()
    {
        final Toolbar mainToolbar = (Toolbar) findViewById(R.id.mainToolbar);

        mainBNV = (BottomNavigationView) findViewById(R.id.mainBNV);

        mainBNV.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                android.support.v4.app.Fragment selectedFragment=null;

                switch (item.getItemId())
                {
                    case R.id.allVenuesBNVItem:
                        selectedFragment = new AllVenuesFragment();
                        ((AllVenuesFragment)selectedFragment).setToolbar(mainToolbar);
                        createAllVenuesActionbar(mainToolbar);
                        break;

                    case R.id.reservationsBNVItem:
                        selectedFragment = new ReservationsFragment();
                        createReservationsToolbar(mainToolbar);
                        break;

                    case R.id.userProfileBNVItem:
                        selectedFragment = new AppUserProfileFragment();
                        createProfileToolbar(mainToolbar);

                }

                android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.mainFrameLayout,selectedFragment);
                transaction.commit();
                return true;
            }
        });

        //Manually displaying the first fragment - one time only
        android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.mainFrameLayout, new AllVenuesFragment());
        createAllVenuesActionbar(mainToolbar);
        transaction.commit();
    }



    public void closeFriendsFragment()
    {
        final Context root = getBaseContext();

        final RelativeLayout friendLayout =  (RelativeLayout) findViewById(R.id.friendsLayout);

        Animation animation = AnimationUtils.loadAnimation(root, R.anim.slide_down);

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}

            @Override
            public void onAnimationRepeat(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {
                friendLayout.setVisibility(View.INVISIBLE);
            }
        });
        final Toolbar mainToolbar = (Toolbar) findViewById(R.id.mainToolbar);
        createProfileToolbar(mainToolbar);
        friendLayout.startAnimation(animation);
    }
    /*
    create all venues toolbar
     */

    private void createAllVenuesActionbar(Toolbar mainToolbar)
    {
        mainToolbar.removeAllViews();
        getLayoutInflater().inflate(R.layout.all_venues_toolbar,mainToolbar);

    }


    /*
    create reservations toolbar
     */

    protected void createReservationsToolbar(Toolbar mainToolbar)
    {
        mainToolbar.removeAllViews();
        getLayoutInflater().inflate(R.layout.reservations_toolbar,mainToolbar);

    }

    /*
   create appUser profile toolbar
    */
    protected void createProfileToolbar(Toolbar mainToolbar) {
        mainToolbar.removeAllViews();
        getLayoutInflater().inflate(R.layout.userprofile_toolbar, mainToolbar);
    }

    /*
     * Function that redirects the user to the initial UI when logged out
     */
    public void logOut()
    {
        //return to the allVenues fragment
        redirectToDefaultFragment();

        //hide bottom navigation view
        hideMainBNV();

        //show login and register buttons
        showLoginAndRegisterButtons();

    }


    /*
     * A function that redirects the user to the default fragment when he logs out
     */
    protected void redirectToDefaultFragment()
    {
        Toolbar mainToolbar = (Toolbar) findViewById(R.id.mainToolbar);
        android.support.v4.app.Fragment selectedFragment = new AllVenuesFragment();
        ((AllVenuesFragment)selectedFragment).setToolbar(mainToolbar);
        createAllVenuesActionbar(mainToolbar);
        android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.mainFrameLayout,selectedFragment);
        transaction.commit();
    }


    protected void createFriendsToolbar(Toolbar mainToolbar)
    {
        mainToolbar.removeAllViews();
        getLayoutInflater().inflate(R.layout.friends_toolbar, mainToolbar);
    }

}