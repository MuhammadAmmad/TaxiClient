package com.example.roman.test;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.roman.test.utilities.Functions;

import javax.inject.Inject;

public class SectorActivity extends AppCompatActivity {
    @Inject
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ((TaxiApp) getApplication()).getNetComponent().inject(this);
        Functions.setWholeTheme(this, prefs);

        String sectorId = Functions.getFromPreferences("sectorId", prefs);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sector);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.sectors_container, SectorFragment.newInstance(sectorId))
                    .commit();
        }
    }
}
