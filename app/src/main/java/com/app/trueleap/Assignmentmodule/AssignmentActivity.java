package com.app.trueleap.Assignmentmodule;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.app.trueleap.Assignmentmodule.adapter.Asssignment_adapter;
import com.app.trueleap.Assignmentmodule.interfaces.assignmentClickListener;
import com.app.trueleap.Classnotemodule.model.ClassnoteModel;
import com.app.trueleap.R;
import com.app.trueleap.auth.LoginActivity;
import com.app.trueleap.base.BaseActivity;
import com.app.trueleap.databinding.ActivityAssignmentBinding;
import com.app.trueleap.home.studentsubject.model.ClassModel;

import java.util.ArrayList;

public class AssignmentActivity extends BaseActivity implements assignmentClickListener {
    Intent intent;
    String subject_name;
    ActivityAssignmentBinding binding;
    Context context;
    ArrayList<ClassModel> classModelArrayList;
    ArrayList<ClassnoteModel> assignment;
    Asssignment_adapter assignment_adpater;
    TextView toolbar_tv;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_assignment);
        intent = getIntent();
        context = AssignmentActivity.this;
        initToolbar();
        assignment = new ArrayList<>();
        if (intent.getExtras() != null) {
            classModelArrayList = new ArrayList<>();
            subject_name = intent.getStringExtra("subject_name");
            assignment = (ArrayList<ClassnoteModel>) intent.getExtras().getSerializable("assignment");
        }
        initData();
    }

    private void initData() {
        try{
            binding.studentClass.setText(localStorage.getClassId());
            binding.studentSection.setText(localStorage.getSectionId());
            binding.sujectName.setText(subject_name +" Assignments");
            populateAssignments();

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void populateAssignments() {
        assignment_adpater  = new Asssignment_adapter(context,assignment,this);
        LinearLayoutManager llayoutmanager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        binding.rvClassNote.setLayoutManager(llayoutmanager);
        binding.rvClassNote.setAdapter(assignment_adpater);
        assignment_adpater.notifyDataSetChanged();
    }

    private void initToolbar() {
        TextView toolbar_tv;
        Toolbar toolbar;
        toolbar_tv = (TextView) findViewById(R.id.toolbar_tv);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_change_lang:
                showLanguageDialog();
                return true;
            case R.id.action_logout:
                try {
                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context, R.style.MyAlertDialogStyle);
                    builder.setTitle(context.getResources().getString(R.string.confirm))
                            .setIcon(R.drawable.logo)
                            .setMessage(context.getResources().getString(R.string.exit_msg))
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    localStorage.logoutUser();
                                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            })
                            .setNegativeButton(android.R.string.no, null);
                    AlertDialog alertDialog = builder.create();
                    if(!((Activity) context).isFinishing())
                    {
                        alertDialog.show();
                    }
                    alertTheme(alertDialog);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return true;
           /* case R.id.action_settings:
                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                return true;*/

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClicked(int position) {
        Intent intent = new Intent(AssignmentActivity.this, AssignmentViewActivity.class);
        intent.putExtra("assignment",assignment.get(position));
        intent.putExtra("subject_name",subject_name);
        startActivity(intent);
    }
}