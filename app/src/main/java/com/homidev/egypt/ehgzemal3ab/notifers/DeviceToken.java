package com.homidev.egypt.ehgzemal3ab.notifers;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DeviceToken {
    @SerializedName("username")
    private String username;
    @SerializedName("Token")
    private String devicetoken;

    // Getter Methods

    public String getUsername() {
        return username;
    }

    public String getDevicetoken() {
        return devicetoken;
    }

    // Setter Methods

    public void setUsername( String username ) {
        this.username = username;
    }

    public void setDevicetoken( String devicetoken ) {
        this.devicetoken = devicetoken;
    }
}
