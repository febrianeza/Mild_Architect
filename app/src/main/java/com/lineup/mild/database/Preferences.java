package com.lineup.mild.database;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Preferences {
    public static final String KEY_POPUP_ADS_INCREMENT = "pop_ups_increment";

    public static SharedPreferences getSharedPreference(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static void setKeyPopUpAdsIncrement(Context context, int i) {
        SharedPreferences.Editor editor = getSharedPreference(context).edit();
        editor.putInt(KEY_POPUP_ADS_INCREMENT, i);
        editor.apply();
    }

    public static int getDownloadForAds(Context context) {
        return getSharedPreference(context).getInt(KEY_POPUP_ADS_INCREMENT, 0);
    }
}
