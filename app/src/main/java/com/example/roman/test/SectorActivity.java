package com.example.roman.test;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.roman.test.services.SocketService;
import com.example.roman.test.utilities.Functions;

import org.json.JSONException;

import javax.inject.Inject;

public class SectorActivity extends AppCompatActivity {
    @Inject
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ((TaxiApp) getApplication()).getNetComponent().inject(this);
        Functions.setWholeTheme(this, prefs);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sector);

        try {
            SocketService.getInstance().getSectors("22");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.sectors_container, SectorFragment.newInstance())
                    .commit();
        }
    }
}
