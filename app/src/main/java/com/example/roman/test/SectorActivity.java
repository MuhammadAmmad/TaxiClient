package com.example.roman.test;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.roman.test.services.SocketService;
import com.example.roman.test.utilities.Functions;

import org.json.JSONException;

import javax.inject.Inject;

import static com.example.roman.test.utilities.Constants.SECTOR_HASH;

public class SectorActivity extends AppCompatActivity {
    @Inject
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ((TaxiApp) getApplication()).getNetComponent().inject(this);
        Functions.setWholeTheme(this, prefs);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sector);

        String sectorId = prefs.getString("sectorId", "-1");
        if (!sectorId.equals("-1")) {
            try {
                SocketService.getInstance().getSectors(sectorId);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.sectors_container, SectorFragment.newInstance(sectorId))
                    .commit();
        }
    }
}
