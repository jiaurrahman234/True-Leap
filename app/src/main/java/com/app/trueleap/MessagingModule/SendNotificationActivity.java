package com.app.trueleap.MessagingModule;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import com.app.trueleap.MessagingModule.model.TeacherModel;
import com.app.trueleap.R;
import com.app.trueleap.base.BaseActivity;
import com.app.trueleap.databinding.ActivitySendNotificationBinding;
import com.app.trueleap.external.Converter;
import com.app.trueleap.interfaces.responseCallback;
import com.app.trueleap.notification.NotificationActivity;
import com.app.trueleap.notification.NotificationModel;
import com.google.android.material.snackbar.Snackbar;
import java.util.ArrayList;

public class SendNotificationActivity extends BaseActivity implements teacherListResponseCallback, responseCallback, notifyResponseCallback {

    ActivitySendNotificationBinding binding;
    Intent intent;
    ArrayList<TeacherModel> teacherArrayList;
    ArrayList<String> teacherlist;
    int selected=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_send_notification);
        intent = getIntent();
        context = SendNotificationActivity.this;
        initdata();
        initToolbar();
        getTeacherList(this);
        initListner();
    }

    private void initListner() {

        binding.sendNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!binding.messageTxt.getText().toString().isEmpty()){
                    notifyTeacher(teacherArrayList.get(selected).getTeacher_id(), binding.messageTxt.getText().toString(), SendNotificationActivity.this );
                } else {
                    binding.messageTxt.setError(getString(R.string.required_field));
                }
            }
        });
        binding.selectTeacher.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG,"question: "+teacherArrayList.get(position));
                selected = position;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void initdata() {
        teacherArrayList = new ArrayList<>();
        teacherlist = new ArrayList<>();
        binding.studentClass.setText(localStorage.getClassId());
        binding.studentSection.setText(localStorage.getSectionId());
        getNotifications(this);
    }

    @Override
    public void onSuccessteacherList(@NonNull ArrayList<TeacherModel> value) {
        if(value.size()>0) {
            teacherArrayList = value;
            for(int i=0; i < teacherArrayList.size() ; i++ ) {
                teacherlist.add(teacherArrayList.get(i).getTeacher_name());
            }
            populateTeachers();
        }
    }

    private void populateTeachers() {
        ArrayAdapter adapter = new ArrayAdapter<String>(context,R.layout.spinner_item_custom_simple,teacherlist);
        binding.selectTeacher.setAdapter(adapter);
        binding.selectTeacher.setSelection(0);
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

    @Override
    public void onSuccessfullNotify() {
        Snackbar snackbar = Snackbar
                .make(binding.rootlayout,"Teacher Notified Successfully", Snackbar.LENGTH_LONG);
        snackbar.show();
        binding.messageTxt.setText("");
    }
}