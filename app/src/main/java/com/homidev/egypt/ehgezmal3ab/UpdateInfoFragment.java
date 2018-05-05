package com.homidev.egypt.ehgezmal3ab;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TextInputEditText;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class UpdateInfoFragment extends android.support.v4.app.Fragment {

    private String phoneNumber;
    private String name;
    private TextInputEditText nameTxt;
    private TextInputEditText phoneNumberTxt;
    private Button updateInfoBtn;
    private ConnectionManager connectionManager;
    private UpdateInfoFragment instance;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        connectionManager = ConnectionManager.getConnectionManager();
        instance = this;

    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {

        View updateInfoView = inflater.inflate(R.layout.update_info_layout,container,false);

        nameTxt = updateInfoView.findViewById(R.id.updateNameTxt);
        phoneNumberTxt = updateInfoView.findViewById(R.id.updateNumberTxt);
        updateInfoBtn = updateInfoView.findViewById(R.id.updateInfoBtn);

        //Get the pre-passed info
        phoneNumber = getArguments().getString("phoneNumber");
        name = getArguments().getString("name");

        DisplayInfo();

        setUpdateButtonListener();

        return updateInfoView;

    }


    //Displays the user's info in the text boxes for editing
    private void DisplayInfo()
    {
        nameTxt.setText(name);
        phoneNumberTxt.setText(phoneNumber);
    }

    /*
     *   Sets the listener of the update button
     *   Gets the text from the text boxes, checks if any is changed, sends the changed only
     */
    private void setUpdateButtonListener()
    {
        updateInfoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Player newPlayer = new Player();

                boolean isValid=true;

                isValid &= isValidNumber(phoneNumberTxt);

                isValid &= isValidFullName(nameTxt);

                if(!isValid){return;}

                boolean changed = false;

                if(!phoneNumberTxt.getText().toString().equals(phoneNumber)) {
                    newPlayer.setNumber(phoneNumberTxt.getText().toString());
                    changed = true;
                }

                if(!nameTxt.getText().toString().equals(name)){
                    newPlayer.setName(nameTxt.getText().toString());
                    changed = true;
                }

                if(!changed)
                {
                    showMessage(getString(R.string.noChangesDetected));
                    return;
                }



                newPlayer.setUsername(getActivity().getSharedPreferences("appUserPrefs", Context.MODE_PRIVATE).getString("username",""));

                connectionManager.updatePlayerInfo(newPlayer,instance);


            }
        });
    }


    //check entered number is valid
    private boolean isValidNumber(TextView numberText)
    {

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

    //Check entered name is valid
    private boolean isValidFullName(TextInputEditText fullNameText)
    {

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

    protected void showMessage(String message)
    {
        Context context = getContext();
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, message, duration);
        toast.show();
    }


    //Called by the connection manager when the update was made successfully
    public void updatedSuccessfully()
    {
        showMessage(getString(R.string.updatedSuccessfully));
        returnToUserProfile();
    }

    public void updateError()
    {
        showMessage(getString(R.string.error));
    }

    //returns to the user profile UI
    protected void returnToUserProfile(){
        android.support.v4.app.FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.mainFrameLayout,new AppUserProfileFragment());
        transaction.commit();

    }
}
