package com.example.roman.test;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;

import com.example.roman.test.data.Message;
import com.example.roman.test.data.Order;
import com.example.roman.test.socket.SocketService;
import com.google.gson.Gson;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final int AIR = 1;

    private MyPageAdapter mPageAdapter;
    private SocketServiceReceiver receiver;
    private SocketService mBoundService;
    private AirFragment mAirFragment;

    @Inject
    Gson gson;

    private boolean mBound = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            Utility.setWholeTheme(this);
        }

        super.onCreate(savedInstanceState);

        ((TaxiApp) getApplication()).getNetComponent().inject(this);

        Intent intent = new Intent(this, SocketService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);

        setContentView(R.layout.activity_main);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_awesome_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        DrawerLayout mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(
                this,
                mDrawerLayout,
                null,
                R.string.nav_drawer_open,
                R.string.nav_drawer_close) {
        };

        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        List<Fragment> fragments = getFragments();

        mPageAdapter = new MyPageAdapter(getSupportFragmentManager(), fragments);
        ViewPager mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mViewPager.setAdapter(mPageAdapter);
        mViewPager.setCurrentItem(AIR);

        mAirFragment = (AirFragment) mPageAdapter.getRegisteredFragment(AIR);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, SocketService.class);
        startService(new Intent(this, SocketService.class));
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);

        if (receiver == null) {
            receiver = new SocketServiceReceiver();
            IntentFilter intentFilter = new IntentFilter(Utility.LOGIN_INTENT);
            registerReceiver(receiver, intentFilter);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mBound) {
            unbindService(mConnection);
            mBound = false;
        }

        if (receiver != null) {
            unregisterReceiver(receiver);
            receiver = null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (receiver == null) {
            receiver = new SocketServiceReceiver();
            IntentFilter intentFilter = new IntentFilter(Utility.MAIN_INTENT);
            registerReceiver(receiver, intentFilter);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }

        super.onBackPressed();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent e) {
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            if (!drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.openDrawer(GravityCompat.START);
            }
            return true;
        }

        return super.onKeyDown(keyCode, e);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_action_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_main) {

//        } else if (id == R.id.nav_my_orders) {
//
//        } else if (id == R.id.nav_messages) {
//
//        } else if (id == R.id.nav_gps_map) {
//
//        } else if (id == R.id.nav_meter) {
//
//        } else if (id == R.id.nav_add_functions) {

        } else if (id == R.id.nav_alarm) {
            showAlert();
        } else if (id == R.id.nav_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
        } else if (id == R.id.nav_exit) {
            try {
                mBoundService.logout();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            System.exit(1);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private class SocketServiceReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            int error = intent.getIntExtra(Utility.ERROR, Utility.DEFAULT);

            switch (error) {
                case Utility.ERROR_NONE:
                    int method = intent.getIntExtra(Utility.METHOD, Utility.DEFAULT);
                    switch (method) {
                        case Utility.METHOD_GET_ORDERS:
                            Order[] orders = gson.fromJson(intent.getStringExtra(Utility.RESPONSE), Order[].class);
                            mAirFragment.addOrders(orders);
                            break;
                        case Utility.METHOD_NEW_ORDER:
                            Order order = gson.fromJson(intent.getStringExtra(Utility.RESPONSE), Order.class);
                            mAirFragment.addOrder(order);
                            break;
                        case Utility.METHOD_DELETE_ORDER:
                            String delOrderId = intent.getStringExtra(Utility.RESPONSE);
                            mAirFragment.removeOrder(delOrderId);
                            break;
                        case Utility.METHOD_NEW_MESSAGE:
                            Message msg = gson.fromJson(intent.getStringExtra(Utility.RESPONSE), Message.class);
                            showMessage(msg);
                            break;
                    }
            }
        }
    }

    private class MyPageAdapter extends FragmentPagerAdapter {
        private final List<Fragment> fragments;

        MyPageAdapter(FragmentManager fm, List<Fragment> fragments) {
            super(fm);
            this.fragments = fragments;
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getString(R.string.main_main);
                case 1:
                    return getString(R.string.main_air);
                case 2:
                    return getString(R.string.main_order);
                default:
                    return "";
            }
        }

        Fragment getRegisteredFragment(int position) {
            return fragments.get(position);
        }
    }

    private final ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mBoundService = ((SocketService.LocalBinder)service).getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mBoundService = null;
        }
    };

    private List<Fragment> getFragments() {
        List<Fragment> fList = new ArrayList<>();

        fList.add(MainFragment.newInstance());
        fList.add(AirFragment.newInstance());
        fList.add(OrderFragment.newInstance());

        return fList;
    }

    private void showAlert() {
        AlertDialog.Builder helpBuilder = Utility.getDialog(this,
                getString(R.string.alert_fire),
                getString(R.string.alert_message));

        helpBuilder.setPositiveButton(getString(R.string.alert_fire),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        try {
                            mBoundService.alert();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }).setNegativeButton(getString(R.string.alert_cancel),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).create().show();
    }

    private void showMessage(Message message) {
        if (message != null) {
            String text = message.getMessage();
            AlertDialog.Builder helpBuilder = Utility.getDialog(this, "New message", text);

            helpBuilder.setPositiveButton("Positive",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    }).create().show();
        }
    }
}
