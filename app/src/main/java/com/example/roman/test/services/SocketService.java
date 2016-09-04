package com.example.roman.test.services;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;

import com.example.roman.test.R;
import com.example.roman.test.TaxiApp;
import com.example.roman.test.data.Order;
import com.example.roman.test.data.OrdersTable;
import com.example.roman.test.data.Sector;
import com.example.roman.test.data.SectorsTable;
import com.example.roman.test.utilities.Constants;
import com.example.roman.test.utilities.Functions;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
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
import static com.example.roman.test.utilities.Constants.METHOD_GET_CURRENT_SECTOR;
import static com.example.roman.test.utilities.Constants.METHOD_GET_NEW_STATUS;
import static com.example.roman.test.utilities.Constants.METHOD_GET_ORDERS;
import static com.example.roman.test.utilities.Constants.METHOD_GET_ORDER_BY_ID;
import static com.example.roman.test.utilities.Constants.METHOD_GET_SECTORS;
import static com.example.roman.test.utilities.Constants.METHOD_GET_SETTINGS;
import static com.example.roman.test.utilities.Constants.METHOD_LOGIN;
import static com.example.roman.test.utilities.Constants.METHOD_NEW_MESSAGE;
import static com.example.roman.test.utilities.Constants.METHOD_NEW_ORDER;
import static com.example.roman.test.utilities.Constants.METHOD_SET_NEW_STATUS;
import static com.example.roman.test.utilities.Constants.METHOD_SET_ORDER_STATUS;
import static com.example.roman.test.utilities.Constants.METHOD_SET_TO_SECTOR;
import static com.example.roman.test.utilities.Constants.NEW_ORDER_MASK;
import static com.example.roman.test.utilities.Constants.NEW_SECTOR;
import static com.example.roman.test.utilities.Constants.ORDER_ID;
import static com.example.roman.test.utilities.Constants.ORDER_STATUS_TAKE;
import static com.example.roman.test.utilities.Constants.PASSWORD;
import static com.example.roman.test.utilities.Constants.REASON_ARRAY;
import static com.example.roman.test.utilities.Constants.RESPONSE;
import static com.example.roman.test.utilities.Constants.SECTOR_HASH;
import static com.example.roman.test.utilities.Constants.STATUS_ARRAY;
import static com.example.roman.test.utilities.Constants.STATUS_ID;
import static com.example.roman.test.utilities.Constants.WAITING_TIME;
import static com.example.roman.test.utilities.Functions.saveToPreferences;

public class SocketService extends Service {
    private static final String LOG_TAG = SocketService.class.getSimpleName();
    private static String serverAddress;
    private static final int TIMEOUT = 5000;
    private static SocketService sService;
    private static String AUTHORITY = "ws://";
    private final static String PATH = "/test";

    private WebSocket webSocket;
    private String id;

    @Inject Gson gson;
    @Inject SharedPreferences prefs;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public static SocketService getInstance() {
        return sService;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ((TaxiApp) getApplication()).getNetComponent().inject(this);
        sService = this;
        String server = PreferenceManager.getDefaultSharedPreferences(this)
                .getString(getString(R.string.pref_server_key),
                        getString(R.string.pref_server_default));
        String port = PreferenceManager.getDefaultSharedPreferences(this)
                .getString(getString(R.string.pref_port_key),
                        getString(R.string.pref_port_default));
        serverAddress = AUTHORITY + server + ":" + port + PATH;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        new ConnectToServer().execute();
        return START_STICKY;
    }

    private WebSocket connect() throws IOException, WebSocketException {
        return new WebSocketFactory()
                .setConnectionTimeout(TIMEOUT)
                .createSocket(serverAddress)
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
        Log.e("Create", "getSectors");
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

    public void getCurrentSector() throws JSONException {
        sendMessage(METHOD_GET_CURRENT_SECTOR, getId());
    }

    public void takeOrder(String orderId, String waitingTime) throws JSONException {
        Functions.JSONField newStatus = new Functions.JSONField(STATUS_ID, ORDER_STATUS_TAKE);
        Functions.JSONField newWaitingTime = new Functions.JSONField(WAITING_TIME, waitingTime);
        Functions.JSONField newOrder = new Functions.JSONField(ORDER_ID, orderId);
        sendMessage(METHOD_SET_ORDER_STATUS, newStatus, newOrder, newWaitingTime);
    }

    public void getOrderById(String ... orderId) throws JSONException {
        Functions.JSONField newOrderId;
        if (orderId.length > 0) {
            newOrderId = new Functions.JSONField(ORDER_ID, orderId[0]);
        } else {
            newOrderId = new Functions.JSONField(ORDER_ID, "null");
        }

        sendMessage(METHOD_GET_ORDER_BY_ID, newOrderId);
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

    private void sendMessage(final int method, Functions.JSONField ... args) throws JSONException {
        JSONObject requestObject = new JSONObject();
        for (Functions.JSONField field : args) {
            requestObject.put(field.getMethod(), field.getData());
        }

        final JSONObject json = new JSONObject();
        json.put(Constants.REQUEST, requestObject);
        json.put(Constants.METHOD, method);

        new SendText().execute(json.toString());
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

    // TODO check hash not to update sectors
    public class HandleRequestTask extends AsyncTask<Void, Void, Void> {
        private final String mMessage;

        HandleRequestTask(String message) {
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
                                saveToPreferences(balance, "balance", prefs);
                                break;

                            case METHOD_DELETE_ORDER:
                                String deleteOrderId = object.getJSONObject(RESPONSE).getString(ORDER_ID);
                                broadcastIntent.setAction(Constants.MAIN_INTENT);
                                broadcastIntent.putExtra(Constants.RESPONSE, deleteOrderId);
                                break;

                            case METHOD_GET_SETTINGS:
                                JSONObject sectorsArray = object.getJSONObject(RESPONSE);
                                setSettings(sectorsArray);
                                break;

                            case METHOD_GET_SECTORS:
                                JSONObject sectors = object.getJSONObject(RESPONSE);
                                updateSectors(sectors);
                                break;

                            case METHOD_GET_ORDERS:
                                JSONArray orderArray = object.getJSONArray(RESPONSE);
                                if (orderArray.length() > 0) {
                                    String orders = orderArray.toString();
                                    broadcastIntent.setAction(MAIN_INTENT);
                                    broadcastIntent.putExtra(RESPONSE, orders);
                                }
                                break;

                            case METHOD_NEW_ORDER:
                                String order = object.getString(RESPONSE);
                                broadcastIntent.setAction(MAIN_INTENT);
                                broadcastIntent.putExtra(Constants.RESPONSE, order);
                                break;

                            case METHOD_SET_TO_SECTOR:
                                String sectorId = object.getString(RESPONSE);
                                broadcastIntent.setAction(MAIN_INTENT);
                                broadcastIntent.putExtra(RESPONSE, sectorId);
                                getCurrentSector();
                                break;

                            case METHOD_GET_CURRENT_SECTOR:
                                String currentSector = object.getString(RESPONSE);
                                broadcastIntent.setAction(MAIN_INTENT);
                                broadcastIntent.putExtra(RESPONSE, currentSector);
                                break;

                            case METHOD_GET_NEW_STATUS:
                                String statusId = object.getString(RESPONSE);
                                broadcastIntent.setAction(MAIN_INTENT);
                                broadcastIntent.putExtra(RESPONSE, statusId);
                                break;

                            case METHOD_SET_NEW_STATUS:
                                getDriverStatus();
                                break;

                            case METHOD_SET_ORDER_STATUS:
                                String orderId = object.getJSONObject(RESPONSE).getString(ORDER_ID);
                                String orderStatus = object.getJSONObject(RESPONSE).getString(STATUS_ID);
                                broadcastIntent.putExtra(ORDER_ID, orderId);
                                broadcastIntent.putExtra(STATUS_ID, orderStatus);
                                broadcastIntent.setAction(MAIN_INTENT);
                                break;

                            case METHOD_GET_ORDER_BY_ID:
                                String newOrder = object.getString(RESPONSE);
                                broadcastIntent.putExtra(RESPONSE, newOrder);
                                broadcastIntent.setAction(MAIN_INTENT);
                                break;

                            case METHOD_NEW_MESSAGE:
                                String msg = object.getJSONObject(Constants.RESPONSE).toString();
                                broadcastIntent.setAction(Constants.MAIN_INTENT);
                                broadcastIntent.putExtra(Constants.RESPONSE, msg);
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

        private void setSettings(JSONObject settings) {
            JSONArray jsonSectors = null;
            JSONArray jsonReasons = null;
            JSONArray jsonStatuses = null;
            int newOrderMask = 0;
            String hash = null;

            try {
                jsonSectors = settings.getJSONArray(Constants.SECTORS);
                jsonStatuses = settings.getJSONArray(STATUS_ARRAY);
                jsonReasons = settings.getJSONArray(REASON_ARRAY);
                newOrderMask = settings.getInt(NEW_ORDER_MASK);
                hash = settings.getString(SECTOR_HASH);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            String oldHash = Functions.getFromPreferences(SECTOR_HASH, prefs);

            if (jsonStatuses != null) {
                saveToPreferences(jsonStatuses.toString(), STATUS_ARRAY, prefs);
            }

            if (newOrderMask != 0) {
                saveToPreferences(String.valueOf(newOrderMask), NEW_ORDER_MASK, prefs);
            }

            if (jsonReasons != null) {
                saveToPreferences(jsonReasons.toString(), REASON_ARRAY, prefs);
            }

            if (!oldHash.equals(hash)) {
                saveToPreferences(hash, SECTOR_HASH, prefs);
                Cursor cursor = getContentResolver()
                        .query(SectorsTable.CONTENT_URI, null, null, null, null);

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
                    }
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
                }
                cursor.close();
            }
        }

        private void addOrder(JsonObject jsonOrder) {
            Order order = gson.fromJson(jsonOrder, Order.class);

            Cursor cursor = getContentResolver().query(OrdersTable.CONTENT_URI, null, null, null, null);
            if (cursor != null) {
                getContentResolver().insert(OrdersTable.CONTENT_URI,
                        OrdersTable.getContentValues(order, false));
                cursor.close();
            }
        }
    }
}
