package com.example.roman.test.utilities;

import android.app.Activity;
import android.app.Notification;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.NotificationCompat;
import android.support.v7.preference.PreferenceManager;

import com.example.roman.test.R;
import com.example.roman.test.data.Message;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static com.example.roman.test.utilities.Constants.METHOD;

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

    public static boolean isNight(Activity activity) {
        boolean isNight = true;
        String state = activity.getSharedPreferences(Constants.MY_PREFS_NAME, Context.MODE_PRIVATE)
                .getString(Constants.THEME, null);

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

    public static void setWholeTheme(Activity activity) {
        boolean isNight = isNight(activity);

        if (isNight) {
            activity.setTheme(R.style.AppThemeNight);
        } else {
            activity.setTheme(R.style.AppThemeDay);
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
    public static void saveArray(String array, String arrayName, Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(arrayName, array);
        editor.apply();
    }
}
