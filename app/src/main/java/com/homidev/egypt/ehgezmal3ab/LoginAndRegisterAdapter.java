package com.homidev.egypt.ehgezmal3ab;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by Omar Farrag on 3/6/2018.
 * This adapter creates the login and register fragments an
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
     * Return the {@link Fragment} that should be displayed for the given page number.
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

    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) {
            return "Log In";
        } else {

            return "Register";
        }
    }

}
