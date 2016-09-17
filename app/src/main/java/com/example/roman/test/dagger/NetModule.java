package com.example.roman.test.dagger;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class NetModule {
    public NetModule() { }

    @Provides
    @Singleton
    SharedPreferences providesSharedPreferences(Application application) {
        return PreferenceManager.getDefaultSharedPreferences(application.getApplicationContext());
    }

    @Provides
    @Singleton
    Gson providesGson() {
        return new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
    }
}