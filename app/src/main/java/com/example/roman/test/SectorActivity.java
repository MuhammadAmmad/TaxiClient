package com.example.roman.test;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class SectorActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sector);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.sectors_container, SectorFragment.newInstance())
                    .commit();
        }
    }
}
