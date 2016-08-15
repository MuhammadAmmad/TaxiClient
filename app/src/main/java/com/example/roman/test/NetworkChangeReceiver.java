package com.example.roman.test;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkChangeReceiver extends BroadcastReceiver {
    private SnackInterface callback;
    private boolean isConnnected = true;

    @Override
    public void onReceive(Context context, Intent intent) {
        callback = (MainActivity) context;
        isNetworkAvailable(context);
    }

    public void registerReceiver(SnackInterface snackInterface) {
        callback = snackInterface;
    }

    private boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (NetworkInfo networkInfo : info) {
                    if (networkInfo.getState().equals(NetworkInfo.State.CONNECTED) ) {
                        if (!isConnnected) {
                            callback.reconnect(true);
                            isConnnected = true;
                        }
                        return true;
                    }
                }
            }
        }

        callback.reconnect(false);
        isConnnected = false;
        return false;
    }

    public interface SnackInterface {
        void reconnect(boolean isConnected);
    }
}
