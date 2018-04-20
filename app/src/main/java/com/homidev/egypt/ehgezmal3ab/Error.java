package com.homidev.egypt.ehgezmal3ab;

import com.google.gson.annotations.SerializedName;

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
