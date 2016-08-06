package com.example.roman.test;

import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;

public class MainFragment extends PreferenceFragmentCompat{
    static MainFragment newInstance() {
        MainFragment f = new MainFragment();
        Bundle bd1 = new Bundle(0);
        f.setArguments(bd1);
        return f;
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.pref_main);
    }
}
