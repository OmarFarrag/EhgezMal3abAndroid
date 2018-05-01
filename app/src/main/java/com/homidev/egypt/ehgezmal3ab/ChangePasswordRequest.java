package com.homidev.egypt.ehgezmal3ab;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;



public class ChangePasswordRequest {

    @SerializedName("username")
    @Expose
    private String username;

    @SerializedName("oldPassword")
    @Expose
    private String oldPassword;

    @SerializedName("password")
    @Expose
    private String password;

    ChangePasswordRequest(String username, String oldPassword, String password)
    {
        this.oldPassword = oldPassword;
        this.password = password;
        this.username = username;
    }
}
