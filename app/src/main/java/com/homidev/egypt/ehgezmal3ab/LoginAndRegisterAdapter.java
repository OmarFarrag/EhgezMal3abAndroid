package com.homidev.egypt.ehgezmal3ab;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * This adapter creates the login and register fragments and coordinates them in the tab layout
 */

public class LoginAndRegisterAdapter extends FragmentPagerAdapter {

    LogInFragment logInFragment;
    RegisterFragment registerFragment;

    public LoginAndRegisterAdapter( FragmentManager fm) {
        super(fm);
        logInFragment = new LogInFragment();
        registerFragment = new RegisterFragment();

    }

    /**
     * Gets the fragment for the selected tab position
     */
    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return logInFragment;
        } else {
            return registerFragment;
        }

    }

    /**
     * Return the total number of pages.
     */
    @Override
    public int getCount() {
        return 2;
    }


    /**
     * returns the title of the fragment at specific position
     */
    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) {
            return "Log In";
        } else {

            return "Register";
        }
    }

}
