package com.appinspire.dailybudget.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.Settings;

import com.appinspire.dailybudget.R;


public final class DialogUtils {

    public static void enableInternet(final Context context) {
        if (context == null) {
            return;
        }

        new AlertDialog.Builder(context)
                .setTitle(context.getResources().getString(R.string.title_internet))
                .setMessage(context.getResources().getString(R.string.msg_internet))
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Settings.ACTION_SETTINGS);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .show();
    }
}
