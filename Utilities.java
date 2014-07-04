package com.abl.app.tools;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.LabeledIntent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.Settings;
import android.text.Html;
import android.util.Patterns;
import android.view.WindowManager;


import com.abl.app.R;
import com.abl.app.models.SocialType;
import com.facebook.FacebookException;
import com.facebook.UiLifecycleHelper;
import com.facebook.widget.FacebookDialog;
import com.github.kevinsawicki.http.HttpRequest;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Formatter;
import java.util.List;
import java.util.Timer;
import java.util.regex.Pattern;

public class Utilities {

    private static Utilities _instance;

    /**
     * Singleton patter
     * @return
     */
    public static synchronized Utilities getInstance() {
        if (_instance == null)
            _instance = new Utilities();
        return _instance;
    }

    public int scaleVolume(float volume, int maxVolume)
    {
        return (int) Math.round((volume / maxVolume) * 100);
    }

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    public void strictModeDisable()
    {
        StrictMode.ThreadPolicy policy = new StrictMode.
                ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    public String getFormattedDateByPattern(String pattern)
    {
        Date dt = Calendar.getInstance().getTime();
        Formatter formatter = new Formatter();
        return formatter.format(pattern, dt).toString();
    }

    public void setKeepScreenOn(Activity activity, boolean keepScreenOn) {
        if(keepScreenOn) {
            activity.getWindow().
                    addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        } else {
            activity.getWindow().
                    clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
    }

    public boolean checkIfUrlExists(String url)
    {
        boolean result = false;
        HttpURLConnection connection = null;
        try{
            URL myurl = new URL(url);
            connection = (HttpURLConnection) myurl.openConnection();
            //Set request to header to reduce load as Subirkumarsao said.
            connection.setRequestMethod("HEAD");
            int code = connection.getResponseCode();
            if (code == Constants.STATUS_CODE_OK) {
                result = true;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }



    /**
     * Check if there is an internet connection via 3G
     * @param ctx current context
     * @return
     */
    public final boolean isOn3G(Context ctx)
    {
        NetworkInfo info = ((ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE))
                .getActiveNetworkInfo();
        return info != null && info.getTypeName().toLowerCase().equals("mobile");
    }

    /**
     * Check if mobile device is connected to internet
     * @param act
     * @return
     */
    public boolean isConnected(final Context act) {
        ConnectivityManager connec = (ConnectivityManager) act.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = null;
        try {
            netInfo = connec.getActiveNetworkInfo();
        } catch (NullPointerException e) {
            return false;
        }
        if (netInfo != null) {
            return netInfo.getState() == NetworkInfo.State.CONNECTED;
        }
        connec = null;
        netInfo = null;
        return false;
    }

    public final boolean isOnWifi(Context context)
    {
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        return mWifi.isConnected();
    }

    /**
     * Returns Integer for a specific
     * value
     * @param value
     * @param safeReturn
     * @return
     */
    public synchronized static Integer getSafeInteger(int value, Integer safeReturn)
    {
        try {
            return Integer.valueOf(value).intValue();
        } catch (NumberFormatException ex) {
            return safeReturn;
        } catch (Exception e) {
            return safeReturn;
        }
    }

    /**
     * Open Url in Browser
     * @param ctx
     * @param url
     */
    public void openURL(Activity ctx, String url)
    {
        if (isConnected(ctx)) {
            try {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                ctx.startActivity(browserIntent);
            } catch (Exception e) {

            }
        } else {
            Dialogs.getInstance().simpleNoInternet(ctx);
        }
    }

    /**
     * Lock unlock wifi for streaming
     * @param context
     * @param unlock
     */
    public void wifiLockUnlock(Context context, boolean unlock)
    {

        WifiManager.WifiLock wifiLock = ((WifiManager) context.getSystemService(Context.WIFI_SERVICE))
                .createWifiLock(WifiManager.WIFI_MODE_FULL, "radioLock");

        if (unlock) {
            if (wifiLock.isHeld()) {
                wifiLock.release();
            }
        } else {
            wifiLock.acquire();
        }
    }

    /**
     * Get the path of the application
     * in the Android OS filesystem
     * @param ctx
     * @returnman
     */
    public String getPath(Context ctx)
    {
        return Environment.getExternalStorageDirectory() + "/" + ctx.getApplicationInfo().packageName.replaceAll("\\.", "") + "/";
    }

    /**
     * Get a uniqueId per device
     * @param ctx
     * @return
     */
    public String getUniqueID(Context ctx)
    {
        String m_szDevIDShort = "";
        String m_szAndroidID = "";
        String m_szWLANMAC = "";

        try {
            m_szDevIDShort = "35" + // we make this look like a valid IMEI
                    Build.BOARD.length() % 10 + Build.BRAND.length() % 10 +
                    Build.CPU_ABI.length() % 10 + Build.DEVICE.length() % 10 +
                    Build.DISPLAY.length() % 10 + Build.HOST.length() % 10 +
                    Build.ID.length() % 10 + Build.MANUFACTURER.length() % 10 +
                    Build.MODEL.length() % 10 + Build.PRODUCT.length() % 10 +
                    Build.TAGS.length() % 10 + Build.TYPE.length() % 10 +
                    Build.USER.length() % 10;
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            m_szAndroidID = Settings.Secure.getString(ctx.getContentResolver(), Settings.Secure.ANDROID_ID);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            WifiManager wm = (WifiManager) ctx.getSystemService(Context.WIFI_SERVICE);
            m_szWLANMAC = wm.getConnectionInfo().getMacAddress();
        } catch (Exception e) {
            e.printStackTrace();
        }

        String m_szLongID = m_szWLANMAC + m_szDevIDShort + m_szAndroidID;

        MessageDigest m = null;
        try {
            m = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        m.update(m_szLongID.getBytes(), 0, m_szLongID.length());
        byte p_md5Data[] = m.digest();

        //noinspection RedundantStringConstructorCall
        String m_szUniqueID = new String();
        for (int i = 0; i < p_md5Data.length; i++) {
            int b = (0xFF & p_md5Data[i]);
            // if it is a single digit, make sure it have 0 in front (proper
            // padding)
            if (b <= 0xF)
                m_szUniqueID += "0";
            // add number to string
            m_szUniqueID += Integer.toHexString(b);
        }

        return m_szUniqueID;
    }
    /**
     *
     * @param context
     * @param title
     * @param text
     * @return
     */
    public Intent getIntentChooser(Context context, String title, String text, String shareUrl)
    {
        Intent sendIntent = new Intent(Intent.ACTION_SEND);
        sendIntent.setType("text/plain");

        // Email to Send
        Intent emailIntent = new Intent();
        emailIntent.setAction(Intent.ACTION_SEND);
        emailIntent.putExtra(Intent.EXTRA_TEXT, title+ " "+ shareUrl);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, title);
        emailIntent.setType("message/rfc822");

        Intent openInChooser = Intent.createChooser(emailIntent, context.getResources().getString(R.string.share));
        PackageManager pm = context.getPackageManager();

        List<ResolveInfo> resInfo = pm.queryIntentActivities(sendIntent, 0);
        List<LabeledIntent> intentList = new ArrayList<LabeledIntent>();

        for (int i = 0; i < resInfo.size(); i++) {
            ResolveInfo ri = resInfo.get(i);
            String packageName = ri.activityInfo.packageName;
            if (ri.activityInfo.packageName.endsWith(".gm") || ri.activityInfo.name.toLowerCase().contains("gmail")) {
                emailIntent.setClassName(ri.activityInfo.packageName, ri.activityInfo.name);
            } else if(packageName.contains("twitter") || packageName.contains("facebook")) {
                Intent intent = new Intent();
                intent.setComponent(new ComponentName(packageName, ri.activityInfo.name));
                intent.setAction(Intent.ACTION_SEND);
                intent.setType("text/plain");

                if(packageName.contains("twitter")) {
                    intent.putExtra(Intent.EXTRA_TEXT, title+ " "+ shareUrl);
                } else if(packageName.contains("facebook")) {
                    intent.putExtra(Intent.EXTRA_TEXT, shareUrl);
                }
                intentList.add(new LabeledIntent(intent, packageName, ri.loadLabel(pm), ri.icon));
            }
        }
        LabeledIntent[] extraIntents = intentList.toArray( new LabeledIntent[intentList.size()]);
        openInChooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, extraIntents);
        return openInChooser;
    }

    /**
     * Email text validation
     * @param email
     * @return
     */
    public boolean validEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }


    /**
     * Open each social app or browser to share
     * content
     * @param act current activity
     * @param uiHelper UI helper of current activity for FB
     * @param socialToShare the type of the share
     * @param title the title if is the Facebook or anything else
     * @param text the text which will be shared
     * @param shareUrl the url which will be shared
     */
    public void shareSocial(Activity act,UiLifecycleHelper uiHelper, SocialType socialToShare,
                            String title, String text, String shareUrl, String[] recipients, boolean fromGmail)
    {
        switch (socialToShare) {
            case FACEBOOK:
                this._fbAppShare(act, uiHelper, shareUrl, text);
                break;
            case TWITTER:
                this._tweetShare(act, title, text);
                break;
            case EMAIL:
                this._emailShare(act, title, text, recipients, fromGmail);
                break;
        }
    }

    /**
     * Share via facebook app and fail over scenario
     * to open the browser for sharing
     * @param act
     * @param uiHelper
     * @param link
     * @param description
     */
    private void _fbAppShare(Activity act, UiLifecycleHelper uiHelper, String link, String description)
    {
        try {
            FacebookDialog shareDialog = new FacebookDialog.ShareDialogBuilder(act)
                    .setLink(link != null && !link.equals("") ? link : "")
                    .setDescription(description != null && !description.equals("") ? description : "")
                    .build();
            uiHelper.trackPendingDialogCall(shareDialog.present());
        } catch (FacebookException ex) {
            String urlParameters = String.format(Constants.FB_SHARE_URL,
                    act.getString(R.string.app_url), act.getString(R.string.app_name));
            Utilities.getInstance().openURL(act, urlParameters);
        }
    }

    /**
     * Share via twitter app and fail over scenario
     * to open the browser for sharing
     * @param act
     * @param title
     * @param description
     */
    private void _tweetShare(Activity act, String title, String description)
    {
        try {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.setPackage("com.twitter.android");
            shareIntent.putExtra(Intent.EXTRA_TITLE, title);
            shareIntent.putExtra(Intent.EXTRA_TEXT, description);
            act.startActivity(shareIntent);
        } catch (ActivityNotFoundException ex) {
            String tweetUrl = StringUtils.join(" ","https://twitter.com/intent/tweet?text=", title, description);
            Uri uri = Uri.parse(tweetUrl);
            act.startActivity(new Intent(Intent.ACTION_VIEW, uri));
        }
    }

    /**
     * Share via email
     * @param act
     * @param title
     * @param text
     */
    private void _emailShare(Activity act, String title, String text, String[] recipients, boolean fromGmail)
    {

        if ( fromGmail ) {
            this.emailFromGmail(act, title, text, recipients);
            return;
        }

        // Email to Send
        Intent emailIntent = new Intent();
        emailIntent.setAction(Intent.ACTION_SEND);
        if ( recipients.length > 0) {
            emailIntent.putExtra(Intent.EXTRA_EMAIL, recipients);
        }
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, title);
        emailIntent.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(text));
        emailIntent.setType("message/rfc822");
        act.startActivity(emailIntent);
    }

    private void emailFromGmail(Activity act, String title, String text, String[] recipients) {
        Intent sendIntent = new Intent(Intent.ACTION_VIEW);
        sendIntent.setType("plain/text");
        sendIntent.setClassName("com.google.android.gm", "com.google.android.gm.ComposeActivityGmail");
        sendIntent.putExtra(Intent.EXTRA_EMAIL, recipients);
        sendIntent.putExtra(Intent.EXTRA_SUBJECT, title);
        sendIntent.putExtra(Intent.EXTRA_TEXT, text);
        act.startActivity(sendIntent);
    }


    /**
     * Open youtube page from the app
     * otherwise in browser
     * @param activity
     */
    public final void openYoutube(Activity activity,  String url) {
        Intent intent = new Intent(
                Intent.ACTION_VIEW ,
                Uri.parse(url));
        intent.setComponent(new ComponentName("com.google.android.youtube","com.google.android.youtube.PlayerActivity"));

        PackageManager manager = activity.getPackageManager();
        List<ResolveInfo> infos = manager.queryIntentActivities(intent, 0);
        if (infos.size() > 0) {
            activity.startActivity(intent);
        } else{
            activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
        }
    }

    /**
     * Open twitter page from the app
     * otherwise in browser
     * @param act
     */
    public final void openTwitter(Activity act, String twitterId)
    {
        Intent intent = null;
        try {
            // get the Twitter app if possible
            act.getPackageManager().getPackageInfo("com.twitter.android", 0);
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?screen_name="+twitterId));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        } catch (Exception e) {
            // no Twitter app, revert to browser
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/"+twitterId));
        }
        act.startActivity(intent);
    }


    /**
     * Open facebook page from the app
     * otherwise in browser
     * @param act
     */
    public final void openFacebook(Activity act, String pageName) {
        final String urlFb = "fb://profile/"+pageName;
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(urlFb));

        // If Facebook application is installed, use that else launch a browser
        final PackageManager packageManager = act.getPackageManager();
        List<ResolveInfo> list =
                packageManager.queryIntentActivities(intent,
                        PackageManager.MATCH_DEFAULT_ONLY);
        if (list.size() == 0) {
            final String urlBrowser = "https://www.facebook.com/"+pageName;
            intent.setData(Uri.parse(urlBrowser));
        }

        act.startActivity(intent);
    }

    /**
     * Open soundcloud page from the app
     * otherwise in browser
     * @param act
     */
    public final void openSoundCloud(Activity act) {
        final String urlFb = act.getString(R.string.share_soundcloud);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(urlFb));

        // If Facebook application is installed, use that else launch a browser
        final PackageManager packageManager = act.getPackageManager();
        List<ResolveInfo> list =
                packageManager.queryIntentActivities(intent,
                        PackageManager.MATCH_DEFAULT_ONLY);
        if (list.size() == 0) {
            final String urlBrowser = act.getString(R.string.soundcloud_http_url);
            intent.setData(Uri.parse(urlBrowser));
        }

        act.startActivity(intent);
    }

    /**
     * Open podcast in browser
     * @param act
     */
    public final void openPodcast(Activity act)
    {
        final String urlBrowser = act.getString(R.string.podcast_url);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(urlBrowser));
        act.startActivity(intent);

    }

    public boolean downloadFile(Context context, final String url, final String filename) {
        System.gc();
        String response = null;
        String path = Utilities.getInstance().getPath(context);
        new File(path).mkdirs();

        File outputFile = new File(path, filename);
        HttpRequest request = new HttpRequest(url, HttpRequest.METHOD_GET);
        request.userAgent("Chrome");
        request.receive(outputFile);
        return true;

    }
}
