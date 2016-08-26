package com.example.roman.test;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;

import com.example.roman.test.data.Status;
import com.example.roman.test.services.SocketService;
import com.google.gson.Gson;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import static com.example.roman.test.utilities.Constants.DEFAULT;
import static com.example.roman.test.utilities.Constants.STATUS_ARRAY;

public class MainFragment extends PreferenceFragmentCompat {
    private Status[] mStatuses;

    @Inject Gson gson;
    @Inject SharedPreferences prefs;

    private AirFragment mAirFragment;

    static MainFragment newInstance() {
        return new MainFragment();
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.pref_main);
        ((TaxiApp) getActivity().getApplication()).getNetComponent().inject(this);

        String balance = prefs.getString("balance", String.valueOf(DEFAULT));
        Preference preference = findPreference("balance");
        preference.setTitle(getActivity().getString(R.string.format_balance, balance));

        String text = prefs.getString(STATUS_ARRAY, "[]");
        mStatuses = gson.fromJson(text, Status[].class);

        final ListPreference statusPreference = (ListPreference) findPreference(getString(R.string.pref_status_key));
        setStatusData(statusPreference);

        statusPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                try {
                    SocketService.getInstance().setDriverStatus((String) newValue);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return true;
            }
        });
    }

    private void setStatusData(ListPreference lp) {
        List<CharSequence> entries = new ArrayList<>();
        List<CharSequence> entryValues = new ArrayList<>();

        for (Status status : mStatuses) {
            if (status.canSet()) {
                entries.add(status.getName());
                entryValues.add(status.getId());
            }
        }

        lp.setEntries(entries.toArray(new CharSequence[0]));
        lp.setEntryValues(entryValues.toArray(new CharSequence[0]));
        try {
            SocketService.getInstance().getDriverStatus();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
