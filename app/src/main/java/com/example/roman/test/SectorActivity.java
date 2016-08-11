package com.example.roman.test;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class SectorActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            Utility.setWholeTheme(this);
        }

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sector);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.sectors_container, SectorFragment.newInstance())
                    .commit();
        }
    }
}
