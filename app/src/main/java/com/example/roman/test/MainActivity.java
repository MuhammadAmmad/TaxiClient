package com.example.roman.test;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.NavigationView;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.roman.test.socket.SocketService;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final int MAIN = 0;
    private static final int AIR = 1;
    private static final int ORDER = 2;

    MyPageAdapter mPageAdapter;
    private SocketServiceReceiver receiver;
    private SocketService mBoundService;

    String balance;
    private boolean mBound = true;

    private final ServiceConnection mConnection = new ServiceConnection() {
        //EDITED PART
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mBoundService = ((SocketService.LocalBinder)service).getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mBoundService = null;
        }
    };

    private class SocketServiceReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            int error = intent.getIntExtra(Utility.ERROR, Utility.DEFAULT);

            switch (error) {
                case Utility.DEFAULT:
                    int method = intent.getIntExtra(Utility.METHOD, Utility.DEFAULT);
                    switch (method) {
                        case Utility.METHOD_DELETE_ORDERS:
                            int deleteOrderId = intent.getIntExtra(
                                    Utility.RESPONSE, Utility.DEFAULT);
                            ((AirFragment) mPageAdapter.getRegisteredFragment(AIR))
                                    .removeOrder(deleteOrderId);
                            return;
                    }
                    break;
            }
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            Utility.setWholeTheme(this);
        }

        super.onCreate(savedInstanceState);

        Intent intent = new Intent(this, SocketService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);

        setContentView(R.layout.activity_main);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_awesome_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

//        setContentView(R.layout.activity_navigation_drawer);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        DrawerLayout mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(
                this,
                mDrawerLayout,
                null,
                R.string.nav_drawer_open,
                R.string.nav_drawer_close) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle("slow");
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle("fast");
            }
        };

        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        List<Fragment> fragments = getFragments();

        MyPageAdapter pageAdapter = new MyPageAdapter(getSupportFragmentManager(), fragments);
        ViewPager mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mViewPager.setAdapter(pageAdapter);
        mViewPager.setCurrentItem(AIR);
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

    private List<Fragment> getFragments() {
        List<Fragment> fList = new ArrayList<>();

        fList.add(MainFragment.newInstance());
        fList.add(AirFragment.newInstance());
        fList.add(OrderFragment.newInstance());

        return fList;
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

        public Fragment getRegisteredFragment(int position) {
            return fragments.get(position);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
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
//        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_main) {

        } else if (id == R.id.nav_my_orders) {

        } else if (id == R.id.nav_messages) {

        } else if (id == R.id.nav_gps_map) {

        } else if (id == R.id.nav_meter) {

        } else if (id == R.id.nav_add_functions) {

        } else if (id == R.id.nav_alarm) {
            showAlertDialog();
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

    private void showAlertDialog() {
        DialogFragment fragment = new AlertDialogFragment();
        fragment.show(getSupportFragmentManager(), "AlertDialogFragment");
    }
}
