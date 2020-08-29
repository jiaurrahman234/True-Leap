package com.app.trueleap.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;

import com.app.trueleap.Assignmentmodule.AssignmentActivity;
import com.app.trueleap.Classnotemodule.ClassNotesActivity;
import com.app.trueleap.R;
import com.app.trueleap.base.BaseActivity;
import com.app.trueleap.base.BaseFragment;
import com.app.trueleap.databinding.FragmentClassmaterialtypeBinding;
import com.app.trueleap.home.studentsubject.ClassModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ClassMaterialTypeActivity extends BaseActivity {
    FragmentClassmaterialtypeBinding binding;
    String sujectName = "",classId="";
    ArrayList<ClassModel> classModelArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.fragment_classmaterialtype);
        initToolbar();
        initdata();
        initListeners();

    }

    private void initToolbar() {
        TextView toolbar_tv;
        Toolbar toolbar;
        toolbar_tv = (TextView) findViewById(R.id.toolbar_tv);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void initListeners() {

        binding.actionClassnote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent noteintent = new Intent(ClassMaterialTypeActivity.this, ClassNotesActivity.class);
                noteintent.putExtra("subject_code", sujectName);
                noteintent.putExtra("subject_name", sujectName);
                startActivity(noteintent);
            }
        });
        binding.actionAssignment.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            Intent assignintent = new Intent(ClassMaterialTypeActivity.this, AssignmentActivity.class);
                                                            assignintent.putExtra("subject_code", sujectName);
                                                            assignintent.putExtra("subject_name", sujectName);
                                                            startActivity(assignintent);
                                                        }
                                                    }
        );

    }

    private void initdata() {
        try{
            classModelArrayList = new ArrayList<>();
            Intent intent = getIntent();
            if (intent.getExtras() != null) {
                Type type = new TypeToken<List<String>>() {
                }.getType();
                classId = intent.getStringExtra("class_id");
                sujectName = intent.getStringExtra("subject_name");
                classModelArrayList = new Gson().fromJson(getIntent().getStringExtra("calendar_data"), type);
                Log.d(TAG,"classModelArrayList "+classId+","+classModelArrayList.size());
            }
            binding.studentClass.setText(localStorage.getClassId());
            binding.studentSection.setText(localStorage.getSectionId());

            if (!sujectName.equals(""))
                binding.sujectName.setText(sujectName);
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
