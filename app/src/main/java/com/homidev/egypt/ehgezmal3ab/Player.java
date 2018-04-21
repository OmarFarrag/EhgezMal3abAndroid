package com.homidev.egypt.ehgezmal3ab;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Player implements Serializable{
    @SerializedName("userName")
    private String username;
    @SerializedName("password")
    private String password;
    @SerializedName("email")
    private String email;
    @SerializedName("phoneNumber")
    private String number;
    @SerializedName("name")
    private String name;
    @SerializedName("balance")
    private int balance;
    @SerializedName("oldPassword")
    private String oldPassword;
    @SerializedName("userType")
    private String userType;

    public Player(String username, String password, String email, String number, String name)
    {
        this.username = username;
        this.password = password;
        this.email = email;
        this.number = number;
        this.name = name;
    }

    public Player(String username, String password, String email, String number, String name, int balance, String oldPassword, String userType) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.number = number;
        this.name = name;
        this.balance = balance;
        this.oldPassword = oldPassword;
        this.userType = userType;
    }

    public Player(String username, String password)
    {
        this.username = username;
        this.password = password;

    }

    public String getUsername(){return username;}

    public String getPassword(){return password;}

    public String getEmail(){return email;}

    public String getNumber(){return number;}

    public String getName(){return name;}

    public int getBalance() {
        return balance;
    }
}
