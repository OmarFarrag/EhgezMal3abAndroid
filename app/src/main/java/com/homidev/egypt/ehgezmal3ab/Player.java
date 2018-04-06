package com.homidev.egypt.ehgezmal3ab;



public class Player {
    private String username;
    private String password;
    private String email;
    private String number;
    private String name;

    public Player(String username, String password, String email, String number, String name)
    {
        this.username = username;
        this.password = password;
        this.email = email;
        this.number = number;
        this.name = name;
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


}
