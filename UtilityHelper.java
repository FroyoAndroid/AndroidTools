package com.soi.modules;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;

/**
 * Created by IntelliJ IDEA.
 * Date: 2/16/13
 * Time: 8:54 PM
 * Author: spirosoikonomakis
 * To change this template use File | Settings | File Templates.
 */
public class UtilityHelper {

    private static UtilityHelper _instance;

    /**
     * Singleton pattern for static methods and variables
     * You must run the singletons which you will use in the first Activity
     */
    public static void initInstance(){
        if (_instance == null){
            _instance = new UtilityHelper();
        }
    }

    public static UtilityHelper getInstance() {
        return _instance;
    }
    /**
     * The path of the application in mobile eg. com.test.app
     * @param ctx  Context of current application
     * @return boolean
     */
    public static String getPath(Context ctx)
    {
        return Environment.getExternalStorageDirectory() + "/" + ctx.getApplicationInfo().packageName.replaceAll("\\.", "") + "/";
    }

    /**
     * Create a shortcut for the application on main screen
     * @param ctx Context you must pass eg. SoiActivity.this, this, getBaseContext()
     * @param classAct The class intent which the shortcut must use to create the intent eg. SoiLaunch.class
     */
    public static void createShortcut(Context ctx, Class classAct){
        final Intent shortcutIntent = new Intent(ctx, classAct);
        final Intent intent = new Intent();
        intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
        // Comment out to use the name of the app and the logo

        //intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, ctx.getString(R.string.app_name));
        //intent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, Intent.ShortcutIconResource.fromContext(ctx, R.drawable.logoicon));
        intent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
        ctx.sendBroadcast(intent);
    }

    /**
     *
     * @param act Current Activity
     * @return true or false if mobile is connected to the Internet
     */
    public static boolean isConnected(final Context act) {
        ConnectivityManager connec = (ConnectivityManager) act.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = null;
        try {
            netInfo = connec.getActiveNetworkInfo();
        } catch (NullPointerException e) {
            return false;
        }
        if (netInfo != null) {
            if (netInfo.getState() == NetworkInfo.State.CONNECTED) {
                return true;
            } else {
                return false;
            }
        }
        connec = null;
        netInfo = null;
        return false;
    }

    /**
     * Returns true if mobile has a 3G Network open
     * WARNING!!! You must add to your AndroidManifest.xml the proper
     * permissions
     * <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
     * @param ctx
     * @return
     */
    public static final boolean isOn3G(Context ctx)
    {
        NetworkInfo info = (NetworkInfo) ((ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE))
                .getActiveNetworkInfo();
        if (info != null && info.getTypeName().toLowerCase().equals("mobile"))
            return true;
        else
            return false;
    }
    /**
     * Open url to browser
     * @param ctx Context of application
     * @param url Url path to open in a browser eg. http://google.com
     */
    public static void openURL(Activity ctx, String url)
    {
        if (isConnected(ctx)) {
            try {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                ctx.startActivity(browserIntent);
            } catch (Exception e) {

            }
        } else {
            //DialogsHelper.simpleNoInternet(ctx);
        }
    }
}
