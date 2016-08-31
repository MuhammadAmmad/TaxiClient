package com.example.roman.test.utilities;

import android.app.Activity;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.NotificationCompat;

import com.example.roman.test.LoginActivity;
import com.example.roman.test.MainActivity;
import com.example.roman.test.R;
import com.example.roman.test.SettingsActivity;
import com.example.roman.test.data.ChatMessage;
import com.example.roman.test.data.Message;
import com.example.roman.test.data.MessagesTable;
import com.example.roman.test.data.Sector;
import com.example.roman.test.data.SectorsTable;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static com.example.roman.test.utilities.Constants.DEFAULT;
import static com.example.roman.test.utilities.Constants.METHOD;
import static com.example.roman.test.utilities.Constants.THEME;

public class Functions {

    public static String getStreetFromLocation(Context context, Location location) {
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

    public static boolean isNight(SharedPreferences prefs) {
        boolean isNight = true;
        String state = prefs.getString(THEME, null);

        if (state != null) {
            isNight = state.equals(Constants.NIGHT);
        }

        return isNight;
    }

    public static int getMethod(JSONObject object) throws JSONException {
        return object.getInt(METHOD);
    }

    public static int getError(JSONObject object) throws JSONException {
        return object.getInt(Constants.ERROR);
    }

    public static void setWholeTheme(Activity activity, SharedPreferences prefs) {
        if (isNight(prefs)) {
            if (activity instanceof MainActivity ||
                    activity instanceof SettingsActivity ||
                    activity instanceof LoginActivity) {
                activity.setTheme(R.style.AppTheme_NoActionBar_Night);
            } else {
                activity.setTheme(R.style.AppTheme_Night);
            }
        } else {
            if (activity instanceof MainActivity ||
                    activity instanceof SettingsActivity ||
                    activity instanceof LoginActivity) {
                activity.setTheme(R.style.AppTheme_NoActionBar_Day);
            } else {
                activity.setTheme(R.style.AppTheme_Day);
            }
        }
    }

    public static AlertDialog.Builder getDialog(Context context, String title, String message) {
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
                .setSmallIcon(R.drawable.ic_new_message)
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
    public static boolean isBetterLocation(Location location, Location currentBestLocation) {
        if (currentBestLocation == null) {
            // A new location is always better than no location
            return true;
        }

        // Check whether the new location fix is newer or older
        long timeDelta = location.getTime() - currentBestLocation.getTime();
        boolean isSignificantlyNewer = timeDelta > Constants.TEN_SECS;
        boolean isSignificantlyOlder = timeDelta < -Constants.TEN_SECS;
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
        boolean isFromSameProvider = isSameProvider(location.getProvider(),
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

    public static class JSONField {
        private String method;
        private String data;

        public JSONField(String method, String data) {
            this.method = method;
            this.data = data;
        }

        public String getData() {
            return data;
        }

        public String getMethod() {
            return method;
        }

        public void setData(String data) {
            this.data = data;
        }

        public void setMethod(String method) {
            this.method = method;
        }
    }

    public static void saveToPreferences(String item, String itemName, SharedPreferences prefs) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(itemName, item);
        editor.apply();
    }

    public static String getFromPreferences(String itemName, SharedPreferences prefs) {
        return prefs.getString(itemName, String.valueOf(DEFAULT));
    }

    public static List<Sector> getSectorList(Context context) {
        Cursor cursor = context.getContentResolver().query(
                SectorsTable.CONTENT_URI, null, null, null, null);

        return SectorsTable.getRows(cursor, false);
    }

    public static List<ChatMessage> getMessageList(Context context) {
        Cursor cursor = context.getContentResolver().query(
                MessagesTable.CONTENT_URI, null, null, null, null);

        return MessagesTable.getRows(cursor, false);
    }

    public static String getSectorNameById(Context context, String id) {
        String sectorName = null;

        Cursor cursor = context.getContentResolver().query(
                SectorsTable.CONTENT_URI,
                null,
                SectorsTable.FIELD_ID + " = ?",
                new String[]{id},
                null);

        if (cursor != null && cursor.getCount() != 0) {
            sectorName = SectorsTable.getRow(cursor, true).getName();
        }

        return sectorName;
    }

    public static boolean showField(int mask, int field) {
        return (mask & field) == field;
    }

    public static void recreate(Activity activity) {
        if (Build.VERSION.SDK_INT >= 11) {
            activity.recreate();
        } else {
            Intent intent = activity.getIntent();
            activity.finish();
            activity.startActivity(intent);
        }
    }

    public static void setLanguage(Activity activity, SharedPreferences prefs) {
        String globalLocale = LocaleHelper.getLanguage(activity.getApplicationContext());
        LocaleHelper.setLocale(activity.getApplicationContext(), globalLocale);
    }
}
