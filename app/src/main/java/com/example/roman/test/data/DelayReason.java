package com.example.roman.test.data;

import com.google.gson.annotations.SerializedName;
import com.orm.SugarRecord;

public class DelayReason extends SugarRecord {
    private static final String ID = "ID";
    private static final String NAME = "DN";

    @SerializedName(ID)
    private String reasonId;

    @SerializedName(NAME)
    private String name;

    public DelayReason(String name, String reasonId) {
        this.name = name;
        this.reasonId = reasonId;
    }

    public void setId(String reasonId) {
        this.reasonId = reasonId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReasonId() {
        return reasonId;
    }

    public String getName() {
        return name;
    }
}
