package com.fabricethilaw.gozem.businesscase.location;


import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.location.Location;

import com.fabricethilaw.gozem.R;

import java.text.DateFormat;
import java.util.Date;

public class Utils {

    private Utils() {
        // It is the constructor
    }


    static final String KEY_REQUESTING_LOCATION_UPDATES = "requesting_locaction_updates";

    /**
     * Returns true if requesting location updates, otherwise returns false.
     *
     * @param context The {@link Context}.
     */
    public static boolean requestingLocationUpdates(Context context) {
        return context.getSharedPreferences(getPrefName(context), MODE_PRIVATE)
                .getBoolean(KEY_REQUESTING_LOCATION_UPDATES, false);
    }

    /**
     * Stores the location updates state in SharedPreferences.
     *
     * @param requestingLocationUpdates The location updates state.
     */
    public static void setRequestingLocationUpdates(Context context, boolean requestingLocationUpdates) {
        context.getSharedPreferences(getPrefName(context), MODE_PRIVATE)
                .edit()
                .putBoolean(KEY_REQUESTING_LOCATION_UPDATES, requestingLocationUpdates)
                .apply();
    }

    /**
     * Returns the {@code location} object as a human readable string.
     *
     * @param location The {@link Location}.
     */
    public static String getLocationText(Location location) {
        return location == null ? "Unknown location" :
                "(" + location.getLatitude() + ", " + location.getLongitude() + ")";
    }


    public static String getLocationTitle(Context context) {
        return context.getString(R.string.location_updated,
                DateFormat.getDateTimeInstance().format(new Date()));
    }

    private static String getPrefName(Context context) {
        return context.getPackageName() + "_preferences";
    }
}