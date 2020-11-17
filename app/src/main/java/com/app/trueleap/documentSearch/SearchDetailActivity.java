package com.app.trueleap.documentSearch;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import com.app.trueleap.R;
import com.app.trueleap.base.BaseActivity;
import com.app.trueleap.databinding.ActivitySearchDetailBinding;
import com.app.trueleap.external.Converter;
import com.app.trueleap.interfaces.responseCallback;
import com.app.trueleap.notification.NotificationActivity;
import com.app.trueleap.notification.NotificationModel;

import java.util.ArrayList;

public class SearchDetailActivity extends BaseActivity implements responseCallback {

    ActivitySearchDetailBinding binding;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_search_detail);
        intent = getIntent();
        context = SearchDetailActivity.this;
        initdata();
        initToolbar();
        initData();
        getNotifications(this);
    }

    private void initdata() {
        binding.studentClass.setText(localStorage.getClassId());
        binding.studentSection.setText(localStorage.getSectionId());
    }

    private void initData() {
        if(intent.getExtras()!=null){
            if(!(intent.getStringExtra("filename").equals(null)))
                binding.fileName.setText(intent.getStringExtra("filename"));
            if(!(intent.getStringExtra("content").equals(null)))
                binding.content.setText(intent.getStringExtra("content"));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
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