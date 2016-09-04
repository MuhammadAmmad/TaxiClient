package com.example.roman.test;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.roman.test.services.SocketService;
import com.example.roman.test.utilities.Constants;
import com.example.roman.test.utilities.Functions;

import org.json.JSONException;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.roman.test.utilities.Constants.LOGIN_INTENT;
import static com.example.roman.test.utilities.Constants.THEME;
import static com.example.roman.test.utilities.Functions.saveToPreferences;
import static com.example.roman.test.utilities.Functions.setLanguage;

public class LoginActivity extends AppCompatActivity {
    private SocketServiceReceiver receiver;

//    @BindView(R.id.login_progress)
//    View mProgressView;

    @BindView(R.id.login_form)
    View mLoginFormView;

    @BindView(R.id.login_version)
    TextView mVersionTextView;

    @BindView(R.id.login_call_sign)
    TextView mCallSignTextView;

    @Inject
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ((TaxiApp) getApplication()).getNetComponent().inject(this);
        Functions.setWholeTheme(this, prefs);
        setLanguage(this, prefs);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        final Context context = this;

        ButterKnife.bind(this);

        final Button mLoginButton = (Button) findViewById(R.id.action_sign_in);
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                final MediaPlayer mp = MediaPlayer.create(LoginActivity.this, R.raw.changestatus);
                mp.start();
                attemptLogin(getApplication());
            }
        });

        Button mStyleButton = (Button) findViewById(R.id.action_style);
        assert mStyleButton != null;
        mStyleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isNight = Functions.isNight(prefs);
                String newState;

                if (isNight) {
                    newState = Constants.DAY;
                } else {
                    newState = Constants.NIGHT;
                }

                saveToPreferences(newState, THEME, prefs);
                Functions.recreate(LoginActivity.this);
            }
        });

        final Button mSettingsButton = (Button) findViewById(R.id.action_setting);
        assert mSettingsButton != null;
        mSettingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, LoginSettingsActivity.class));
            }
        });

        try {
            mVersionTextView.setText(getString(R.string.format_version,
                    getPackageManager().getPackageInfo(getPackageName(), 0).versionName));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        mCallSignTextView.setText(getString(R.string.format_call_sign,
                preferences.getString(getString(R.string.pref_login_key),
                        getString(R.string.pref_login_default))));
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (receiver == null) {
            receiver = new SocketServiceReceiver();
            IntentFilter intentFilter = new IntentFilter(LOGIN_INTENT);
            registerReceiver(receiver, intentFilter);
        }
    }

    @Override
    protected void onResume() {
        startService(new Intent(this, SocketService.class));
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (receiver != null) {
            unregisterReceiver(receiver);
            receiver = null;
        }
    }

    public static void attemptLogin(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

        String login = prefs.getString(context.getString(R.string.pref_login_key), context.getString(R.string.pref_login_default));
        saveToPreferences(login, "login", prefs);
        String password = prefs.getString(context.getString(R.string.pref_password_key), context.getString(R.string.pref_password_default));
        saveToPreferences(password, "password", prefs);

        new UserLoginTask(login, password).execute();
    }

    private class SocketServiceReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            int error = intent.getIntExtra(Constants.ERROR, Constants.DEFAULT);
            String errorMessage = "";

            switch (error) {
                case Constants.ERROR_NONE:
                    errorMessage = context.getString(R.string.login_error_success);
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
