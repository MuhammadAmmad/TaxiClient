package com.example.roman.test.data;

import com.google.gson.annotations.SerializedName;
import com.orm.SugarRecord;
import com.orm.dsl.Unique;

public class Sector extends SugarRecord {
    private static final String ID = "I";
    private static final String NAME = "N";
    private static final String DRIVERS = "P";

    @Unique
    @SerializedName(ID)
    private String sectorId;

    @SerializedName(NAME)
    private String name;

    @SerializedName(DRIVERS)
    private int drivers;

    private transient boolean isChecked;

    public Sector() { }

    public Sector(String sectorId, String name, int drivers) {
        this.sectorId = sectorId;
        this.name = name;
        this.drivers = drivers;
    }

    public String getSectorId() {
        return sectorId;
    }

    public void setSectorId(String id) {
        this.sectorId = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDrivers() {
        return drivers;
    }

    public void setDrivers(int drivers) {
        this.drivers = drivers;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }
}
