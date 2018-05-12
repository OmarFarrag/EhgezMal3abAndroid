package com.homidev.egypt.ehgezmal3ab;

import com.google.gson.annotations.SerializedName;

/*
 * Class used to wrap JSON responses that return a string with key "text"
 */
public class Error {
    @SerializedName("text")
    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
