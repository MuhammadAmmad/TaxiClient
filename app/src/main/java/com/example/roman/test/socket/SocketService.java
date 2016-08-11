package com.example.roman.test.socket;

import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.roman.test.TaxiContract;
import com.example.roman.test.data.Sector;
import com.example.roman.test.data.SectorsTable;
import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketExtension;
import com.neovisionaries.ws.client.WebSocketFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class SocketService extends Service {
    private static final String LOG_TAG = SocketService.class.getSimpleName();
    private static final String SERVER = "ws://gw.staxi.com.ua:16999/test";
    private static final int TIMEOUT = 5000;

    private WebSocket webSocket;
    private String id;

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
        final JSONObject json = TaxiContract.getRQObject();
        json.put(TaxiContract.REQUEST, object);
        json.put(TaxiContract.METHOD, method);
        (new SendText()).execute(json.toString());
    }

    public void login(String username, String password) throws JSONException {
        final String LOGIN = "L";
        final String PASSWORD = "P";

        JSONObject json = new JSONObject();
        json.put(LOGIN, username);
        json.put(PASSWORD, password);

        sendMessage(json, TaxiContract.METHOD_LOGIN);
    }

    public void getSettings() throws JSONException {
        JSONObject json = new JSONObject();
        json.put(TaxiContract.DG, id);
        sendMessage(json, TaxiContract.METHOD_GET_SETTINGS);
    }

    public void logout() throws JSONException {
        JSONObject json = new JSONObject();
        json.put(TaxiContract.DG, id);
        sendMessage(json, TaxiContract.METHOD_LOGOUT);
    }

    public void alert() throws JSONException {
        JSONObject json = new JSONObject();
        json.put(TaxiContract.DG, id);
        sendMessage(json, TaxiContract.METHOD_SET_ALERT);
    }

    public void getBalance() throws JSONException {
        JSONObject json = new JSONObject();
        json.put(TaxiContract.DG, id);
        sendMessage(json, TaxiContract.METHOD_GET_BALANCE);
    }

    private class SocketListener extends WebSocketAdapter {
        @Override
        public void onTextMessage(WebSocket webSocket, String message) throws JSONException {
            Log.e(LOG_TAG, message);
            Intent broadcastIntent = new Intent();

            JSONObject object = new JSONObject(message);
            int error = TaxiContract.getError(object);
            broadcastIntent.putExtra(TaxiContract.ERROR, error);
            switch (error) {
                case TaxiContract.ERROR_NONE:
                    int type = TaxiContract.getMethod(object);
                    broadcastIntent.putExtra(TaxiContract.METHOD, type);

                    switch (type) {
                        case TaxiContract.METHOD_LOGIN:
                            id = object.getString(TaxiContract.RESPONSE);
                            getBalance();
                            getSettings();
                            broadcastIntent.setAction(TaxiContract.LOGIN_INTENT);
                            break;
                        case TaxiContract.METHOD_GET_BALANCE:
                            String balance = object.getString(TaxiContract.RESPONSE);
                            getSharedPreferences(TaxiContract.MY_PREFS_NAME, Context.MODE_PRIVATE)
                                    .edit()
                                    .putString("balance", balance)
                                    .apply();
                            break;
                        case TaxiContract.METHOD_DELETE_ORDERS:
                            int deleteOrderId  = object.getInt(TaxiContract.RESPONSE);
                            broadcastIntent.setAction(TaxiContract.MAIN_INTENT);
                            broadcastIntent.putExtra(TaxiContract.RESPONSE, deleteOrderId);
                            break;
                        case TaxiContract.METHOD_GET_SETTINGS:
                            JSONObject response = object.getJSONObject(TaxiContract.RESPONSE);
                            JSONArray sectorsArray = response.getJSONArray(TaxiContract.SECTORS);

                            Cursor c = getContentResolver().query(SectorsTable.CONTENT_URI, null, null, null, null);
                            if (c.getCount() == 0) {
                                ArrayList<Sector> sectors = new ArrayList<>();
                                for (int i = 0; i < sectorsArray.length(); i++) {
                                    try {
                                        sectors.add((Sector) sectorsArray.get(i));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                                for (Sector s : sectors) {
                                    getContentResolver().insert(SectorsTable.CONTENT_URI,
                                            SectorsTable.getContentValues(s, false));
                                }
                            }
                            return;
                    }
                    break;
                case TaxiContract.ERROR_LOGIN_INCORRECT:
                case TaxiContract.ERROR_LOGIN_BLOCKED:
                case TaxiContract.ERROR_LOGIN_OCCUPIED:
                case TaxiContract.ERROR_LOGIN_RADIO:
                case TaxiContract.ERROR_LOGIN_TAKEN:
                    broadcastIntent.setAction(TaxiContract.LOGIN_INTENT);
                    break;
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
