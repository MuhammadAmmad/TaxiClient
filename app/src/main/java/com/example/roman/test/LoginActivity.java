package com.example.roman.test;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.roman.test.services.SocketService;
import com.example.roman.test.utilities.Constants;
import com.example.roman.test.utilities.Functions;
import com.example.roman.test.utilities.HelperClasses;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.roman.test.utilities.Constants.LOGIN_INTENT;
import static com.example.roman.test.utilities.Constants.NIGHT;
import static com.example.roman.test.utilities.Constants.THEME;
import static com.example.roman.test.utilities.Functions.isNetworkConnected;
import static com.example.roman.test.utilities.Functions.saveToPreferences;
import static com.example.roman.test.utilities.Functions.setLanguage;

public class LoginActivity extends AppCompatActivity {
    private SocketServiceReceiver receiver;

    @BindView(R.id.login_form)
    View mLoginFormView;

    @BindView(R.id.login_version)
    TextView mVersionTextView;

    @BindView(R.id.login_call_sign)
    TextView mCallSignTextView;

    @BindView(R.id.action_sign_in)
    Button loginButton;

    @BindView(R.id.action_style)
    Button styleButton;

    @BindView(R.id.action_setting)
    Button settingsButton;

    @Inject SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ((TaxiApp) getApplication()).getNetComponent().inject(this);
        Functions.setWholeTheme(this, prefs);
        setLanguage(this, prefs);

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNetworkConnected(getApplication())) {
                    requestInternet();
                } else {
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    startService(new Intent(LoginActivity.this, SocketService.class));
                }
            }
        });

        styleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isNight = Functions.isNight(prefs);
                String newState;

                if (isNight) {
                    newState = Constants.DAY;
                } else {
                    newState = NIGHT;
                }

                saveToPreferences(newState, THEME, prefs);
                Functions.recreate(LoginActivity.this);
            }
        });

        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, LoginSettingsActivity.class));
            }
        });

        try {
            mVersionTextView.setText(getString(R.string.format_version,
                    getPackageManager().getPackageInfo(getPackageName(), 0).versionName));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (receiver == null) {
            receiver = new SocketServiceReceiver();
            IntentFilter intentFilter = new IntentFilter(LOGIN_INTENT);
            registerReceiver(receiver, intentFilter);
        }

        String callSign = getString(R.string.format_call_sign,
                prefs.getString(getString(R.string.pref_login_key),
                        getString(R.string.pref_login_default)));
        mCallSignTextView.setText(callSign);
    }

    @Override
    protected void onStop()  {
        super.onStop();

        if (receiver != null) {
            unregisterReceiver(receiver);
            receiver = null;
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

    private void requestInternet() {
        HelperClasses.MyDialogFragment.newInstance(LoginActivity.this,
                getString(R.string.internet_request_title),
                getString(R.string.internet_request_text))
                .setPositiveButton(getString(android.R.string.ok),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                .create()
                .show();
    }
}
