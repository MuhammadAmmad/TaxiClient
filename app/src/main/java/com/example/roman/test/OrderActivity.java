package com.example.roman.test;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.example.roman.test.utilities.Functions;

import static com.example.roman.test.DetailOrderFragment.DETAIL_ORDER;

public class OrderActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            Functions.setWholeTheme(this);
        }

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_order);
        if (savedInstanceState == null) {
            Bundle arguments = new Bundle();
            arguments.putString(DETAIL_ORDER, getIntent().getStringExtra(DETAIL_ORDER));

            DetailOrderFragment fragment = DetailOrderFragment.newInstance();
            fragment.setArguments(arguments);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, fragment)
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.order, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return true;
    }
}