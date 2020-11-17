package com.app.trueleap.notification;

import androidx.annotation.NonNull;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import com.app.trueleap.R;
import com.app.trueleap.base.BaseActivity;
import com.app.trueleap.databinding.ActivityNotificationBinding;
import com.app.trueleap.interfaces.recyclerviewClickListener;
import com.app.trueleap.interfaces.responseCallback;
import com.app.trueleap.notification.adapter.notification_adapter;
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
        ViewNotification(notificationlist.get(position).getNote());
    }

    public void ViewNotification(String content) {
        try {
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context, R.style.MyAlertDialogStyle);
            builder.setTitle(R.string.message)
                    .setMessage(content)
                    .setPositiveButton(R.string.close, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            dialog.dismiss();
                        }
                    });
            android.app.AlertDialog alertDialog = builder.create();
            alertDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSuccess(@NonNull ArrayList<NotificationModel> value) {
        if(value.size()>0) {
            notificationlist = value;
            for(int i = value.size()-1; i>=0; i-- ){
                notificationlist.add(value.get(i));
            }
            populateNotificationlist(notificationlist);
        }
    }
}