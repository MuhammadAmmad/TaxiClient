package com.example.roman.test.data;

import com.google.gson.annotations.SerializedName;
import com.orm.SugarRecord;
import com.orm.dsl.Unique;

public class Message extends SugarRecord {
    private static final String ID = "I";
    private static final String DATE = "D";
    private static final String MESSAGE = "MS";

    @SerializedName(ID)
    @Unique
    private String messageId;

    @SerializedName(MESSAGE)
    private String message;

    @SerializedName(DATE)
    private String date;

    public Message() { }

    public Message(String messageId, String message, String date) {
        this.messageId = messageId;
        this.message = message;
        this.date = date;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setId(String messageId) {
        this.messageId = messageId;
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
