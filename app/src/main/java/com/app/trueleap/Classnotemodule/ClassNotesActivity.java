package com.app.trueleap.Classnotemodule;

import android.content.Intent;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.app.trueleap.Classnotemodule.adapter.classnote_adapter;
import com.app.trueleap.Classnotemodule.interfaces.noteClickListener;
import com.app.trueleap.Classnotemodule.model.ClassnoteModel;
import com.app.trueleap.R;
import com.app.trueleap.base.BaseActivity;
import com.app.trueleap.databinding.ActivityClassNotesBinding;
import com.app.trueleap.home.studentsubject.model.ClassModel;

import java.util.ArrayList;

public class ClassNotesActivity extends BaseActivity implements noteClickListener {

    ActivityClassNotesBinding binding;
    Intent intent;
    String subject_name,period_id, class_date;
    ArrayList<ClassModel> classModelArrayList;
    ArrayList<ClassnoteModel> class_notes;
    classnote_adapter note_adpater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_class_notes);
        intent = getIntent();
        initToolbar();
        initData();
    }

    private void initData() {
        try{
            class_notes = new ArrayList<>();
            if (intent.getExtras() != null) {
                classModelArrayList = new ArrayList<>();
                period_id = intent.getStringExtra("period_id");
                subject_name = intent.getStringExtra("subject_name");
                class_date = intent.getStringExtra("class_date");
                class_notes = (ArrayList<ClassnoteModel>) intent.getExtras().getSerializable("class_note");
            }
            binding.studentClass.setText(localStorage.getClassId());
            binding.studentSection.setText(localStorage.getSectionId());
            binding.classDate.setText(class_date);
            binding.sujectName.setText(subject_name +" Class Notes");
            populatenotes();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void populatenotes() {
        note_adpater  = new classnote_adapter(context,class_notes,this);
        LinearLayoutManager llayoutmanager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        binding.rvClassNote.setLayoutManager(llayoutmanager);
        binding.rvClassNote.setAdapter(note_adpater);
        note_adpater.notifyDataSetChanged();
    }

    @Override
    public void onClicked(int position) {
        Intent intent = new Intent(ClassNotesActivity.this, ViewClassNoteActivity.class);
        intent.putExtra("class_note",class_notes.get(position));
        intent.putExtra("subject_name",subject_name);
        intent.putExtra("period_id",period_id);
        intent.putExtra("class_date",class_date);
        startActivity(intent);
    }
}