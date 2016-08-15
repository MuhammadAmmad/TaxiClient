package com.example.roman.test;

import android.app.Activity;
import android.app.Notification;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AlertDialog;
import android.content.Context;
import android.support.v7.app.NotificationCompat;

import com.example.roman.test.data.Message;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class Utility {
    private static final int TEN_SECS = 1000 * 10;

    public static int id;

    public static final String MY_PREFS_NAME = "MyPrefsFile";
    public static final int DEFAULT = -1;

    public static final String LOGIN_INTENT = "login_intent";
    public static final String AIR_INTENT = "air_intent";
    public static final String MAIN_INTENT = "main_intent";
    public static final String SECTORS_INTENT = "sectors_intent";

    public static final String METHOD = "M";
    public static final String ERROR = "E";
    public static final String REQUEST = "RQ";
    public static final String RESPONSE = "RS";
    public static final String DG = "DG";

    public static final int METHOD_GET_PREF = 1;
    public static final int METHOD_GET_CURRENT_STATE = 5;
    public static final int METHOD_GET_INFO_ORDER_BY_ID = 18;
    public static final int METHOD_LOGIN = 102;
    public static final int METHOD_GET_SETTINGS = 103;
    public static final int METHOD_GET_SECTORS  = 104;
    public static final int METHOD_GET_BALANCE = 105;
    public static final int METHOD_SET_ALERT = 106;
    public static final int METHOD_GET_CURRENT_SECTOR = 107;
    public static final int METHOD_SET_TO_SECTOR = 108;
    public static final int METHOD_LOGOUT = 113;
    public static final int METHOD_SET_NEW_STATUS = 114;
    public static final int METHOD_GET_NEW_STATUS = 115;
    public static final int METHOD_NEW_ORDER = 116;
    public static final int METHOD_DELETE_ORDER = 117;
    public static final int METHOD_GET_ORDERS  = 118;
    public static final int METHOD_GET_PREV_ORDERS = 119;
    public static final int METHOD_SET_ORDER = 120;
    public static final int METHOD_SET_STATUS = 121;
    public static final int METHOD_SET_LATE_DATA = 122;
    public static final int METHOD_CLOSE_ORDER = 123;
    public static final int METHOD_GET_ORDER_BY_ID = 124;
    public static final int METHOD_SEND_MESSAGE = 125;
    public static final int METHOD_NEW_MESSAGE = 126;
    public static final int METHOD_GET_MESSAGE = 127;
    public static final int METHOD_DENY_ORDER = 128;
    public static final int METHOD_SET_ORDER_NEW = 130;
    public static final int METHOD_GET_ORDER_COUNT = 131;
    public static final int METHOD_GET_EXEC_ORDERS = 134;

    public static final int ERROR_NONE = 0;
    public static final int ERROR_LOGIN_INCORRECT = 1;
    public static final int ERROR_LOGIN_BLOCKED = 2;
    public static final int ERROR_LOGIN_OCCUPIED= 3;
    public static final int ERROR_LOGIN_RADIO = 4;
    public static final int ERROR_LOGIN_TAKEN = 5;
    public static final int ERROR_FULL_QUEUE = 6;
    public static final int ERROR_SECTOR_FAILED = 7;
    public static final int ERROR_ORDER_NOT_FOUND = 10;
    public static final int ERROR_ORDER_TAKEN = 11;
    public static final int ERROR_LOW_BALANCE = 15;
    public static final int ERROR_SHIFT_CLOSED = 100;
    public static final int ERROR_USER_BLOCKED = 106;

    public static final String THEME = "theme";
    public static final String NIGHT = "night";
    public static final String DAY = "day";

    public static final String SECTORS = "SC";

    public static JSONObject getRQObject() throws JSONException {
        JSONObject json = new JSONObject();
        json.put("RQ", new Object());
        json.put("M", 0);

        return json;
    }

    public static int getMethod(JSONObject object) throws JSONException {
        return object.getInt(METHOD);
    }

    public static int getError(JSONObject object) throws JSONException {
        return object.getInt(ERROR);
    }

    static boolean isNight(Activity activity) {
        boolean isNight = true;
        String state = activity.getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE)
                .getString(THEME, null);

        if (state != null) {
            isNight = state.equals(NIGHT);
        }

        return isNight;
    }

    static void setWholeTheme(Activity activity) {
        boolean isNight = Utility.isNight(activity);

        if (isNight) {
            activity.setTheme(R.style.AppThemeNight);
        } else {
            activity.setTheme(R.style.AppThemeDay);
        }
    }

    static AlertDialog.Builder getDialog(Context context, String title, String message) {
        AlertDialog.Builder helpBuilder = new AlertDialog.Builder(context);
        helpBuilder.setTitle(title);
        helpBuilder.setMessage(message);
        return helpBuilder;
    }

    private static void showNotification(Context context, Notification notification) {
        NotificationManagerCompat notificationManager =
                NotificationManagerCompat.from(context);
        notificationManager.notify(0x1234, notification);
    }

    public static void showMessageNotification(Context context, Message message) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        NotificationCompat.BigTextStyle style = new NotificationCompat.BigTextStyle(builder);
        style.bigText(message.getMessage());

        Notification notification = builder
                .setContentTitle("New message")
                .setContentText(message.getMessage())
                .setSmallIcon(R.drawable.ic_email_black_24dp)
                .build();

        showNotification(context, notification);
    }

    static boolean isSameProvider(String provider1, String provider2) {
        if (provider1 == null) {
            return provider2 == null;
        }
        return provider1.equals(provider2);
    }

    /** Determines whether one Location reading is better than the current Location fix
     * @param location  The new Location that you want to evaluate
     * @param currentBestLocation  The current Location fix, to which you want to compare the new one
     */
    static boolean isBetterLocation(Location location, Location currentBestLocation) {
        if (currentBestLocation == null) {
            // A new location is always better than no location
            return true;
        }

        // Check whether the new location fix is newer or older
        long timeDelta = location.getTime() - currentBestLocation.getTime();
        boolean isSignificantlyNewer = timeDelta > TEN_SECS;
        boolean isSignificantlyOlder = timeDelta < -TEN_SECS;
        boolean isNewer = timeDelta > 0;

        // If it's been more than two minutes since the current location, use the new location
        // because the user has likely moved
        if (isSignificantlyNewer) {
            return true;
            // If the new location is more than two minutes older, it must be worse
        } else if (isSignificantlyOlder) {
            return false;
        }

        // Check whether the new location fix is more or less accurate
        int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
        boolean isLessAccurate = accuracyDelta > 0;
        boolean isMoreAccurate = accuracyDelta < 0;
        boolean isSignificantlyLessAccurate = accuracyDelta > 200;

        // Check if the old and new location are from the same provider
        boolean isFromSameProvider = Utility.isSameProvider(location.getProvider(),
                currentBestLocation.getProvider());

        // Determine location quality using a combination of timeliness and accuracy
        if (isMoreAccurate) {
            return true;
        } else if (isNewer && !isLessAccurate) {
            return true;
        } else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
            return true;
        }
        return false;
    }

    static String getStreetFromLocation(Context context, Location location) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (addresses != null) {
            return addresses.get(0).getAddressLine(0);
        }

        return "Unknown";
    }
}