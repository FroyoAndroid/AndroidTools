package com.soi.modules;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import com.letfriendsin.app.R;

public class DialogsModule {
    public static ProgressDialog waitingDialog;

    /**
     * Shows a dialog with a custom msg, two buttons (OK and Cancel) and the logo of the app
     * WARNING!!! Must show the dialog in Android's runOnUiThread method of the current activity
     * because of can't show a dialog out of the activity's ui thread
     * @param act is the current activity eg. SoiActivity.this
     * @param msg is the msg which we want to show in our dialog
     */
    public static void showDialog(final Activity act, final String msg) {
        try {
            Builder builder = new Builder(act);
            builder.setMessage(msg)
                    .setTitle(act.getResources().getString(R.string.app_name))
                    .setCancelable(true)
                            //.setIcon(R.drawable.logoicon)
                    .setPositiveButton(act.getResources().getString(R.string.OK),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                }
                            });
            final AlertDialog alert = builder.create();
            act.runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    alert.show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Shows a dialog with a default msg for "No Internet Access", two buttons (OK and Cancel) and the logo of the app
     * WARNING!!! Must show the dialog in Android's runOnUiThread method of the current activity
     * @param act is the current activity eg. SoiActivity.this
     */
    public static void simpleNoInternet(final Activity act) {
        try {
            Builder builder = new Builder(act);
            builder.setMessage(act.getResources().getString(R.string.nointernetaccess))
                    .setTitle(act.getResources().getString(R.string.app_name))
                    .setCancelable(false)
                            //.setIcon(R.drawable.logoicon)
                    .setPositiveButton(act.getResources().getString(R.string.OK),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                }
                            });
            final AlertDialog alert = builder.create();
            act.runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    alert.show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Shows a progress dialog when must run tasks,jobs for some time like an async event
     * WARNING!!! Must show the dialog in Android's runOnUiThread method of the current activity
     * @param act is the current activity eg. SoiActivity.this
     * @param message
     */
    public static void showWaitingDialog(Activity act, String message) {
        if (waitingDialog != null) {
            if (waitingDialog.isShowing())
                return;
        }
        waitingDialog = ProgressDialog.show(act, act.getResources().getString(R.string.app_name), message, true);
        waitingDialog.setIndeterminate(true);
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    Thread.sleep(40 * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (waitingDialog != null && waitingDialog.isShowing()) {
                    closeWaitingDialog();
                }
            }
        }).start();
        act.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                waitingDialog.show();
            }
        });
        waitingDialog.setCancelable(false);
    }

    /**
     *
     */
    public static void closeWaitingDialog() {
        if (waitingDialog != null && waitingDialog.isShowing()) {
            waitingDialog.dismiss();
        }
    }
}
