package com.example.roman.test;

import android.app.Application;
import android.content.SharedPreferences;

import com.example.roman.test.dagger.AppModule;
import com.example.roman.test.dagger.DaggerNetComponent;
import com.example.roman.test.dagger.NetComponent;
import com.example.roman.test.dagger.NetModule;

import javax.inject.Inject;

public class TaxiApp extends Application {
    private NetComponent mNetComponent;
    @Inject SharedPreferences prefs;

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
