package com.example.roman.test.data;

import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

import ckm.simple.sql_provider.annotation.SimpleSQLColumn;
import ckm.simple.sql_provider.annotation.SimpleSQLTable;

@SimpleSQLTable(
        table = "messages",
        provider = "MessageProvider")
public class Message {
    public static final String ID = "I";
    public static final String DATE = "D";
    public static final String MESSAGE = "MS";

    @SerializedName(ID)
    @SimpleSQLColumn(value = "col_id", primary = true)
    public int id;

    @SerializedName(MESSAGE)
    @SimpleSQLColumn("message")
    public String message;

    @SerializedName(DATE)
    @SimpleSQLColumn("date")
    public String date;

    public Message() {
        id = 0;
        message = "";
        date = "";
    }

    public Message(JSONObject sector) throws JSONException {
        id = sector.getInt(ID);
        date = sector.getString(DATE);
        message = sector.getString(MESSAGE);
    }

    public String getMessage() {
        return message;
    }

    public String getDate() {
        return date;
    }

    public int getId() {
        return id;
    }
}
