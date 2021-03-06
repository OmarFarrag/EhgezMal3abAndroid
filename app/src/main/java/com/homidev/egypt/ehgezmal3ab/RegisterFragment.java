package com.homidev.egypt.ehgezmal3ab;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class RegisterFragment extends Fragment {
    private RegisterFragment instance;
    protected ConnectionManager connectionManager;
    public RegisterFragment(){
         connectionManager= ConnectionManager.getConnectionManager();
    }


    /*
     * Called when creating the fragment view
     * returns the view to be created
     * Sets the listeners for register button
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View registerView = inflater.inflate(R.layout.register_layout, container, false);

        setListeners(registerView);

        instance = this;

        return registerView;

    }

    /*
     * Sets the listener for register button
     * Checks if info is valid
     * If so, send the request to server through connection manager
     */
    private void setListeners(final View registerView) {
        Button registerButton = (Button) registerView.findViewById(R.id.registerBtn);
        registerButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                boolean validInfo = verifyUserInput(registerView);
                if (!validInfo) {
                    return;
                }

                Player playerToRegister = readRegisterInfo(registerView);

                String responseMessage = connectionManager.registerPlayer(playerToRegister);

                showRegisterResponseMessage(responseMessage);

            }

        });


        /*
         * Listener for become venue admin button
         */
        TextView becomeVenueAdmin = (TextView) registerView.findViewById(R.id.becomeVenAdminBtn);
        becomeVenueAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                connectionManager.getGeneralAdminPhone(instance);
            }
        });
    }


    /*
     * Verify all input fields according to the role of each one
     */
    private boolean verifyUserInput(View registerView)
    {
        boolean isValidInput= true;

        /*
            All fields are checked to display their error messages
         */


        isValidInput &= isValidUsername(registerView);

        isValidInput &= isValidPassword(registerView);

        isValidInput &= isValidEmail(registerView);

        isValidInput &= isValidNumber(registerView);

        isValidInput &= isValidFullName(registerView);

        return isValidInput;

    }

    /*
     * Checks username is not empty
     */
    private boolean isValidUsername(View registerView)
    {
        TextView usernameText = (TextView) registerView.findViewById(R.id.usernameText);
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
     * Checks password is not empty
     *                    more than eight chars
     */
    private boolean isValidPassword(View registerView)
    {

        TextView passwordText = (TextView) registerView.findViewById(R.id.passwordText);
        String password =  passwordText.getText().toString();
        //Set error initially to null so if it is a callback , the previous error goes away
        passwordText.setError(null);
        if( TextUtils.isEmpty(password)  )
        {
            passwordText.setError(getString(R.string.fieldIsRequired));
            return false;
        }
        else if( password.length()<8)
        {
            passwordText.setError(getString(R.string.shortPassword));
            return false;
        }
        return true;
    }

    /*
     * Checks email matches emails format and not empty
     */
    private boolean isValidEmail(View registerView)
    {
        TextView emailText = (TextView) registerView.findViewById(R.id.emailText);
        String email = emailText.getText().toString();
        //Set error initially to null so if it is a callback , the previous error goes away
        emailText.setError(null);
        if ( TextUtils.isEmpty(email))
        {
            emailText.setError(getString(R.string.fieldIsRequired));
            return false;
        }
        else if ( !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() )
        {
            emailText.setError(getString(R.string.invalidEmail));
            return false;
        }
        return true;
    }

    /*
     * Checks number is not empty and 11 cahrs
     */
    private boolean isValidNumber(View registerView)
    {
        TextView numberText = (TextView) registerView.findViewById(R.id.numberText);
        String number = numberText.getText().toString();
        //Set error initially to null so if it is a callback , the previous error goes away
        numberText.setError(null);
        String vaildationString = "^[0-9]*$";
        if( TextUtils.isEmpty(number))
        {
            numberText.setError(getString(R.string.fieldIsRequired));
            return false;
        }
        else if ( number.length() != 11 || !number.trim().matches(vaildationString))
        {
            numberText.setError(getString(R.string.invalidNumber));
            return false;
        }
        return true;
    }

    /*
     * Checks full name is not empty
     */
    private boolean isValidFullName(View registerView)
    {
        TextView fullNameText = (TextView) registerView.findViewById(R.id.fullNameText);
        String fullName = fullNameText.getText().toString();
        //Set error initially to null so if it is a callback , the previous error goes away
        fullNameText.setError(null);
        if( TextUtils.isEmpty(fullName))
        {
            fullNameText.setError(getString(R.string.fieldIsRequired));
            return false;
        }
        return true;
    }

    /*
     * Parse text from all fields and wrap them in player object
     */
    protected Player readRegisterInfo(View registerView)
    {
        TextView usernameText = (TextView) registerView.findViewById(R.id.usernameText);
        String username = usernameText.getText().toString();

        TextView passwordText = (TextView) registerView.findViewById(R.id.passwordText);
        String password =  passwordText.getText().toString();

        TextView emailText = (TextView) registerView.findViewById(R.id.emailText);
        String email = emailText.getText().toString();

        TextView numberText = (TextView) registerView.findViewById(R.id.numberText);
        String number = numberText.getText().toString();

        TextView fullNameText = (TextView) registerView.findViewById(R.id.fullNameText);
        String fullName = fullNameText.getText().toString();

        return new Player(username,password,email,number,fullName);
    }

    /*
     * SHows the response of the register request
     */
    protected void showRegisterResponseMessage(String message)
    {
        Context context = getContext();
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, message, duration);
        toast.show();
    }


    /*
     * Launches the dial with the general admin contact
     * Contact is brought from the server
     */
    public void callGeneralAdmin(String number) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:"+number));
        startActivity(intent);
    }

}


