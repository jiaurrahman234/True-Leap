package com.app.trueleap.Assignmentmodule;

import android.content.Intent;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.app.trueleap.Assignmentmodule.adapter.Asssignment_adapter;
import com.app.trueleap.Assignmentmodule.interfaces.assignmentClickListener;
import com.app.trueleap.Classnotemodule.model.ClassnoteModel;
import com.app.trueleap.R;
import com.app.trueleap.base.BaseActivity;
import com.app.trueleap.databinding.ActivityAssignmentBinding;
import com.app.trueleap.home.studentsubject.model.ClassModel;

import java.util.ArrayList;

public class AssignmentActivity extends BaseActivity implements assignmentClickListener {
    Intent intent;
    String subject_name,class_date, period_id;
    ActivityAssignmentBinding binding;
    ArrayList<ClassModel> classModelArrayList;
    ArrayList<ClassnoteModel> assignment;
    Asssignment_adapter assignment_adpater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_assignment);
        intent = getIntent();
        initToolbar();
        initData();
    }

    private void initData() {
        try{
            assignment = new ArrayList<>();
            if (intent.getExtras() != null) {
                classModelArrayList = new ArrayList<>();
                subject_name = intent.getStringExtra("subject_name");
                class_date = intent.getStringExtra("class_date");
                period_id = intent.getStringExtra("period_id");
                assignment = (ArrayList<ClassnoteModel>) intent.getExtras().getSerializable("assignment");
            }
            binding.studentClass.setText(localStorage.getClassId());
            binding.studentSection.setText(localStorage.getSectionId());
            binding.classDate.setText(class_date);
            binding.sujectName.setText(subject_name +" Assignments");
            populateAssignments();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void populateAssignments() {
        try{
            assignment_adpater  = new Asssignment_adapter(context,assignment,this);
            LinearLayoutManager llayoutmanager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
            binding.rvClassNote.setLayoutManager(llayoutmanager);
            binding.rvClassNote.setAdapter(assignment_adpater);
            assignment_adpater.notifyDataSetChanged();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onClicked(int position) {
        Intent intent = new Intent(AssignmentActivity.this, AssignmentViewActivity.class);
        intent.putExtra("assignment",assignment.get(position));
        intent.putExtra("subject_name",subject_name);
        intent.putExtra("class_date",class_date);
        intent.putExtra("period_id",period_id);
        startActivity(intent);
    }
}