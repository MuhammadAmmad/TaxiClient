package com.example.roman.test.data;

import com.google.gson.annotations.SerializedName;

public class Message {
    private static final String ID = "I";
    private static final String DATE = "D";
    private static final String MESSAGE = "MS";

    @SerializedName(ID)
    private String id;

    @SerializedName(MESSAGE)
    private String message;

    @SerializedName(DATE)
    private String date;

    public Message() { }

    public Message(String id, String message, String date) {
        this.id = id;
        this.message = message;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    void setMessage(String message) {
        this.message = message;
    }

    public String getDate() {
        return date;
    }

    void setDate(String date) {
        this.date = date;
    }
}
