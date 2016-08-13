package com.example.roman.test.data;

import ckm.simple.sql_provider.UpgradeScript;
import ckm.simple.sql_provider.annotation.SimpleSQLConfig;

@SimpleSQLConfig(
        name = "SectorProvider",
        authority = "just.some.sector_provider.authority",
        database = "two.db",
        version = 1
)

public class SectorProviderConfig implements ckm.simple.sql_provider.annotation.ProviderConfig{
    @Override
    public UpgradeScript[] getUpdateScripts() {
        return new UpgradeScript[0];
    }
}
