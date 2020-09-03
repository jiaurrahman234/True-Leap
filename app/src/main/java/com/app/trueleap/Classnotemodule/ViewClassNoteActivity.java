package com.app.trueleap.Classnotemodule;

import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.app.trueleap.Classnotemodule.model.ClassnoteModel;
import com.app.trueleap.R;
import com.app.trueleap.base.BaseActivity;
import com.app.trueleap.databinding.ActivityViewClassNoteBinding;

import static com.app.trueleap.external.CommonFunctions.parse_date;

public class ViewClassNoteActivity extends BaseActivity {

    ActivityViewClassNoteBinding binding;
    Intent intent;
    String subject_code;
    String subject_name;
    Context context;
    ClassnoteModel class_note;
    TextView toolbar_tv;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_view_class_note);
        intent = getIntent();
        context = ViewClassNoteActivity.this;
        initToolbar();
        if (intent.getExtras() != null) {
            class_note = (ClassnoteModel) intent.getExtras().getParcelable("class_note");
        }
        initData();
    }

    private void initData() {
        try{
            binding.studentClass.setText(localStorage.getClassId());
            binding.studentSection.setText(localStorage.getSectionId());
            binding.sujectName.setText(subject_name +" Class Notes");
            renderContent();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void initToolbar() {
        toolbar_tv = (TextView) findViewById(R.id.toolbar_tv);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void renderContent() {
        binding.noteTitle.setText(class_note.getNote_title());
        binding.noteTextFull.setText(class_note.getNote_text());
        binding.noteDate.setText(parse_date(class_note.getUploaded_date()));

        if(class_note.getNote_doc_file()!=null){
            binding.fileName.setText(class_note.getNote_doc_file());
        }else {
            binding.fileTitle.setVisibility(View.GONE);
        }
    }

}