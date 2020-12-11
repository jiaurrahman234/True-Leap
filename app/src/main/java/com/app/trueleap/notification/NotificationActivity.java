package com.app.trueleap.notification;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import com.app.trueleap.R;
import com.app.trueleap.base.BaseActivity;
import com.app.trueleap.databinding.ActivityNotificationBinding;
import com.app.trueleap.interfaces.recyclerviewClickListener;
import com.app.trueleap.interfaces.responseCallback;
import com.app.trueleap.notification.adapter.notification_adapter;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;

public class NotificationActivity extends BaseActivity implements recyclerviewClickListener, responseCallback {
    Intent intent;
    ActivityNotificationBinding binding;
    ArrayList<NotificationModel> notificationlist;
    notification_adapter notificationAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_notification);
        intent = getIntent();
        context = NotificationActivity.this;
        initdata();
        initToolbar();
    }

    private void initdata() {
        binding.studentClass.setText(localStorage.getClassId());
        binding.studentSection.setText(localStorage.getSectionId());
        notificationlist = new ArrayList<>();
        getNotifications(this);
    }

    private void populateNotificationlist( ArrayList<NotificationModel> notificationlist) {
        notificationAdapter = new notification_adapter(context, notificationlist, this);
        LinearLayoutManager llayoutmanager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        binding.rvNotification.setLayoutManager(llayoutmanager);
        binding.rvNotification.setAdapter(notificationAdapter);
        notificationAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClicked(int position) {
        readNotifications(notificationlist.get(position).getNotificationid());
        notificationlist.get(position).setViewed(true);
        ViewNotification(notificationlist.get(position).getNote());
        notificationAdapter.notifyDataSetChanged();
    }

    public void ViewNotification(String content) {
        try {
            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(context, R.style.MyAlertDialogStyle);
            View titleView = getLayoutInflater().inflate(R.layout.alerdialog_title, null);
            builder.setCustomTitle(titleView);
            builder.setMessage(content)
                    .setPositiveButton(R.string.close, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            dialog.dismiss();
                        }
                    });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSuccess(@NonNull ArrayList<NotificationModel> value) {
        Log.d(TAG,"SIZE: "+value.size());
        if(value.size()>0) {
            for(int i = value.size()-1; i>=0; i--){
                notificationlist.add(value.get(i));
            }
            Log.d(TAG, "array size " + notificationlist.size());
            populateNotificationlist(notificationlist);
        }
    }
}