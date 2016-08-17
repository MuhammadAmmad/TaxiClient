package com.example.roman.test.data;

import com.google.gson.annotations.SerializedName;

public class Status {
    private static final String ID = "ID";
    private static final String NAME = "DN";
    private static final String CAN_SET = "CD";

    @SerializedName(ID)
    private String id;

    @SerializedName(NAME)
    private String name;

    @SerializedName(CAN_SET)
    private boolean canSet;

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isCanSet() {
        return canSet;
    }

    public void setCanSet(boolean canSet) {
        this.canSet = canSet;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean canSet() {
        return canSet;
    }
}
