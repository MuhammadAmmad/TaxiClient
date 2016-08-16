package com.example.roman.test;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceManager;

import com.example.roman.test.utilities.Constants;

public class MainFragment extends PreferenceFragmentCompat {

    static MainFragment newInstance() {
        MainFragment f = new MainFragment();
        Bundle bd1 = new Bundle(0);
        f.setArguments(bd1);
        return f;
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.pref_main);
        String balance = (getActivity().getSharedPreferences(
                Constants.MY_PREFS_NAME, Context.MODE_PRIVATE)).getString("balance", "Unknown");
        Preference preference = findPreference("balance");
        preference.setTitle(getActivity().getString(R.string.format_balance, balance));

    }

    private void setStatusData(ListPreference lp) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());


        CharSequence[] entries = { "English", "French" };
        CharSequence[] entryValues = {"1" , "2"};
        lp.setEntries(entries);
        lp.setDefaultValue("1");
        lp.setEntryValues(entryValues);
    }
}
