package com.example.roman.test.data;

import com.google.gson.annotations.SerializedName;
import com.orm.SugarRecord;
import com.orm.dsl.Unique;

public class DriverStatus extends SugarRecord {
    private static final String ID = "ID";
    private static final String NAME = "DN";
    private static final String CAN_SET = "CD";

    @SerializedName(ID)
    @Unique
    private String statusId;

    @SerializedName(NAME)
    private String name;

    @SerializedName(CAN_SET)
    private boolean canSet;

    public DriverStatus() { }

    public DriverStatus(String statusId, String name, boolean canSet) {
        this.statusId = statusId;
        this.name = name;
        this.canSet = canSet;
    }

    public void setId(String statusId) {
        this.statusId = statusId;
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

    public String getStatusId() {
        return statusId;
    }

    public String getName() {
        return name;
    }

    public boolean canSet() {
        return canSet;
    }
}
