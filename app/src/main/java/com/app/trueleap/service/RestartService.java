package com.app.trueleap.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

public class RestartService extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("BaseActivity", "Broadcast Receiver");
        if (intent.getBooleanExtra("startService", false)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(new Intent(context,documentuploadService.class));
            } else {
                context.startService(new Intent(context,documentuploadService.class));
            }
        }
    }
}
