package com.example.roman.test.socket;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.roman.test.Utility;
import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketExtension;
import com.neovisionaries.ws.client.WebSocketFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class SocketService extends Service {
    private static final String LOG_TAG = SocketService.class.getSimpleName();
    private static final String SERVER = "ws://gw.staxi.com.ua:16999/test";
    private static final int TIMEOUT = 5000;
    private int id;
    private WebSocket webSocket;

    private final IBinder myBinder = new LocalBinder();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return myBinder;
    }

    public class LocalBinder extends Binder {
        public SocketService getService() {
            return SocketService.this;
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        (new ConnectToServer()).execute();
        return START_STICKY;
    }

    private WebSocket connect() throws IOException, WebSocketException {
        return new WebSocketFactory()
                .setConnectionTimeout(TIMEOUT)
                .createSocket(SERVER)
                .addListener(new SocketListener())
                .addExtension(WebSocketExtension.PERMESSAGE_DEFLATE)
                .connect();
    }

    public void sendMessage(final JSONObject object, final int method) throws JSONException {
        final JSONObject json = Utility.getRQObject();
        json.put(Utility.REQUEST, object);
        json.put(Utility.METHOD, method);
        (new SendText()).execute(json.toString());
    }

    public void login(String username, String password) throws JSONException {
        final String LOGIN = "L";
        final String PASSWORD = "P";

        JSONObject json = new JSONObject();
        json.put(LOGIN, username);
        json.put(PASSWORD, password);

        sendMessage(json, Utility.METHOD_LOGIN);
    }

    public void logout() throws JSONException {
        JSONObject json = new JSONObject();
        json.put(Utility.DG, id);
        sendMessage(json, Utility.METHOD_LOGOUT);
    }

    private class SocketListener extends WebSocketAdapter {
        @Override
        public void onTextMessage(WebSocket webSocket, String message) throws JSONException {
            Log.e(LOG_TAG, message);
            Intent broadcastIntent = new Intent();

            JSONObject object = new JSONObject(message);
            int error = Utility.getError(object);
            switch (error) {
                case Utility.ERROR_NONE:
                    int type = Utility.getMethod(object);

                    switch (type) {
                        case Utility.METHOD_LOGIN: {
                            id = object.getInt(Utility.RESPONSE);
                            broadcastIntent.setAction(Utility.LOGIN_INTENT);
                            broadcastIntent.putExtra(Utility.METHOD, Utility.METHOD_LOGIN);
                            break;
                        }
                        case Utility.METHOD_LOGOUT: {
                            broadcastIntent.putExtra(Utility.METHOD, Utility.METHOD_LOGOUT);
                            break;
                        }
                    }
                    break;

                case Utility.ERROR_LOGIN_FAILED: {
                    broadcastIntent.setAction(Utility.LOGIN_INTENT);
                    broadcastIntent.putExtra(Utility.ERROR, Utility.METHOD_LOGIN);
                    break;
                }
            }

            sendBroadcast(broadcastIntent);
        }
    }

    private class SendText extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... strings) {
            webSocket.sendText(strings[0]);
            return null;
        }
    }

    private class ConnectToServer extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                webSocket = connect();
            } catch (IOException | WebSocketException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
