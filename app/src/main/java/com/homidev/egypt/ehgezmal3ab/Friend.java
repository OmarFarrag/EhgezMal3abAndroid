package com.homidev.egypt.ehgezmal3ab;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Friend {

    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("friendName")
    @Expose
    private String friendName;
    @SerializedName("friendshipStatus")
    @Expose
    private String friendshipStatus;
    @SerializedName("friend")
    @Expose
    private Player friend;

    /**
     * No args constructor for use in serialization
     *
     */
    public Friend() {
    }

    /**
     *
     * @param username
     * @param friend
     * @param friendName
     * @param friendshipStatus
     */
    public Friend(String username, String friendName, String friendshipStatus, Player friend) {
        super();
        this.username = username;
        this.friendName = friendName;
        this.friendshipStatus = friendshipStatus;
        this.friend = friend;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFriendName() {
        return friendName;
    }

    public void setFriendName(String friendName) {
        this.friendName = friendName;
    }

    public String getFriendshipStatus() {
        return friendshipStatus;
    }

    public void setFriendshipStatus(String friendshipStatus) {
        this.friendshipStatus = friendshipStatus;
    }

    public Player getFriend() {
        return friend;
    }

    public void setFriend(Player friend) {
        this.friend = friend;
    }

}