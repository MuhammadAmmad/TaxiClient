package com.example.roman.test;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.roman.test.services.SocketService;
import com.example.roman.test.utilities.Constants;
import com.example.roman.test.utilities.Functions;

import org.json.JSONException;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity {
    private SocketServiceReceiver receiver;

    @BindView(R.id.login_progress)
    View mProgressView;

    @BindView(R.id.login_form)
    View mLoginFormView;

    @Inject
    SharedPreferences prefs;

    static {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.e("Some stuff", "Create");
        ((TaxiApp) getApplication()).getNetComponent().inject(this);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            Functions.setWholeTheme(this, prefs);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);

        Button mLoginButton = (Button) findViewById(R.id.action_sign_in);
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    attemptLogin(LoginActivity.this);
//                    showProgress(true);
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
                    boolean isNight = Functions.isNight(prefs);
                    String newState;

                    if (isNight) {
                        newState = Constants.DAY;
                    } else {
                        newState = Constants.NIGHT;
                    }

                    Functions.saveToPreferences(newState, Constants.THEME, prefs);
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
        startService(new Intent(this, SocketService.class));

        if (receiver == null) {
            receiver = new SocketServiceReceiver();
            IntentFilter intentFilter = new IntentFilter(Constants.LOGIN_INTENT);
            registerReceiver(receiver, intentFilter);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (receiver != null) {
            unregisterReceiver(receiver);
            receiver = null;
        }
    }

    static void attemptLogin(Context context) throws JSONException {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

        String login = prefs.getString(context.getString(R.string.pref_login_key),
                context.getString(R.string.pref_login_default));
        String password = prefs.getString(context.getString(R.string.pref_password_key),
                context.getString(R.string.pref_password_default));
        new UserLoginTask(login, password).execute();
    }

//    private void showProgress(final boolean show) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
//            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
//
//            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
//            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
//                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
//                @Override
//                public void onAnimationEnd(Animator animation) {
//                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
//                }
//            });
//
//            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
//            mProgressView.animate().setDuration(shortAnimTime).alpha(
//                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
//                @Override
//                public void onAnimationEnd(Animator animation) {
//                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
//                }
//            });
//        } else {
//            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
//            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
//        }
//    }

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
            int error = intent.getIntExtra(Constants.ERROR, Constants.DEFAULT);
            String errorMessage = "";

            switch (error) {
                case Constants.ERROR_NONE:
                    errorMessage = context.getString(R.string.login_error_success);
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    break;
                case Constants.ERROR_LOGIN_INCORRECT:
                    errorMessage = context.getString(R.string.login_error_incorrect);
                    break;
                case Constants.ERROR_LOGIN_BLOCKED:
                    errorMessage = context.getString(R.string.login_error_blocked);
                    break;
                case Constants.ERROR_LOGIN_OCCUPIED:
                    errorMessage = context.getString(R.string.login_error_occupied);
                    break;
                case Constants.ERROR_LOGIN_RADIO:
                    errorMessage = context.getString(R.string.login_error_radio);
                    break;
                case Constants.ERROR_LOGIN_TAKEN:
                    errorMessage = context.getString(R.string.login_error_taken);
                    break;
            }

            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
        }
    }

    public static class UserLoginTask extends AsyncTask<Void, Void, Boolean> {
        private final String mLogin;
        private final String mPassword;

        UserLoginTask(String login, String password) {
            mLogin = login;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                SocketService.getInstance().login(mLogin, mPassword);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return true;
        }
    }
}
