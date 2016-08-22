package com.example.roman.test;

import android.Manifest;
import android.app.ActivityManager;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.roman.test.data.Message;
import com.example.roman.test.data.MessagesTable;
import com.example.roman.test.data.Order;
import com.example.roman.test.data.Sector;
import com.example.roman.test.services.SocketService;
import com.example.roman.test.utilities.Constants;
import com.example.roman.test.utilities.Functions;
import com.google.gson.Gson;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.roman.test.DetailOrderFragment.DETAIL_ORDER;
import static com.example.roman.test.LoginActivity.attemptLogin;
import static com.example.roman.test.utilities.Constants.ERROR_NONE;
import static com.example.roman.test.utilities.Constants.METHOD_GET_NEW_STATUS;
import static com.example.roman.test.utilities.Constants.METHOD_GET_ORDERS;
import static com.example.roman.test.utilities.Constants.METHOD_NEW_ORDER;
import static com.example.roman.test.utilities.Constants.RESPONSE;
import static com.example.roman.test.utilities.Constants.STATUS_ARRAY;
import static com.example.roman.test.utilities.Functions.getSectorList;
import static com.example.roman.test.utilities.Functions.getStreetFromLocation;
import static com.example.roman.test.utilities.Functions.isBetterLocation;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        NetworkChangeReceiver.SnackInterface,
        AirFragment.Callback {

    private static final int REQUEST_LOCATION = 1;
    private static final int AIR = 1;

    private Menu mMenu;
    private Location mCurrentBestLocation;
    private NetworkChangeReceiver mNetworkReceiver;
    private SocketServiceReceiver mReceiver;
    private LocationManager mLocationManager;
    private LocationListener mLocationListener;
    ActionBarDrawerToggle mDrawerToggle;

    @Inject Gson gson;
    @Inject SharedPreferences prefs;

    private AirFragment mAirFragment;


    @BindView(R.id.nav_view)
    NavigationView navigationView;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

//    @BindView(R.id.view_pager)
//    ViewPager mViewPager;

    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        ((TaxiApp) getApplication()).getNetComponent().inject(this);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            Functions.setWholeTheme(this, prefs);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mDrawerToggle = new ActionBarDrawerToggle(
                this, mDrawer, toolbar,
                R.string.nav_drawer_open,
                R.string.nav_drawer_close) {
        };

        mDrawer.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        mAirFragment = AirFragment.newInstance();

        // TODO set true to main nav item
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.flContent, mAirFragment)
                .commit();

//        MyPageAdapter mPageAdapter = new MyPageAdapter(getSupportFragmentManager(), getFragments());
//
//        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//                expandToolbar();
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//                if (state == ViewPager.SCROLL_STATE_DRAGGING) {
//                    expandToolbar();
//                }
//            }
//        });
//        mViewPager.setAdapter(mPageAdapter);
//        mViewPager.setCurrentItem(AIR);
//        mAirFragment = (AirFragment) mPageAdapter.getRegisteredFragment(AIR);
//        tabLayout.setupWithViewPager(mViewPager);

        if (savedInstanceState == null) {
            // Acquire a reference to the system Location Manager
            mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

            // Define a listener that responds to location updates
            mLocationListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    if (isBetterLocation(location, mCurrentBestLocation)) {
                        mCurrentBestLocation = location;
                        String address = getStreetFromLocation(MainActivity.this, location);
                        if (mMenu != null) {
                            mMenu.findItem(R.id.address).setTitle(address);
                        }
                    }
                }

                @Override
                public void onStatusChanged(String s, int i, Bundle bundle) {
                }

                @Override
                public void onProviderEnabled(String s) {
                    Toast.makeText(MainActivity.this,
                            "Provider enabled: " + s, Toast.LENGTH_SHORT)
                            .show();
                }

                @Override
                public void onProviderDisabled(String s) {
                    Toast.makeText(MainActivity.this,
                            "Provider disabled: " + s, Toast.LENGTH_SHORT)
                            .show();
                }
            };

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_COARSE_LOCATION) !=
                            PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
            }

            mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, mLocationListener);
            mLocationManager.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, 0, 0, mLocationListener);

            new StartUpTask().execute();
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (mReceiver == null) {
            mReceiver = new SocketServiceReceiver();
            IntentFilter intentFilter = new IntentFilter(Constants.MAIN_INTENT);
            registerReceiver(mReceiver, intentFilter);
        }

        if (mNetworkReceiver == null) {
            mNetworkReceiver = new NetworkChangeReceiver();
            IntentFilter intentNetworkFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
            registerReceiver(mNetworkReceiver, intentNetworkFilter);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mReceiver != null) {
            unregisterReceiver(mReceiver);
            mReceiver = null;
        }

        if (mNetworkReceiver != null) {
            unregisterReceiver(mNetworkReceiver);
            mNetworkReceiver = null;
        }
    }

    @Override
    protected void onDestroy() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        } else {
            mLocationManager.removeUpdates(mLocationListener);
        }

        super.onDestroy();
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
        mMenu = menu;
        getMenuInflater().inflate(R.menu.main_action_bar, menu);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            return true;
        Location location = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        if (location == null) {
            location = mLocationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
        }

        if (location != null) {
            mMenu.findItem(R.id.address).setTitle(getStreetFromLocation(this, location));
        }

        return true;
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = null;
        Class fragmentClass = null;

        switch(id) {
            case R.id.nav_main:
                fragmentManager
                        .beginTransaction()
                        .replace(R.id.flContent, mAirFragment)
                        .commit();
                return true;
            case R.id.nav_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            case R.id.nav_messages:
                fragmentClass = MessageFragment.class;
                break;
            case R.id.nav_alarm:
                showAlert();
            case R.id.nav_exit:
                try {
                    SocketService.getInstance().logout();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                System.exit(1);
                break;
        }

        try {
            if (fragmentClass != null) {
                fragment = (Fragment) fragmentClass.newInstance();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();

        item.setChecked(true);
//        setTitle(item.getTitle());

        mDrawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void reconnect(boolean isConnected) {
        View view = findViewById(android.R.id.content);

        if (isConnected) {
            final Snackbar snackBar = Snackbar.make(view, "Connection established", Snackbar.LENGTH_INDEFINITE);
            snackBar.setAction("GO ONLINE", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        attemptLogin(MainActivity.this);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    snackBar.dismiss();
                    recreate();
                }
            });
            snackBar.show();
        } else {
            Snackbar.make(view, "No internet connection", Snackbar.LENGTH_INDEFINITE).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawer.openDrawer(GravityCompat.START);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(Order order) {
        Intent intent = new Intent(this, DetailOrderActivity.class);
        intent.putExtra(DETAIL_ORDER, gson.toJson(order));
        startActivity(intent);
    }

    private class MyPageAdapter extends FragmentStatePagerAdapter {
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

    private List<Fragment> getFragments() {
        List<Fragment> fList = new ArrayList<>();

        fList.add(MainFragment.newInstance());
        fList.add(AirFragment.newInstance());
        fList.add(OrderFragment.newInstance());

        return fList;
    }

    private void showAlert() {
        AlertDialog.Builder helpBuilder = Functions.getDialog(this,
                getString(R.string.alert_fire),
                getString(R.string.alert_message));

        helpBuilder.setPositiveButton(getString(R.string.alert_fire),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        try {
                            SocketService.getInstance().alert();
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

    public void showMessage(final Message message) {
        Bundle bundle = new Bundle();
        bundle.putString("MSG", message.toString());
        MyDialogFragment dialogFragment = new MyDialogFragment();
        dialogFragment.setArguments(bundle);
        dialogFragment.show(getSupportFragmentManager(), "tag");
    }

    public static class MyDialogFragment extends DialogFragment {
        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            Bundle bundle = getArguments();
            String text = bundle.getString("MSG");

            return new AlertDialog.Builder(getActivity())
                    .setTitle("New message")
                    .setMessage(text)
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    }).create();
        }
    }

//    private void expandToolbar() {
//        ((AppBarLayout) findViewById(R.id.app_bar_layout)).setExpanded(true);
//    }

    private void newMessage(Message message) {
        ActivityManager activityManager = (ActivityManager)
                getApplication().getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> services = activityManager.getRunningTasks(1);
        boolean isActivityFound = false;
        getContentResolver().insert(MessagesTable.CONTENT_URI,
                MessagesTable.getContentValues(message, false));

        if (services.get(0).topActivity.getPackageName()
                .equalsIgnoreCase(getApplicationContext().getPackageName())) {
            isActivityFound = true;
        }

        if (isActivityFound) {
            showMessage(message);
        } else {
            Functions.showMessageNotification(this, message);
        }
    }

    @Override
    public void recreate() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            super.recreate();
        } else {
            Intent intent = getIntent();
            finish();
            startActivity(intent);
        }
    }

    public void showSectorInfo() {
        List<Sector> sectors = getSectorList(this);
        StringBuilder textMessage = new StringBuilder("");

        for (Sector sector : sectors) {
            if (sector.getDrivers() != 0) {
                textMessage
                        .append(sector.getName())
                        .append(": ")
                        .append(sector.getDrivers())
                        .append("\n");
            }
        }

        newMessage(new Message("1", textMessage.toString(), "19/08/2016"));
    }

    private class SocketServiceReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            new HandleRequestTask().execute(intent);
        }
    }

    private static class StartUpTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            try {
                SocketService socketService = SocketService.getInstance();
                socketService.getOrders();
                socketService.getDriverStatus();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public class HandleRequestTask extends AsyncTask<Intent, Void, Void> {

        @Override
        protected Void doInBackground(Intent... intents) {
            Intent intent = intents[0];
            int error = intent.getIntExtra(Constants.ERROR, Constants.DEFAULT);

            switch (error) {
                case ERROR_NONE:
                    int method = intent.getIntExtra(Constants.METHOD, Constants.DEFAULT);
                    switch (method) {
                        case METHOD_GET_ORDERS:
                            String orderList = intent.getStringExtra(RESPONSE);
                            final Order[] orders = gson.fromJson(orderList, Order[].class);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mAirFragment.addOrders(orders);
                                }
                            });
                            break;

                        case METHOD_NEW_ORDER:
                            Order order = gson.fromJson(intent.getStringExtra(RESPONSE), Order.class);
                            mAirFragment.addOrder(order);
                            break;

                        case METHOD_GET_NEW_STATUS:
                            String id = intent.getStringExtra(RESPONSE);

                            String text = prefs.getString(STATUS_ARRAY, "[]");
                            com.example.roman.test.data.Status[] statuses =
                                    gson.fromJson(text, com.example.roman.test.data.Status[].class);
                            for (com.example.roman.test.data.Status status : statuses) {
                                if (status.getId().equals(id)) {
                                    break;
                                }
                            }
                            break;

                        case Constants.METHOD_DELETE_ORDER:
                            String delOrderId = intent.getStringExtra(RESPONSE);
                            mAirFragment.removeOrder(delOrderId);
                            break;

                        case Constants.METHOD_NEW_MESSAGE:
                            Message msg = gson.fromJson(intent.getStringExtra(RESPONSE), Message.class);
                            showMessage(msg);
                            break;
                    }
            }
            return null;
        }
    }
}
