package com.homidev.egypt.ehgezmal3ab;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import static android.content.Context.MODE_PRIVATE;


public class ChangePasswordFragment extends android.support.v4.app.Fragment {

    private ConnectionManager connectionManager;
    private ChangePasswordFragment instance;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /*
     * This function is called to display the UI components to the user
     * Inflate the view, get the connection manager and call setListeners function to set listeners for buttons
     * The caller is expecting the function to return the view to be displayed so at the end we return the all venues view
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View userProfileView = inflater.inflate(R.layout.change_password_layout, container, false);

        setListeners(userProfileView);

        connectionManager = ConnectionManager.getConnectionManager();

        instance=this;


        return userProfileView;
    }


    /*
     * Set the listener of the change password button
     * When the button is clicked the following checks are to be made:
     *  1- No field is empty
     *  2- New passwords match
     * Get the username and call the connection manager function passing the username, old password and new password
     */
    protected void setListeners(View changePasswordView)
    {
        Button changePasswordButton = changePasswordView.findViewById(R.id.changePasswordBtn);
        final TextView oldPassword = changePasswordView.findViewById(R.id.oldPasswordTxt);
        final TextView newPassword = changePasswordView.findViewById(R.id.newPasswordTxt);
        final TextView confirmNewPassword = changePasswordView.findViewById(R.id.confirmPasswordTxt);

        changePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String oldPasswordString = oldPassword.getText().toString();
                String newPasswordString = newPassword.getText().toString();
                String confirmNewPasswordString = confirmNewPassword.getText().toString();
                if(!newPasswordString.equals(confirmNewPasswordString))
                {
                    showToastMessage(getResources().getString(R.string.passwordsDontMatch));
                }
                else
                {
                    String username = getActivity().getSharedPreferences("appUserPrefs", MODE_PRIVATE).getString("username", "");
                    if (username == "") {
                        username = getActivity().getSharedPreferences("venAdminPrefs", MODE_PRIVATE).getString("username", "");;
                    }
                    connectionManager.changePassword(new ChangePasswordRequest(username,oldPasswordString,newPasswordString),instance);
                }
            }
        });
    }


    /*
     * Shows a toast message with the string parameter
     */
    public void showToastMessage(String message)
    {
        Toast messageToast = null;
        messageToast=  Toast.makeText(getContext(),message,Toast.LENGTH_SHORT);
        messageToast.show();
    }


    /*
     * Called when the connection manager receives a successful response from the server on the cahnge password request
     * Redirects the user to the profile fragment
     */
    public void returnToParent()
    {

        android.support.v4.app.FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();

        if(!getContext().getSharedPreferences("appUserPrefs",MODE_PRIVATE).getString("username","").equals("")) {
            transaction.replace(R.id.mainFrameLayout, new AppUserProfileFragment());
        }
        else{
            transaction.replace(R.id.adminMainFrame, new AppUserProfileFragment());
        }

        transaction.commit();


    }


}
