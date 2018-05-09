package com.homidev.egypt.ehgezmal3ab;

import android.content.Context;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.TextInputEditText;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

/**
 * Created by engineer on 08/05/18.
 */

public class UpdateVenueFragment extends android.support.v4.app.Fragment {

    ConnectionManager connectionManager;
    private String venuePhone;
    private String name;
    UpdateVenueFragment instance;
    TextInputEditText venueNameTxt;
    TextInputEditText venuePhoneTxt;
    Button updateInfoBtn;


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        connectionManager = ConnectionManager.getConnectionManager();
        instance = this;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {

        View updateInfoView = inflater.inflate(R.layout.update_venue_fragment,container,false);

        venueNameTxt = updateInfoView.findViewById(R.id.updateVenueNameTxt);
        venuePhoneTxt = updateInfoView.findViewById(R.id.updateVenuePhoneTxt);
        updateInfoBtn = updateInfoView.findViewById(R.id.updateVenueInfoBtn);

        //Get the pre-passed info
        venuePhone = getArguments().getString("phoneNumber");
        name = getArguments().getString("name");

        displayInfo();

        setUpdateButtonListener();

        return updateInfoView;

    }

    public void setUpdateButtonListener() {
        updateInfoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Venue venue = new Venue();

                boolean isValid = true;

                isValid &= isValidNumber(venuePhoneTxt);

                isValid &= isValidFullName(venueNameTxt);

                if(!isValid){return;}

                boolean changed = false;

                if(!venuePhoneTxt.getText().toString().equals(venuePhone)) {

                    changed = true;
                }

                if(!venueNameTxt.getText().toString().equals(name)){

                    changed = true;
                }

                if(!changed)
                {
                    showMessage(getString(R.string.noChangesDetected));
                    return;
                }
                venue.setPhoneNumber(venuePhoneTxt.getText().toString());
                venue.setVenueTitle(venueNameTxt.getText().toString());
                connectionManager.updateVenueInformation(venue, instance);


            }
        });
    }

    public void displayInfo() {
        venueNameTxt.setText(name);
        venuePhoneTxt.setText(venuePhone);
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

    protected void showMessage(String message) {
        Context context = getContext();
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, message, duration);
        toast.show();
    }

    public void updateError()
    {
        showMessage(getString(R.string.error));
    }


    //Called by the connection manager when the update was made successfully
    public void updatedSuccessfully()
    {
        showMessage(getString(R.string.updatedSuccessfully));
        returnToVenAdminProfile();
    }

    private void returnToVenAdminProfile() {
        android.support.v4.app.FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.mainFrameLayout,new AppUserProfileFragment());
        transaction.commit();

    }
}
