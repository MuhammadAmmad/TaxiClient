package com.example.roman.test;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.roman.test.services.SocketService;

import org.json.JSONException;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.os.Debug.startMethodTracing;

public class LoginActivity extends AppCompatActivity {
    private SocketServiceReceiver receiver;
    private SocketService mService;
    private boolean mBound;

    @BindView(R.id.login_progress)
    View mProgressView;

    @BindView(R.id.login_form)
    View mLoginFormView;
    static {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            Utility.setWholeTheme(this);
        }

        Log.e("Some stuff", "Create");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);

        Button mLoginButton = (Button) findViewById(R.id.action_sign_in);
        assert mLoginButton != null;
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    attemptLogin();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        Button mStyleButton = (Button) findViewById(R.id.action_style);
        assert mStyleButton != null;
        mStyleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentNightMode = getResources().getConfiguration().uiMode
                        & Configuration.UI_MODE_NIGHT_MASK;

                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                    boolean isNight = Utility.isNight(LoginActivity.this);
                    String newState;

                    if (isNight) {
                        newState = Utility.DAY;
                    } else {
                        newState = Utility.NIGHT;
                    }

                    getSharedPreferences(Utility.MY_PREFS_NAME, Context.MODE_PRIVATE)
                            .edit()
                            .putString(Utility.THEME, newState)
                            .apply();
                } else {
                    switch (currentNightMode) {
                        case Configuration.UI_MODE_NIGHT_NO:
                            getDelegate().setLocalNightMode(
                                    AppCompatDelegate.MODE_NIGHT_YES);
                            break;
                        case Configuration.UI_MODE_NIGHT_YES:
                            getDelegate().setLocalNightMode(
                                    AppCompatDelegate.MODE_NIGHT_NO);
                            break;
                        case Configuration.UI_MODE_NIGHT_UNDEFINED:
                            getDelegate().setLocalNightMode(
                                    AppCompatDelegate.MODE_NIGHT_YES);
                            break;
                    }
                }

                recreate();
            }
        });

        Button mSettingsButton = (Button) findViewById(R.id.action_setting);
        assert mSettingsButton != null;
        mSettingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, LoginSettingsActivity.class));
            }
        });
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

    private void attemptLogin() throws JSONException {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String login = prefs.getString(getString(R.string.pref_login_key), getString(R.string.pref_login_default));
        String password = prefs.getString(getString(R.string.pref_password_key), getString(R.string.pref_password_default));
        new UserLoginTask(login, password).execute();
    }

    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public void recreate() {
        if (Build.VERSION.SDK_INT >= 11) {
            super.recreate();
        } else {
            Intent intent = getIntent();
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            finish();
            startActivity(intent);
        }
    }

    private class SocketServiceReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            int error = intent.getIntExtra(Utility.ERROR, Utility.DEFAULT);
            String errorMessage = "";

            switch (error) {
                case Utility.ERROR_NONE:
                    errorMessage = context.getString(R.string.login_error_success);
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    break;
                case Utility.ERROR_LOGIN_INCORRECT:
                    errorMessage = context.getString(R.string.login_error_incorrect);
                    break;
                case Utility.ERROR_LOGIN_BLOCKED:
                    errorMessage = context.getString(R.string.login_error_blocked);
                    break;
                case Utility.ERROR_LOGIN_OCCUPIED:
                    errorMessage = context.getString(R.string.login_error_occupied);
                    break;
                case Utility.ERROR_LOGIN_RADIO:
                    errorMessage = context.getString(R.string.login_error_radio);
                    break;
                case Utility.ERROR_LOGIN_TAKEN:
                    errorMessage = context.getString(R.string.login_error_taken);
                    break;
            }

            Toast.makeText(context,
                    errorMessage, Toast.LENGTH_SHORT).show();
        }
    }

    private final ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            SocketService.LocalBinder binder = (SocketService.LocalBinder) service;
            mService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };

    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {
        private final String mLogin;
        private final String mPassword;

        UserLoginTask(String login, String password) {
            mLogin = login;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                mService.login(mLogin, mPassword);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return true;
        }
    }
}
