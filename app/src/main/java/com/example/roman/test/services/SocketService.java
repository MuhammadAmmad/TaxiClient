package com.example.roman.test.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.roman.test.TaxiApp;
import com.example.roman.test.data.Message;
import com.example.roman.test.data.MessagesTable;
import com.example.roman.test.data.Sector;
import com.example.roman.test.data.SectorsTable;
import com.example.roman.test.utilities.Constants;
import com.example.roman.test.utilities.Functions;
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

import javax.inject.Inject;

import static com.example.roman.test.utilities.Constants.CURRENT_SECTOR;
import static com.example.roman.test.utilities.Constants.DG;
import static com.example.roman.test.utilities.Constants.LOGIN;
import static com.example.roman.test.utilities.Constants.MAIN_INTENT;
import static com.example.roman.test.utilities.Constants.METHOD_DELETE_ORDER;
import static com.example.roman.test.utilities.Constants.METHOD_GET_BALANCE;
import static com.example.roman.test.utilities.Constants.METHOD_GET_NEW_STATUS;
import static com.example.roman.test.utilities.Constants.METHOD_GET_ORDERS;
import static com.example.roman.test.utilities.Constants.METHOD_GET_SECTORS;
import static com.example.roman.test.utilities.Constants.METHOD_GET_SETTINGS;
import static com.example.roman.test.utilities.Constants.METHOD_LOGIN;
import static com.example.roman.test.utilities.Constants.METHOD_NEW_ORDER;
import static com.example.roman.test.utilities.Constants.METHOD_SET_NEW_STATUS;
import static com.example.roman.test.utilities.Constants.METHOD_SET_STATUS;
import static com.example.roman.test.utilities.Constants.METHOD_SET_TO_SECTOR;
import static com.example.roman.test.utilities.Constants.NEW_SECTOR;
import static com.example.roman.test.utilities.Constants.ORDER_ID;
import static com.example.roman.test.utilities.Constants.PASSWORD;
import static com.example.roman.test.utilities.Constants.REASON_ARRAY;
import static com.example.roman.test.utilities.Constants.RESPONSE;
import static com.example.roman.test.utilities.Constants.STATUS_ARRAY;
import static com.example.roman.test.utilities.Constants.STATUS_ID;

public class SocketService extends Service {
    private static final String LOG_TAG = SocketService.class.getSimpleName();
    private static final String SERVER = "ws://gw.staxi.com.ua:16999/test";
    private static final int TIMEOUT = 5000;
    private static SocketService sService;

    private WebSocket webSocket;
    private String id;

    @Inject
    Gson gson;

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

    public static SocketService getInstance() {
        return sService;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        (new ConnectToServer()).execute();

        sService = this;
        ((TaxiApp) getApplication()).getNetComponent().inject(sService);

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
        Functions.JSONField name = new Functions.JSONField(LOGIN, username);
        Functions.JSONField pass = new Functions.JSONField(PASSWORD, password);
        sendMessage(METHOD_LOGIN, name, pass);
    }

    public void logout() throws JSONException {
        sendMessage(Constants.METHOD_LOGOUT, getId());
    }

    public void alert() throws JSONException {
        sendMessage(Constants.METHOD_SET_ALERT, getId());
    }

    public void getSectors(String sector) throws JSONException {
        Functions.JSONField currentSector = new Functions.JSONField(CURRENT_SECTOR, sector);
        sendMessage(METHOD_GET_SECTORS, getId(), currentSector);
    }

    public void setSector(String sector) throws JSONException {
        Functions.JSONField setSector = new Functions.JSONField(NEW_SECTOR, sector);
        sendMessage(METHOD_SET_TO_SECTOR, setSector);
    }

    private void getBalance() throws JSONException {
        sendMessage(METHOD_GET_BALANCE, getId());
    }

    private void getSettings() throws JSONException {
        sendMessage(METHOD_GET_SETTINGS, getId());
    }

    public void setDriverStatus(String id) throws JSONException {
        Functions.JSONField newStatus = new Functions.JSONField(STATUS_ID, id);
        sendMessage(METHOD_SET_NEW_STATUS, newStatus, getId());
    }

    public void getDriverStatus() throws JSONException {
        sendMessage(METHOD_GET_NEW_STATUS, getId());
    }

    public void getOrders() throws JSONException {
        sendMessage(METHOD_GET_ORDERS, getId());
    }

    public void setStatus(String orderStatus, String orderId) throws JSONException {
        Functions.JSONField newOrderStatus = new Functions.JSONField(STATUS_ID, orderStatus);
        Functions.JSONField newOrderId = new Functions.JSONField(ORDER_ID, orderId);
        sendMessage(METHOD_SET_STATUS, newOrderId, newOrderStatus, getId());
    }

    private Functions.JSONField getId() {
        return new Functions.JSONField(DG, id);
    }

    private class SocketListener extends WebSocketAdapter {
        @Override
        public void onTextMessage(WebSocket webSocket, String message) throws JSONException {
            Log.e(LOG_TAG, message);
            new HandleRequestTask(message).execute();
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

    private void sendMessage(final int method, Functions.JSONField ... args) throws JSONException {
        JSONObject requestObject = new JSONObject();
        for (Functions.JSONField field : args) {
            requestObject.put(field.getMethod(), field.getData());
        }

        final JSONObject json = new JSONObject();
        json.put(Constants.REQUEST, requestObject);
        json.put(Constants.METHOD, method);

        (new SendText()).execute(json.toString());
    }

    // TODO check hash not to update sectors

    public class HandleRequestTask extends AsyncTask<Void, Void, Void> {
        private final String mMessage;

        HandleRequestTask(String message) throws JSONException {
            mMessage = message;
        }

        @Override
        protected Void doInBackground(Void... params) {
            Intent broadcastIntent = new Intent();

            JSONObject object;
            try {
                object = new JSONObject(mMessage);
                int error = Functions.getError(object);
                broadcastIntent.putExtra(Constants.ERROR, error);
                switch (error) {
                    case Constants.ERROR_NONE:
                        int type = Functions.getMethod(object);
                        broadcastIntent.putExtra(Constants.METHOD, type);

                        switch (type) {
                            case METHOD_LOGIN:
                                id = object.getString(Constants.RESPONSE);
                                getBalance();
                                getSettings();
                                broadcastIntent.setAction(Constants.LOGIN_INTENT);
                                break;

                            case METHOD_GET_BALANCE:
                                String balance = object.getString(Constants.RESPONSE);
                                getSharedPreferences(Constants.MY_PREFS_NAME, Context.MODE_PRIVATE)
                                        .edit()
                                        .putString("balance", balance)
                                        .apply();
                                break;

                            case METHOD_DELETE_ORDER:
                                int deleteOrderId = object.getInt(Constants.RESPONSE);
                                broadcastIntent.setAction(Constants.MAIN_INTENT);
                                broadcastIntent.putExtra(Constants.RESPONSE, deleteOrderId);
                                break;

                            case METHOD_GET_SETTINGS:
                                JSONObject sectorsArray = object.getJSONObject(Constants.RESPONSE);
                                setSettings(sectorsArray);
                                break;

                            case METHOD_GET_SECTORS:
                                JSONObject sectors = object.getJSONObject(Constants.RESPONSE);
                                updateSectors(sectors);
                                break;

                            case METHOD_GET_ORDERS:
                                String orders = object.getJSONArray(Constants.RESPONSE).toString();
                                broadcastIntent.setAction(MAIN_INTENT);
                                broadcastIntent.putExtra(RESPONSE, orders);
                                break;

                            case METHOD_NEW_ORDER:
                                String order = object.getString(Constants.RESPONSE);
                                broadcastIntent.setAction(MAIN_INTENT);
                                broadcastIntent.putExtra(Constants.RESPONSE, order);
                                break;

                            case METHOD_SET_TO_SECTOR:
                                String sectorId = object.getString(RESPONSE);
                                broadcastIntent.setAction(MAIN_INTENT);
                                broadcastIntent.putExtra(RESPONSE, sectorId);
                                break;

                            case METHOD_GET_NEW_STATUS:
                                String statusId = object.getString(RESPONSE);
                                broadcastIntent.setAction(MAIN_INTENT);
                                broadcastIntent.putExtra(RESPONSE, statusId);
                                break;

                            case Constants.METHOD_NEW_MESSAGE:
                                String msg = object.getJSONObject(Constants.RESPONSE).toString();
                                broadcastIntent.setAction(Constants.MAIN_INTENT);
                                broadcastIntent.putExtra(Constants.RESPONSE, msg);
                                getContentResolver().insert(SectorsTable.CONTENT_URI,
                                        MessagesTable.getContentValues(gson.fromJson(msg, Message.class), false));
                                break;
                        }
                        break;
                    case Constants.ERROR_LOGIN_INCORRECT:
                    case Constants.ERROR_LOGIN_BLOCKED:
                    case Constants.ERROR_LOGIN_OCCUPIED:
                    case Constants.ERROR_LOGIN_RADIO:
                    case Constants.ERROR_LOGIN_TAKEN:
                        broadcastIntent.setAction(Constants.LOGIN_INTENT);
                        break;
                }
                sendBroadcast(broadcastIntent);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        private void setSettings(JSONObject sectors) {
            JSONArray jsonSectors = null;
            JSONArray jsonReasons = null;
            JSONArray jsonStatuses = null;

            try {
                jsonSectors = sectors.getJSONArray(Constants.SECTORS);
                jsonStatuses = sectors.getJSONArray(STATUS_ARRAY);
                jsonReasons = sectors.getJSONArray(REASON_ARRAY);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (jsonStatuses != null) {
                Functions.saveArray(jsonStatuses.toString(), STATUS_ARRAY, getApplicationContext());
            }

            if (jsonReasons != null) {
                Functions.saveArray(jsonReasons.toString(), REASON_ARRAY, getApplicationContext());
            }

            Cursor cursor = getContentResolver().query(SectorsTable.CONTENT_URI, null, null, null, null);
            if (cursor == null) {
                return;
            }

            if (jsonSectors != null) {
                if (cursor.getCount() == 0) {
                    Sector[] sectorsArray = gson.fromJson(jsonSectors.toString(), Sector[].class);
                    for (Sector s : sectorsArray) {
                        getContentResolver().insert(SectorsTable.CONTENT_URI,
                                SectorsTable.getContentValues(s, false));
                    }

                    cursor.close();
                }
            }
        }

        private void updateSectors(JSONObject sectors) {
            JSONArray jsonSectorArray = null;
            try {
                jsonSectorArray = sectors.getJSONArray(Constants.SECTORS);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Cursor cursor = getContentResolver().query(SectorsTable.CONTENT_URI, null, null, null, null);

            if (cursor != null) {
                if (jsonSectorArray != null) {
                    Sector[] sectorsArray = gson.fromJson(jsonSectorArray.toString(), Sector[].class);

                    for (Sector s : sectorsArray) {
                        getContentResolver().update(
                                SectorsTable.CONTENT_URI,
                                SectorsTable.getContentValues(s, false),
                                SectorsTable.FIELD_ID + "= ?",
                                new String[]{s.getId()});
                    }

                    cursor.close();
                }
            }
        }
    }
}
