package com.app.trueleap.Classnotemodule;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.app.trueleap.Classnotemodule.adapter.classnote_adapter;
import com.app.trueleap.Classnotemodule.interfaces.noteClickListener;
import com.app.trueleap.Classnotemodule.model.ClassnoteModel;
import com.app.trueleap.R;
import com.app.trueleap.auth.LoginActivity;
import com.app.trueleap.base.BaseActivity;
import com.app.trueleap.databinding.ActivityClassNotesBinding;
import com.app.trueleap.external.Utils;
import com.app.trueleap.home.studentsubject.ClassModel;
import com.app.trueleap.home.studentsubject.HomeSubjectsFragment;

import java.util.ArrayList;

public class ClassNotesActivity extends BaseActivity implements noteClickListener {

    ActivityClassNotesBinding binding;
    Intent intent;
    String subject_code;
    String subject_name;
    Context context;
    ArrayList<ClassModel> classModelArrayList;
    ArrayList<ClassnoteModel> class_notes;
    classnote_adapter note_adpater;
    TextView toolbar_tv;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_class_notes);
        intent = getIntent();
        context = ClassNotesActivity.this;
        initToolbar();
        class_notes = new ArrayList<>();
        if (intent.getExtras() != null) {
            classModelArrayList = new ArrayList<>();
            subject_code = intent.getStringExtra("subject_code");
            subject_name = intent.getStringExtra("subject_name");
            class_notes = (ArrayList<ClassnoteModel>) intent.getExtras().getSerializable("class_note");
        }
        initData();
        /*
        ClassnotesFragmentListing fragmentListing = new ClassnotesFragmentListing().newInstance(subject_code, subject_name);
        loadFragment(fragmentListing);
        */
    }

    private void initData() {
        try{
            binding.studentClass.setText(localStorage.getClassId());
            binding.studentSection.setText(localStorage.getSectionId());
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


    @Override
    public void onClicked(int position) {

        Intent intent = new Intent(ClassNotesActivity.this, ViewClassNoteActivity.class);
        intent.putExtra("class_note",class_notes.get(position));
        startActivity(intent);

    }
}