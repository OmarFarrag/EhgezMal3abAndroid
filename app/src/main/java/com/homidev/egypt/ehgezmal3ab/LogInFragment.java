package com.homidev.egypt.ehgezmal3ab;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.homidev.egypt.ehgezmal3ab.R;

/**
 * Created by Omar Farrag on 3/6/2018.
 */

public class LogInFragment extends Fragment {


    protected ConnectionManager connectionManager;
    LogInFragment me;

    public LogInFragment(){
        connectionManager= ConnectionManager.getConnectionManager();
    }

    /**
     * This function returns the view of the login fragment
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View loginView = inflater.inflate(R.layout.login_layout, container, false);

        setListeners(loginView);

        me=this;

        return loginView;

    }


    //Set the listeners for the interactive elements on the login fragment
    protected void setListeners(final View loginView)
    {
        Button registerButton = (Button) loginView.findViewById(R.id.loginBtn);
        registerButton.setOnClickListener( new View.OnClickListener()
        {
            public void onClick(View v)
            {

                boolean validInfo = verifyUserInput(loginView);
                if(!validInfo){return;}

                Player playerToLogin = readLoginInfo(loginView);

                connectionManager.loginPlayer(playerToLogin,me);



            }

        });
    }


    /*
    Verify that the user's input is valid so as to send it to the server or display error
     */
    protected boolean verifyUserInput(View loginView)
    {
        boolean isValidInput= true;

        /*
            All fields are checked to display their error messages
         */


        isValidInput &= isValidUsername(loginView);

        isValidInput &= isValidPassword(loginView);

        return isValidInput;
    }


    /*
    Check if the username is valid (not empty)
     */
    private boolean isValidUsername(View loginView)
    {
        TextView usernameText = (TextView) loginView.findViewById(R.id.usernameText);
        String username = usernameText.getText().toString();

        //Set error initially to null so if it is a callback , the previous error goes away
        usernameText.setError(null);

        if(TextUtils.isEmpty(username))
        {
            usernameText.setError(getString(R.string.fieldIsRequired));
            return false;
        }

        return true;
    }


    /*
    Check if the password is valid (not empty / more than 8 chars)
     */
    protected boolean isValidPassword(View loginView)
    {

        TextView passwordText = (TextView) loginView.findViewById(R.id.passwordText);
        String password =  passwordText.getText().toString();

        //Set error initially to null so if it is a callback , the previous error goes away
        passwordText.setError(null);

        if( TextUtils.isEmpty(password)  )
        {
            passwordText.setError(getString(R.string.fieldIsRequired));
            return false;
        }
        else if(password.length()<8)
        {
            passwordText.setError(getString(R.string.shortPassword));
            return false;
        }

        return true;
    }


    /*
    get the user input from the text fields and construct the player object
     */
    protected Player readLoginInfo(View loginView)
    {
        TextView usernameText = (TextView) loginView.findViewById(R.id.usernameText);
        String username = usernameText.getText().toString();

        TextView passwordText = (TextView) loginView.findViewById(R.id.passwordText);
        String password =  passwordText.getText().toString();

        return new Player(username,password);
    }


    /*
    Show the response message after attempting to login
     */
    protected void showLoginResponseMessage(boolean loggedIn)
    {
        Toast toast = null;
        Context context = getContext();

        int duration = Toast.LENGTH_SHORT;

        if(loggedIn) {
             toast = Toast.makeText(context, getString(R.string.loggedIn), duration);
        }
        else
        {
            toast = Toast.makeText(context, getString(R.string.incorrectUP), duration);
        }

        toast.show();
    }


}
