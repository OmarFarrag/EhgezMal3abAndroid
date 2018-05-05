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

    public void showToastMessage(String message)
    {
        Toast messageToast = null;
        messageToast=  Toast.makeText(getContext(),message,Toast.LENGTH_SHORT);

        messageToast.show();
    }


    public void returnToParent()
    {
        //Check if the user is appUser or venue admin
        if( !getActivity().getSharedPreferences("appUserPrefs", MODE_PRIVATE).getString("username", "").equals(""))
        {
            returnToUserProfile();
        }
    }

    protected void returnToUserProfile(){
            android.support.v4.app.FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.mainFrameLayout,new AppUserProfileFragment());
            transaction.commit();

    }
}
