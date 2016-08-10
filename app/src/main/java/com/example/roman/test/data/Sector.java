package com.example.roman.test.data;

import org.json.JSONException;
import org.json.JSONObject;

import ckm.simple.sql_provider.annotation.SimpleSQLColumn;
import ckm.simple.sql_provider.annotation.SimpleSQLTable;

@SimpleSQLTable(
        table = "sectors",
        provider = "SectorProvider")
public class Sector {
    @SimpleSQLColumn(value = "col_id", primary = true)
    public int id;

    @SimpleSQLColumn("name")
    public String name;

    @SimpleSQLColumn("drivers")
    public int numOfDrivers;

    public Sector() {
        id = 0;
        name = "name";
        numOfDrivers = 4;
    }

    public Sector(JSONObject sector) throws JSONException {
        final String id = "I";
        final String name = "N";
        final String numOfDrivers = "P";

        this.id = sector.getInt(id);
        this.name = sector.getString(name);
        this.numOfDrivers = sector.getInt(numOfDrivers);
    }
}
