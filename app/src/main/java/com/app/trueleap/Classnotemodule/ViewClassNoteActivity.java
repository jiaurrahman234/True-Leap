package com.app.trueleap.Classnotemodule;

import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import com.app.trueleap.Classnotemodule.model.ClassnoteModel;
import com.app.trueleap.R;
import com.app.trueleap.auth.LoginActivity;
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
            subject_name = (String) intent.getStringExtra("subject_name");
        }
        initData();
        initListeners();
    }

    private void initListeners() {
        binding.fileTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
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

                    builder.setTitle("Confirm")
                            .setIcon(R.drawable.logo)
                            .setMessage("Do you really want to logout?")
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
                    alertDialog.show();
                    alertTheme(alertDialog);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return true;
        }
        return super.onOptionsItemSelected(item);
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