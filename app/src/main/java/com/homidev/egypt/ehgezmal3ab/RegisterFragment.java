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


public class RegisterFragment extends Fragment {
    protected ConnectionManager connectionManager;
    public RegisterFragment(){
         connectionManager= ConnectionManager.getConnectionManager();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View registerView = inflater.inflate(R.layout.register_layout, container, false);

        setListeners(registerView);

        return registerView;

    }

    private void setListeners(final View registerView)
    {
        Button registerButton = (Button) registerView.findViewById(R.id.registerBtn);
        registerButton.setOnClickListener( new View.OnClickListener()
        {
            public void onClick(View v)
            {

                boolean validInfo = verifyUserInput(registerView);
                if(!validInfo){return;}

                Player playerToRegister = readRegisterInfo(registerView);

                String responseMessage = connectionManager.registerPlayer(playerToRegister);

                showRegisterResponseMessage(responseMessage);

            }

        });
    }

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

    protected void showRegisterResponseMessage(String message)
    {
        Context context = getContext();
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, message, duration);
        toast.show();
    }

}


