package com.example.roman.test.data;

import java.util.Date;

import ckm.simple.sql_provider.annotation.SimpleSQLColumn;
import ckm.simple.sql_provider.annotation.SimpleSQLTable;

@SimpleSQLTable(
        table = "sectors",
        provider = "SectorProvider")
public class Sector {
    public static final String ID = "I";
    public static final String NAME = "N";
    public static final String DRIVERS = "P";

//    @SerializedName(ID)
    @SimpleSQLColumn(value = "col_id", primary = true)
    public String id;

//    @SerializedName(NAME)
    @SimpleSQLColumn("name")
    public String name;

//    @SerializedName(DRIVERS)
    @SimpleSQLColumn("drivers")
    public int drivers;

    public Sector() { }

    public Sector(String id, String name, int drivers){
        this.id = id;
        this.name = name;
        this.drivers = drivers;
    }
}
