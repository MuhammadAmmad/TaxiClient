package com.example.roman.test;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;

import com.example.roman.test.utilities.Functions;

import javax.inject.Inject;

import static com.example.roman.test.DetailOrderFragment.DETAIL_ORDER;
import static com.example.roman.test.utilities.Constants.ORDER_ID;

public class DetailOrderActivity extends AppCompatActivity {
    @Inject
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ((TaxiApp) getApplication()).getNetComponent().inject(this);
        Functions.setWholeTheme(this,
                PreferenceManager.getDefaultSharedPreferences(getApplication()));

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState == null) {
            Bundle arguments = new Bundle();
            arguments.putString(DETAIL_ORDER, getIntent().getStringExtra(DETAIL_ORDER));
            arguments.putString(ORDER_ID, getIntent().getStringExtra(ORDER_ID));

            DetailOrderFragment fragment = DetailOrderFragment.newInstance();
            fragment.setArguments(arguments);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, fragment)
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.order, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        } else if (id == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }

        return true;
    }
}