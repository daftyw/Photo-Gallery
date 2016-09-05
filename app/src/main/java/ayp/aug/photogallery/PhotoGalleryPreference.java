package ayp.aug.photogallery;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by Rawin on 19-Aug-16.
 */
public class PhotoGalleryPreference {
    private static final String TAG = "PhotoGalleryPref";

    private static final String PREF_SEARCH_KEY = "PhotoGalleryPref";
    private static final String PREF_LAST_ID = "PREF_LAST_ID";
    private static final String PREF_IS_ALARM_ON = "PREF_ALARM_ON";
    private static final String PREF_USE_GPS = "use_gps";

    public static SharedPreferences mySharedPref(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static Boolean getUseGPS(Context ctx) {
        return mySharedPref(ctx).getBoolean(PREF_USE_GPS, false);
    }

    public static void setUseGPS(Context ctx, boolean use_GPS) {
        mySharedPref(ctx).edit().putBoolean(PREF_USE_GPS, use_GPS).apply();
    }

    public static Boolean getStoredIsAlarmOn(Context ctx) {
        return mySharedPref(ctx).getBoolean(PREF_IS_ALARM_ON, false);
    }

    public static void setStoredIsAlarmOn(Context context, Boolean isAlarmOn) {
        mySharedPref(context).edit().putBoolean(PREF_IS_ALARM_ON, isAlarmOn).apply();
    }

    public static String getStoredSearchKey(Context context) {
        return mySharedPref(context).getString(PREF_SEARCH_KEY, null);
    }

    public static void setStoredSearchKey(Context context, String key) {
        mySharedPref(context).edit()
                .putString(PREF_SEARCH_KEY, key)
                .apply();
    }

    public static String getStoredLastId(Context context) {
        return mySharedPref(context).getString(PREF_LAST_ID, null);
    }

    public static void setStoredLastId(Context context, String lastId) {
        mySharedPref(context).edit()
                .putString(PREF_LAST_ID, lastId)
                .apply();
    }
}
