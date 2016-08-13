package com.example.roman.test.data;

import com.google.gson.annotations.SerializedName;

import ckm.simple.sql_provider.annotation.SimpleSQLColumn;
import ckm.simple.sql_provider.annotation.SimpleSQLTable;

@SimpleSQLTable(
        table = "sectors",
        provider = "SectorProvider")
public class Sector {
    private static final String ID = "I";
    private static final String NAME = "N";
    private static final String DRIVERS = "P";

    @SerializedName(ID)
    @SimpleSQLColumn(value = "col_id", primary = true)
    private String id;

    @SerializedName(NAME)
    @SimpleSQLColumn("name")
    private String name;

    @SerializedName(DRIVERS)
    @SimpleSQLColumn("drivers")
    private int drivers;

    public Sector() {
        id = "";
        name = "";
        drivers = 0;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    void setDrivers(int drivers) {
        this.drivers = drivers;
    }
}
