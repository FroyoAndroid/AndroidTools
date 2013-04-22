package com.soi.modules;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by IntelliJ IDEA.
 * Date: 4/22/13
 * Time: 10:34 PM
 * Author: spirosoikonomakis
 * To change this template use File | Settings | File Templates.
 */
public class SharedPreferencesHelper {

    private static SharedPreferencesHelper _instance;
    private static SharedPreferences preferences;
    private static SharedPreferences.Editor editor;

    public static void init(Context ctx)
    {
        preferences = PreferenceManager.getDefaultSharedPreferences(ctx);
        editor = preferences.edit();
    }

    public static boolean getFirstTimeOpenApp()
    {
        boolean firstTime = preferences.getBoolean("firstTime", true);
        if (firstTime)
            return true;
        else
            return false;
    }

    public static void setFirstTimeOpenApp(boolean time)
    {
        try {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("firstTime", time);
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
