package com.example.roman.test.data;

import com.google.gson.annotations.SerializedName;

import ckm.simple.sql_provider.annotation.SimpleSQLColumn;
import ckm.simple.sql_provider.annotation.SimpleSQLTable;

@SimpleSQLTable(
        table = "messages",
        provider = "MessageProvider")
public class Message {
    private static final String ID = "I";
    private static final String DATE = "D";
    private static final String MESSAGE = "MS";

    @SerializedName(ID)
    @SimpleSQLColumn("col_id")
    private String id;

    @SerializedName(MESSAGE)
    @SimpleSQLColumn("message")
    private String message;

    @SerializedName(DATE)
    @SimpleSQLColumn("date")
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
