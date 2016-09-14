package com.example.roman.test;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceManager;

import com.example.roman.test.data.DriverStatus;
import com.example.roman.test.services.SocketService;
import com.google.gson.Gson;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import static com.example.roman.test.utilities.Constants.SECTOR_HASH;

public class MainFragment extends PreferenceFragmentCompat {
    int index;
    String mBalance;
    List<DriverStatus> mStatuses;
    ListPreference mStatusPreference;
    Preference mBalancePreference;

    @Inject Gson gson;
    @Inject SharedPreferences prefs;

    static MainFragment newInstance() {
        return new MainFragment();
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        if (savedInstanceState == null) {
            PreferenceManager.getDefaultSharedPreferences(getActivity()).edit().clear().apply();
        }

        addPreferencesFromResource(R.xml.pref_main);
        ((TaxiApp) getActivity().getApplication()).getNetComponent().inject(this);

        String balance = prefs.getString(getString(R.string.pref_balance_key), "0,00 грн");
        mBalancePreference = findPreference(getString(R.string.pref_balance_key));
        if (savedInstanceState != null && savedInstanceState.containsKey("balance")) {
            setBalance();
        } else {
            mBalancePreference.setTitle(getActivity().getString(R.string.format_balance, balance));
        }

        mStatusPreference = (ListPreference) findPreference(getString(R.string.pref_status_key));
        if (savedInstanceState != null && savedInstanceState.containsKey("status")) {
            mStatusPreference.setTitle(savedInstanceState.getString("status"));
            setStatusData();
        }
        mStatusPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                index = mStatusPreference.findIndexOfValue((String) newValue);
                try {
                    SocketService.getInstance().setDriverStatus((String) newValue);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return true;
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("status", (String) mStatusPreference.getTitle());
        outState.putString("balance", (String) mBalancePreference.getTitle());
        super.onSaveInstanceState(outState);
    }

    public void setStatusData() {
        List<CharSequence> entries = new ArrayList<>();
        List<CharSequence> entryValues = new ArrayList<>();

        mStatuses = DriverStatus.listAll(DriverStatus.class);

        for (DriverStatus driverStatus : mStatuses) {
            if (driverStatus.canSet()) {
                entries.add(driverStatus.getName());
                entryValues.add(driverStatus.getStatusId());
            }
        }

        mStatusPreference.findIndexOfValue(mStatusPreference.getValue());
        mStatusPreference.setEntries(entries.toArray(new CharSequence[0]));
        mStatusPreference.setEntryValues(entryValues.toArray(new CharSequence[0]));
//        try {
//            SocketService.getInstance().getDriverStatus();
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
    }

    public void setBalance() {
        mBalance = prefs.getString(getString(R.string.pref_balance_key), "0.00 грн");
        Preference preference = findPreference(getString(R.string.pref_balance_key));
        preference.setTitle(getActivity().getString(R.string.format_balance, mBalance));
    }
}
