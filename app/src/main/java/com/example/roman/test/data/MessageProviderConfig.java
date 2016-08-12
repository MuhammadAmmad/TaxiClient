package com.example.roman.test.data;

import ckm.simple.sql_provider.UpgradeScript;
import ckm.simple.sql_provider.annotation.SimpleSQLConfig;

@SimpleSQLConfig(
        name = "MessageProvider",
        authority = "just.some.message_provider.authority",
        database = "taxi.db",
        version = 1
)

public class MessageProviderConfig implements ckm.simple.sql_provider.annotation.ProviderConfig{
    @Override
    public UpgradeScript[] getUpdateScripts() {
        return new UpgradeScript[0];
    }
}
