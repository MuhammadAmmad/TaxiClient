package com.example.roman.test.data;

import ckm.simple.sql_provider.annotation.SimpleSQLColumn;
import ckm.simple.sql_provider.annotation.SimpleSQLTable;

@SimpleSQLTable(
        table = "messages",
        provider = "MessageProvider")
public class ChatMessage {
    @SimpleSQLColumn("id")
    private String id;

    @SimpleSQLColumn("date")
    private String date;

    @SimpleSQLColumn("self")
    private boolean isSelf;

    @SimpleSQLColumn("message")
    private String message;

    public ChatMessage() { }

    public ChatMessage(Message message, boolean isSelf) {
        this.message = message.getMessage();
        this.date = message.getDate();
        this.id = message.getId();
        this.isSelf = isSelf;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean getIsSelf() {
        return isSelf;
    }

    public void setIsSelf(boolean isSelf) {
        this.isSelf = isSelf;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
