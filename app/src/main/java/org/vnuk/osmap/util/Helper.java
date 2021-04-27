package org.vnuk.osmap.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.os.Build;

import androidx.appcompat.app.AlertDialog;

/**
 * Contains useful methods that are used all over App.
 */
public class Helper {

    public void alerter(Context context, int titleID, int messageID) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(context.getString(titleID));
        builder.setMessage(context.getString(messageID));
        builder.setNeutralButton(context.getString(android.R.string.ok),
                (dialog, which) -> dialog.dismiss());
        builder.create().show();
    }

    public boolean isDeviceConnected(Context context) {
        boolean status = false;

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            NetworkCapabilities cap = cm.getNetworkCapabilities(cm.getActiveNetwork());
            if (cap != null)
                status = cap.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (cm != null && cm.getActiveNetwork() != null && cm.getNetworkCapabilities(cm.getActiveNetwork()) != null)
                status = true;
        } else {
            if (cm != null && cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnectedOrConnecting())
                status = true;
        }
        return status;
    }
}
