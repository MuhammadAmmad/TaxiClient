package com.example.roman.test.socket;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.roman.test.Utility;
import com.example.roman.test.data.Message;
import com.example.roman.test.data.MessagesTable;
import com.example.roman.test.data.Sector;
import com.example.roman.test.data.SectorsTable;
import com.google.gson.Gson;
import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketExtension;
import com.neovisionaries.ws.client.WebSocketFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class SocketService extends Service {
    private static final String LOG_TAG = SocketService.class.getSimpleName();
    private static final String SERVER = "ws://gw.staxi.com.ua:16999/test";
    private static final int TIMEOUT = 5000;

    private WebSocket webSocket;
    private String id;

    Gson gson = new Gson();

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

    public void login(String username, String password) throws JSONException {
        final String LOGIN = "L";
        final String PASSWORD = "P";

        JSONObject json = new JSONObject();
        json.put(LOGIN, username);
        json.put(PASSWORD, password);

        sendMessage(json, Utility.METHOD_LOGIN);
    }

    public void logout() throws JSONException {
        sendRequest(Utility.METHOD_LOGOUT);
    }

    public void alert() throws JSONException {
        sendRequest(Utility.METHOD_SET_ALERT);
    }

    private class SocketListener extends WebSocketAdapter {
        @Override
        public void onTextMessage(WebSocket webSocket, String message) throws JSONException {
            Log.e(LOG_TAG, message);
            Intent broadcastIntent = new Intent();

            JSONObject object = new JSONObject(message);
            int error = Utility.getError(object);
            broadcastIntent.putExtra(Utility.ERROR, error);
            switch (error) {
                case Utility.ERROR_NONE:
                    int type = Utility.getMethod(object);
                    broadcastIntent.putExtra(Utility.METHOD, type);

                    switch (type) {
                        case Utility.METHOD_LOGIN:
                            id = object.getString(Utility.RESPONSE);
                            getBalance();
                            getSettings();
                            broadcastIntent.setAction(Utility.LOGIN_INTENT);
                            break;

                        case Utility.METHOD_GET_BALANCE:
                            String balance = object.getString(Utility.RESPONSE);
                            getSharedPreferences(Utility.MY_PREFS_NAME, Context.MODE_PRIVATE)
                                    .edit()
                                    .putString("balance", balance)
                                    .apply();
                            break;

                        case Utility.METHOD_DELETE_ORDER:
                            int deleteOrderId  = object.getInt(Utility.RESPONSE);
                            broadcastIntent.setAction(Utility.MAIN_INTENT);
                            broadcastIntent.putExtra(Utility.RESPONSE, deleteOrderId);
                            break;

                        case Utility.METHOD_GET_SETTINGS:
                            JSONArray sectorsArray = object.getJSONObject(Utility.RESPONSE)
                                    .getJSONArray(Utility.SECTORS);
                            new GetSettingsTask(sectorsArray).execute();
                            break;

                        case Utility.METHOD_NEW_ORDER:
                            String order = object.getString(Utility.RESPONSE);
                            broadcastIntent.setAction(Utility.MAIN_INTENT);
                            broadcastIntent.putExtra(Utility.RESPONSE, order);
                            break;

                        case Utility.METHOD_NEW_MESSAGE:
                            String msg = object.getJSONObject(Utility.RESPONSE).toString();
                            broadcastIntent.setAction(Utility.MAIN_INTENT);
                            broadcastIntent.putExtra(Utility.RESPONSE, msg);
                            getContentResolver().insert(SectorsTable.CONTENT_URI,
                                    MessagesTable.getContentValues(gson.fromJson(msg, Message.class), false));
                            break;
                    }
                    break;
                case Utility.ERROR_LOGIN_INCORRECT:
                case Utility.ERROR_LOGIN_BLOCKED:
                case Utility.ERROR_LOGIN_OCCUPIED:
                case Utility.ERROR_LOGIN_RADIO:
                case Utility.ERROR_LOGIN_TAKEN:
                    broadcastIntent.setAction(Utility.LOGIN_INTENT);
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

    private void sendRequest(int method) throws JSONException {
        JSONObject json = new JSONObject();
        json.put(Utility.DG, id);
        sendMessage(json, method);
    }

    private void getBalance() throws JSONException {
        sendRequest(Utility.METHOD_GET_BALANCE);
    }

    private void getSettings() throws JSONException {
        sendRequest(Utility.METHOD_GET_SETTINGS);
    }

    public void sendMessage(final JSONObject object, final int method) throws JSONException {
        final JSONObject json = Utility.getRQObject();
        json.put(Utility.REQUEST, object);
        json.put(Utility.METHOD, method);
        (new SendText()).execute(json.toString());
    }

    public class GetSettingsTask extends AsyncTask<Void, Void, Boolean> {
        private final JSONArray mSectors;

        GetSettingsTask(JSONArray sectors) {
            mSectors = sectors;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            Cursor c = getContentResolver().query(SectorsTable.CONTENT_URI, null, null, null, null);
            if (c != null && c.getCount() == 0) {
                Sector[] sectors = gson.fromJson(mSectors.toString(), Sector[].class);

                for (Sector s : sectors) {
                    getContentResolver().insert(SectorsTable.CONTENT_URI,
                            SectorsTable.getContentValues(s, false));
                }
                c.close();
            }
            return true;
        }
    }
}
