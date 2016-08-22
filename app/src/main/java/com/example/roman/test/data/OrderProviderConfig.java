package com.example.roman.test.data;

import ckm.simple.sql_provider.UpgradeScript;
import ckm.simple.sql_provider.annotation.SimpleSQLConfig;

@SimpleSQLConfig(
        name = "OrderProvider",
        authority = "just.some.provider_order.authority",
        database = "three.db",
        version = 1
)

public class OrderProviderConfig implements ckm.simple.sql_provider.annotation.ProviderConfig{
    @Override
    public UpgradeScript[] getUpdateScripts() {
        return new UpgradeScript[0];
    }
}


