package com.example.roman.test;

import com.example.roman.test.dagger.AppModule;
import com.example.roman.test.dagger.DaggerNetComponent;
import com.example.roman.test.dagger.NetComponent;
import com.example.roman.test.dagger.NetModule;
import com.orm.SugarApp;

public class TaxiApp extends SugarApp {
    private NetComponent mNetComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        mNetComponent = DaggerNetComponent.builder()
                .netModule(new NetModule())
                .appModule(new AppModule(this))
                .build();
    }

    public NetComponent getNetComponent() {
        return mNetComponent;
    }
}
