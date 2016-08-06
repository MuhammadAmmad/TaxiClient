package com.example.roman.test;

import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * Created by Roman on 05/08/2016.
 */

public class SettingActivity extends PreferenceActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }
}
