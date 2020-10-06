package com.app.trueleap.home;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import com.app.trueleap.Assignmentmodule.AssignmentActivity;
import com.app.trueleap.Classnotemodule.ClassNotesActivity;
import com.app.trueleap.Classnotemodule.model.ClassnoteModel;
import com.app.trueleap.R;
import com.app.trueleap.auth.LoginActivity;
import com.app.trueleap.base.BaseActivity;
import com.app.trueleap.databinding.FragmentClassmaterialtypeBinding;
import com.app.trueleap.gradebook.GradebookActivity;
import com.app.trueleap.home.studentsubject.model.CalendarModel;
import com.app.trueleap.home.studentsubject.model.ClassModel;
import com.app.trueleap.home.studentsubject.model.DocumentsModel;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import static com.app.trueleap.external.CommonFunctions.parse_date;

public class ClassMaterialTypeActivity extends BaseActivity {
    FragmentClassmaterialtypeBinding binding;
    String sujectName = "", classDate = "";
    ArrayList<ClassModel> classModelArrayList;
    ArrayList<CalendarModel> calendarModelArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.fragment_classmaterialtype);
        initToolbar();
        initdata();
        initListeners();
    }

    private void initListeners() {
        binding.actionClassnote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    ArrayList<ClassnoteModel> classnoteModelArrayList = new ArrayList<>();
                    for (int i = 0; i < calendarModelArrayList.size(); i++) {
                        if (classDate.equalsIgnoreCase(calendarModelArrayList.get(i).getDate())) {
                            for (int j = 0; j < classModelArrayList.size(); j++) {
                                ClassModel classModel = classModelArrayList.get(j);
                                if (calendarModelArrayList.get(i).getPeriodId().equalsIgnoreCase(classModel.getUniqueperiodid())) {
                                    if (!classModel.getDocumentsModelArrayList().isEmpty()) {
                                        for (int k = 0; k < classModel.getDocumentsModelArrayList().size(); k++) {
                                            DocumentsModel documentsModel = classModel.getDocumentsModelArrayList().get(k);
                                            classnoteModelArrayList.add(new ClassnoteModel(
                                                    documentsModel.getId(),
                                                    documentsModel.getTitle(),
                                                    documentsModel.getFile_url(),
                                                    documentsModel.getNote(),
                                                    classModel.getStartdate(),
                                                    documentsModel.getFilename(),
                                                    documentsModel.getType(),documentsModel.getValidupto()));
                                        }
                                        startActivity(new Intent(ClassMaterialTypeActivity.this, ClassNotesActivity.class)
                                                .putExtra("period_id", classModel.getUniqueperiodid())
                                                .putExtra("subject_name", sujectName)
                                                .putExtra("class_date", binding.classDate.getText().toString())
                                                .putExtra("class_note", classnoteModelArrayList));

                                    }else{
                                        Toast.makeText(context,"Class notes not found!",Toast.LENGTH_SHORT).show();
                                    }
                                    break;
                                }
                            }
                            break;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        binding.actionAssignment.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            try {
                                                                ArrayList<ClassnoteModel> AssignmentModelArrayList = new ArrayList<>();
                                                                for (int i = 0; i < calendarModelArrayList.size(); i++) {
                                                                   if (classDate.equalsIgnoreCase(calendarModelArrayList.get(i).getDate())) {
                                                                        for (int j = 0; j < classModelArrayList.size(); j++) {
                                                                            ClassModel classModel = classModelArrayList.get(j);
                                                                            if (calendarModelArrayList.get(i).getPeriodId().equalsIgnoreCase(classModel.getUniqueperiodid())) {
                                                                                if (!classModel.getAssgnmentModelArrayList().isEmpty()) {
                                                                                    for (int k = 0; k < classModel.getAssgnmentModelArrayList().size(); k++) {
                                                                                        DocumentsModel documentsModel = classModel.getAssgnmentModelArrayList().get(k);
                                                                                        AssignmentModelArrayList.add(new ClassnoteModel(
                                                                                                documentsModel.getId(),
                                                                                                documentsModel.getTitle(),
                                                                                                documentsModel.getFile_url(),
                                                                                                documentsModel.getNote(),
                                                                                                classModel.getStartdate(),
                                                                                                documentsModel.getFilename(),
                                                                                                documentsModel.getType(),documentsModel.getValidupto()));
                                                                                    }

                                                                                    startActivity(new Intent(ClassMaterialTypeActivity.this, AssignmentActivity.class)
                                                                                            .putExtra("period_id", classModel.getUniqueperiodid())
                                                                                            .putExtra("subject_name", sujectName)
                                                                                            .putExtra("class_date", binding.classDate.getText().toString())
                                                                                            .putExtra("assignment", AssignmentModelArrayList));
                                                                                }
                                                                                else{
                                                                                    Toast.makeText(context,"No Asssignment uploaded!",Toast.LENGTH_SHORT).show();
                                                                                }
                                                                                break;
                                                                            }
                                                                        }
                                                                        break;
                                                                    }
                                                                }

                                                            } catch (Exception e) {
                                                                e.printStackTrace();
                                                            }

                                                        }
                                                    }
        );
        binding.actionGradebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, GradebookActivity.class).putExtra("subject_name",sujectName));
            }
        });
    }

    private void initdata() {
        try {
            Intent intent = getIntent();
            if (intent.getExtras() != null) {
                classModelArrayList = new ArrayList<>();
                calendarModelArrayList = new ArrayList<>();
                classDate = intent.getStringExtra("classDate");
                sujectName = intent.getStringExtra("subject_name");
                //classModelArrayList = (ArrayList<ClassModel>) intent.getExtras().getSerializable("class_data");
                calendarModelArrayList = (ArrayList<CalendarModel>) intent.getExtras().getSerializable("calendar_data");

            }

            if (localStorage.getClassData() != null) {
                String jsonClass = localStorage.getClassData();
                Type type = new TypeToken<ArrayList<ClassModel>>() {
                }.getType();
                classModelArrayList = gson.fromJson(jsonClass, type);
            }

            for (int i = 0; i < calendarModelArrayList.size(); i++) {
                if (classDate.equalsIgnoreCase(calendarModelArrayList.get(i).getDate())) {
                    for (int j = 0; j < classModelArrayList.size(); j++) {
                        ClassModel classModel = classModelArrayList.get(j);
                        if (calendarModelArrayList.get(i).getPeriodId().equalsIgnoreCase(classModel.getUniqueperiodid())) {
                            binding.classDate.setText(parse_date(classModel.getStartdate()));
                            break;
                        }
                    }
                    break;
                }
            }

            binding.studentClass.setText(localStorage.getClassId());
            binding.studentSection.setText(localStorage.getSectionId());

            if (!sujectName.equals(""))
                binding.sujectName.setText(sujectName);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
