package com.homidev.egypt.ehgezmal3ab;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TimeSlot {

    @SerializedName("start")
    @Expose
    private String startsOn;

    @SerializedName("end")
    @Expose
    private String endsOn;

    @SerializedName("empty")
    @Expose
    private boolean empty;

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getUsername() {
        return username;
    }

    @SerializedName("phoneNumber")
    @Expose
    private String phoneNumber;

    @SerializedName("username")
    @Expose
    private String username;

    private boolean selectd;

    public String getStartsOn()
    {
        return startsOn;
    }

    public String getEndsOn() {return endsOn;}

    public boolean isEmpty()
    {
        return empty;
    }

    public boolean isSelected()
    {
        return selectd;
    }

    public void setSelected(boolean setter)
    {
        selectd = setter;
    }
}
