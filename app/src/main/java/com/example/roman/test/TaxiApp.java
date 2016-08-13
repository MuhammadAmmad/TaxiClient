package com.example.roman.test;

import android.app.Application;

import com.example.roman.test.dagger.AppModule;
import com.example.roman.test.dagger.DaggerNetComponent;
import com.example.roman.test.dagger.NetComponent;
import com.example.roman.test.dagger.NetModule;

public class TaxiApp extends Application {
    private NetComponent mNetComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        mNetComponent = DaggerNetComponent.builder()
                .appModule(new AppModule(this))
                .netModule(new NetModule("ws://gw.staxi.com.ua:16999/test"))
                .build();
    }

    public NetComponent getNetComponent() {
        return mNetComponent;
    }
}
