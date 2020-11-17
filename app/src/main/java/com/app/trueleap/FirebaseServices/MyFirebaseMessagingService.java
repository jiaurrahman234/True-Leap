
package com.app.trueleap.FirebaseServices;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.app.trueleap.R;
import com.app.trueleap.auth.LoginActivity;
import com.app.trueleap.external.DatabaseHelper;
import com.app.trueleap.external.LocalStorage;
import com.app.trueleap.home.MainActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    LocalStorage localStorage;
    DatabaseHelper databaseHelper;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        try {
            localStorage = new LocalStorage(this);
            Log.d(TAG, "Notification From firebase: " + remoteMessage.getFrom());

            if (remoteMessage == null)
                return;

            Log.d(TAG, "Notification text: " + remoteMessage.getNotification().getBody());
            if (remoteMessage.getNotification() != null) {
                Log.d(TAG, "Notification Body: " + remoteMessage.getNotification().getBody());
                //localStorage.setNotification(remoteMessage.getNotification().getBody());
                Intent intent;

                if (localStorage.isUserLoggedIn()) {
                    intent = new Intent(this, MainActivity.class);
                } else {
                    intent = new Intent(this, LoginActivity.class);
                }

                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

                String channelId = "Default";
                NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.logo)
                        .setContentTitle(remoteMessage.getNotification().getTitle())
                        .setContentText(remoteMessage.getNotification().getBody()).setAutoCancel(true).setContentIntent(pendingIntent);
                NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    NotificationChannel channel = new NotificationChannel(channelId, "Default channel", NotificationManager.IMPORTANCE_DEFAULT);
                    manager.createNotificationChannel(channel);
                }
                manager.notify(0, builder.build());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);
        if (!token.isEmpty()) {
            Log.d(TAG,"NEW_TOKEN "+token);
            insertIntoDb(token);
        }
    }
    private void insertIntoDb(String token) {
        String android_id = Settings.Secure.getString(getApplicationContext().getContentResolver(),Settings.Secure.ANDROID_ID);
        Log.d(TAG, "device id: " + android_id);
        databaseHelper = new DatabaseHelper(this);
        ContentValues values1 = new ContentValues();
        values1.put(databaseHelper.FCM_TOKEN, token);
        databaseHelper.saveInLocalStorage(DatabaseHelper.TABLE_FILE_UPLOAD, values1);
        Log.d(TAG,"Data saved");
    }
}
