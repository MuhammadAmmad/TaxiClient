package com.example.roman.test.data;

import ckm.simple.sql_provider.annotation.SimpleSQLColumn;
import ckm.simple.sql_provider.annotation.SimpleSQLTable;

@SimpleSQLTable(
        table = "sectors",
        provider = "SectorProvider")
public class Sector {

    @SimpleSQLColumn(value = "col_id", primary = true)
    public int id;

    @SimpleSQLColumn("col_name")
    public String name;

    @SimpleSQLColumn("drivers")
    public int drivers;

    @SimpleSQLColumn("district")
    public String district;
}
