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
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;



public class AppUserProfileFragment extends android.support.v4.app.Fragment {

    //The fragment toolbar
    private Toolbar menuToolBar;
    //Connection manager to handle server requests
    private static ConnectionManager connectionManager = ConnectionManager.getConnectionManager();
    private EhgezMal3abAPI ehgezMal3abAPI = connectionManager.createEhgezMal3abService();
    //The current player that the profile fragment will show his info
    private static Player player;


    /*
     *Set the player whose info will be displayed
     * This function is called by the connection manager when gets the info from the server
     */
    public static void setPlayer(Player player) {
        AppUserProfileFragment.player = player;
    }


    //Empty constructor
    public AppUserProfileFragment()
    {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    /*
     * This function is called to display the UI components to the user
     * The view is inflated from the corresponding layout
     * The function that sets the listeners is called
     * The connection manager is called to request the player info
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View userProfileView = inflater.inflate(R.layout.app_user_profile, container, false);

        setListeners(userProfileView);

        connectionManager.getPlayer(userProfileView, this);

        return userProfileView;
    }


    public static void getUser() {
        return;
    }


    //Sets the toolbar of the profile fragment
    public void setToolbar(Toolbar mainToolBar) {
        this.menuToolBar = mainToolBar;
    }


    /*
     * Extracts the info from the player object and displays it on th UI
     * Called by the connection manager when the player object is brought from the server
     */
    public void setupLayout(View view) {
        if(player != null) {
            TextView welcomeFullName = view.findViewById(R.id.userProfileWelcome);
            TextView balance = view.findViewById(R.id.userProfileBalance);
            TextView phoneNumber = view.findViewById(R.id.userProfilePhone);
            TextView email = view.findViewById(R.id.userProfileEmail);
            welcomeFullName.setText("Hey, " + player.getName());
            Integer bal = player.getBalance();
            balance.setText(bal.toString());
            email.setText(player.getEmail());
            phoneNumber.setText(player.getNumber());
        }
    }


    /*
     * SUMMARY
     * Creates click listeners for the update info, reset password, friends and logout buttons
     * Since venue admin listeners are differnet from normal user, a check is made to call the proper function in each listener
     * Link each button with its listener
     * If the user is venue admin hide the friends button
     */
    private void setListeners(final View view) {

        View.OnClickListener updateInfoListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Check if the user is app user or venue admin
                if(!getContext().getSharedPreferences("appUserPrefs",Context.MODE_PRIVATE).getString("username","").equals("")) {
                    showUpdateInfoFragment();
                }
                else{
                    showUpdateVenueFragment();
                }
            }
        };

        View.OnClickListener resetPassListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChangePasswordFragment();
            }
        };

        View.OnClickListener viewFriendsListener = new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                if(!getContext().getSharedPreferences("appUserPrefs",Context.MODE_PRIVATE).getString("username","").equals("")) {
                    //display the view friends fragment.
                    showFriendsFragment(view);
                }

            }
        };

        View.OnClickListener logoutListener = new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                /*SharedPreferences sharedPreferences = getActivity().getSharedPreferences("appUserPrefs", Context.MODE_PRIVATE);
                sharedPreferences.edit().remove("token");
                Toast.makeText(getContext(), "logged out", Toast.LENGTH_SHORT);*/
                connectionManager.logoutUser(player);
            }
        };


        //Extract the buttons
        CardView updateInfo = view.findViewById(R.id.updateInfoCV);
        CardView resetPass = view.findViewById(R.id.resetPassCV);
        CardView viewFriends = view.findViewById(R.id.friendsCV);
        CardView logout = view.findViewById(R.id.logoutCV);

        //Link the listeners
        updateInfo.setOnClickListener(updateInfoListener);
        resetPass.setOnClickListener(resetPassListener);
        viewFriends.setOnClickListener(viewFriendsListener);
        logout.setOnClickListener(logoutListener);

        //Hide friends button for venue admins
        if(getContext().getSharedPreferences("appUserPrefs",Context.MODE_PRIVATE).getString("username","").equals(""))
        {
            viewFriends.setVisibility(View.GONE);
        }
    }


    /*
     * Shows the change password fragment
     * If the user is admin inflate the ChangePassword fragment in the admin frame, else in the main frame
     * Called when the user clicks on Reset password button
     */
    protected void showChangePasswordFragment()
    {
        android.support.v4.app.FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        if(!getContext().getSharedPreferences("appUserPrefs",Context.MODE_PRIVATE).getString("username","").equals("")) {
            transaction.replace(R.id.mainFrameLayout, new ChangePasswordFragment());
        }
        else {
            transaction.replace(R.id.adminMainFrame, new ChangePasswordFragment());
        }
        transaction.commit();
    }


    /*
     * Shows the updateInfoFragment
     * Called when a normal user clicks on the update info button
     * Puts the phone number and name in a bundle to be passed to the new fragment
     */
    protected void showUpdateInfoFragment()
    {
        //store the info to be passed
        Bundle bundle = new Bundle();
        bundle.putString("phoneNumber",player.getNumber());
        bundle.putString("name",player.getName());

        //Create the new fragment and pass the info
        UpdateInfoFragment updateInfoFragment = new UpdateInfoFragment();
        updateInfoFragment.setArguments(bundle);

        //Replace the fragment in the main layout and commit the transaction
        android.support.v4.app.FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.mainFrameLayout,updateInfoFragment);
        transaction.commit();
    }


    /* Shows the Update venue info fragment
     * Called when a venue admin clicks on the update info button
     * Puts the venue phone number and venue name in a bundle to be passed to the new fragment
     */
    protected void showUpdateVenueFragment()
    {
        //store the info to be passed
        Bundle bundle = new Bundle();
        bundle.putString("phoneNumber",connectionManager.getAdminVenue().getPhoneNumber());
        bundle.putString("name",connectionManager.getAdminVenue().getVenueTitle());

        //Create the new fragment and pass the info
        UpdateVenueFragment updateVenueFragment = new UpdateVenueFragment();
        updateVenueFragment .setArguments(bundle);

        //Replace the fragment in the admin main frame and commit the transaction
        android.support.v4.app.FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.adminMainFrame,updateVenueFragment );
        transaction.commit();
    }


    /*
     * Shows friends fragment
     * Called when a normal user clicks on friends button
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void showFriendsFragment(View itemView){
        android.support.v4.app.Fragment friendsFragment = new ViewFriends();
        android.support.v4.app.FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.mainFrameLayout, friendsFragment);
        transaction.commit();
        ImageButton slideDownButton = (ImageButton) itemView.findViewById(R.id.friends_slideDown_button);
    }
}


