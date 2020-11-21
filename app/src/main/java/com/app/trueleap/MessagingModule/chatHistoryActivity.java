package com.app.trueleap.MessagingModule;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.app.trueleap.Classnotemodule.model.ClassnoteModel;
import com.app.trueleap.MessagingModule.adapter.chat_adapter;
import com.app.trueleap.MessagingModule.model.messageModel;
import com.app.trueleap.R;
import com.app.trueleap.base.BaseActivity;
import com.app.trueleap.databinding.ActivityChatHistoryBinding;
import com.app.trueleap.external.Converter;
import com.app.trueleap.interfaces.recyclerviewClickListener;
import com.app.trueleap.interfaces.responseCallback;
import com.app.trueleap.notification.NotificationActivity;
import com.app.trueleap.notification.NotificationModel;

import java.util.ArrayList;

public class chatHistoryActivity extends BaseActivity implements recyclerviewClickListener, chatResponseCallback , responseCallback {

    ActivityChatHistoryBinding binding;
    Intent intent;
    ArrayList<messageModel> chat;
    chat_adapter chat_adapter;
    String class_date, subject_name, period_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_chat_history);
        intent = getIntent();
        context = chatHistoryActivity.this;
        initdata();
        initToolbar();
        getNotifications(this);
    }

    private void initdata() {
        binding.studentClass.setText(localStorage.getClassId());
        binding.studentSection.setText(localStorage.getSectionId());
        if (intent.getExtras() != null) {
            subject_name = intent.getStringExtra("subject_name");
            class_date = intent.getStringExtra("class_date");
            period_id = intent.getStringExtra("period_id");
        }
        binding.studentClass.setText(localStorage.getClassId());
        binding.studentSection.setText(localStorage.getSectionId());
        binding.classDate.setText(class_date);
        binding.sujectName.setText(subject_name +" Chat History");
        chat = new ArrayList<>();
        getchatHistory(this,subject_name, period_id );
    }

    private void populatechat( ArrayList<messageModel> chat) {
        chat_adapter = new chat_adapter(context, chat, this);
        LinearLayoutManager llayoutmanager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        binding.rvChatHistory.setLayoutManager(llayoutmanager);
        binding.rvChatHistory.setAdapter(chat_adapter);
        chat_adapter.notifyDataSetChanged();
    }

    @Override
    public void onClicked(int position) {

    }

    @Override
    public void onSucceschat(@NonNull ArrayList<messageModel> value) {
        if(value.size()>0) {
            chat = value;
            populatechat(value);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem menuItem = menu.findItem(R.id.action_notification);
        menuItem.setIcon(Converter.convertLayoutToImage(this, localStorage.getNotificationCount(), R.drawable.ic_baseline_notifications_24));
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_notification:
                startActivity(new Intent(this, NotificationActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSuccess(@NonNull ArrayList<NotificationModel> value) {
        if(value.size()>0) {
            invalidateOptionsMenu();
        }
    }
}