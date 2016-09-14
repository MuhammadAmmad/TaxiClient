package com.example.roman.test.data;

import com.orm.SugarRecord;
import com.orm.dsl.Unique;

public class AirRecord extends SugarRecord {
    @Unique
    private String recordId;
    private String description;
    private String fromAddress;
    private boolean isPrevious;

    public AirRecord() { }

    public AirRecord(Record record) {
        this.recordId = record.getRecordId();
        this.description = record.getDescription();
        this.fromAddress = record.getFromAddress();
        this.isPrevious = record.getIsPrevious();
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    public String getRecordId() {
        return recordId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFromAddress() {
        return fromAddress;
    }

    public void setFromAddress(String fromAddress) {
        this.fromAddress = fromAddress;
    }

    public boolean isPrevious() {
        return isPrevious;
    }

    public void setPrevious(boolean previous) {
        isPrevious = previous;
    }
}
