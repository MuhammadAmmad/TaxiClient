package com.example.roman.test.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.orm.SugarRecord;
import com.orm.dsl.Unique;

public class Sector extends SugarRecord {
    private static final String ID = "I";
    private static final String NAME = "N";
    private static final String DRIVERS = "P";

    @Unique
    @Expose
    @SerializedName(ID)
    private String sectorId;

    @Expose
    @SerializedName(NAME)
    private String name;

    @Expose
    @SerializedName(DRIVERS)
    private int drivers;

    private boolean isChecked;

    public Sector() { }

    public Sector(String sectorId, String name, int drivers, boolean isChecked) {
        this.sectorId = sectorId;
        this.name = name;
        this.drivers = drivers;
        this.isChecked = isChecked;
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
