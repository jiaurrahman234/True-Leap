package com.app.trueleap.interfaces;
import androidx.annotation.NonNull;
import com.app.trueleap.notification.NotificationModel;
import java.util.ArrayList;

public interface responseCallback {
    void onSuccess(@NonNull ArrayList<NotificationModel> value);
}