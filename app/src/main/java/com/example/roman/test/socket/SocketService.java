package com.example.roman.test.socket;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.roman.test.AirFragment;
import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketExtension;
import com.neovisionaries.ws.client.WebSocketFactory;

import java.io.IOException;

public class SocketService extends Service {
    private static final String LOG_TAG = SocketService.class.getSimpleName();
    public static final String BROADCAST_ACTION = "com.supun.broadcasttest";
    private static final String SERVER = "ws://gw.staxi.com.ua:16999/test";
    private static final int TIMEOUT = 5000;
    private WebSocket webSocket;

    private final IBinder myBinder = new LocalBinder();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        Log.e(LOG_TAG,  "I am in Ibinder onBind method");
        return myBinder;
    }

    public class LocalBinder extends Binder {
        public SocketService getService() {
            Log.e(LOG_TAG, "I am in Localbinder ");
            return SocketService.this;
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        new Thread(new connectSocket()).start();
        return START_STICKY;
    }

    private WebSocket connect() throws IOException, WebSocketException {
        return new WebSocketFactory()
                .setConnectionTimeout(TIMEOUT)
                .createSocket(SERVER)
                .addListener(new WebSocketAdapter() {

                    @Override
                    public void onTextMessage(WebSocket webSocket, String message) {
                        Log.e(LOG_TAG, message);

                        Intent broadcastIntent = new Intent();
                        broadcastIntent.setAction(AirFragment.mBroadcastStringAction);
                        broadcastIntent.putExtra("Data", "Broadcast Data");
                        sendBroadcast(broadcastIntent);
                    }
                })
                .addExtension(WebSocketExtension.PERMESSAGE_DEFLATE)
                .connect();
    }

    class connectSocket implements Runnable {
        @Override
        public void run() {
            try {
                webSocket = connect();
                try {
                    webSocket.sendText("{\"RQ\":{\"L\":\"3\",\"P\":\"3\"},\"M\":102}");
                }
                catch (Exception e) {
                    Log.e("TCP", "S: Error", e);
                }
            } catch (Exception e) {
                Log.e("TCP", "C: Error", e);
            }
        }
    }

    public String getMessage(String message) {
        return message;
    }

    public interface Callbacks {
        public void updateAir(String message);
    }
}
