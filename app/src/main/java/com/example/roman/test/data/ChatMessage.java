package com.example.roman.test.data;

import com.orm.SugarRecord;

public class ChatMessage extends SugarRecord {
    private String messageId;
    private String date;
    private boolean isSelf;
    private String message;

    ChatMessage() { }

    public ChatMessage(String messageId, String date, boolean isSelf, String message) {
        this.messageId = messageId;
        this.date = date;
        this.isSelf = isSelf;
        this.message = message;
    }

    public ChatMessage(Message message, boolean isSelf) {
        this.message = message.getMessage();
        this.date = message.getDate();
        this.messageId = message.getMessageId();
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

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
