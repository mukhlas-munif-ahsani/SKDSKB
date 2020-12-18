package com.munifahsan.skdskb;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.preference.PreferenceManager;

public class DarkMode extends Application {
    private static final String NIGHT_MODE = "NIGHT_MODE";
    private static final String TOOGLE = "TOOGLE";

    public static boolean isNightModeEnabled(Context context) {
        SharedPreferences mPrefs = context.getSharedPreferences("MY_PREF", MODE_PRIVATE);
        return mPrefs.getBoolean(NIGHT_MODE, false);
    }

    public static void setIsNightModeEnabled(Context context, boolean isNightModeEnabled) {
        SharedPreferences mPrefs = context.getSharedPreferences("MY_PREF", MODE_PRIVATE);
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putBoolean(NIGHT_MODE, isNightModeEnabled);
        editor.apply();
    }

    public static void setIsToogleEnabled(Context context, boolean isToogleEnabled) {
        SharedPreferences mPrefs = context.getSharedPreferences("MY_PREF", MODE_PRIVATE);
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putBoolean(TOOGLE, isToogleEnabled);
        editor.apply();
    }

    public static boolean isToogleEnabled(Context context) {
        SharedPreferences mPrefs = context.getSharedPreferences("MY_PREF", MODE_PRIVATE);
        return mPrefs.getBoolean(TOOGLE, false);
    }

    public static boolean isDarkMode(Activity activity) {
        return (activity.getResources().getConfiguration()
                .uiMode & Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES;
    }
}
