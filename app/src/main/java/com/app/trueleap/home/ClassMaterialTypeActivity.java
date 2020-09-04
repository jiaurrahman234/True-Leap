package com.app.trueleap.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;

import com.app.trueleap.Assignmentmodule.AssignmentActivity;
import com.app.trueleap.Classnotemodule.ClassNotesActivity;
import com.app.trueleap.Classnotemodule.model.ClassnoteModel;
import com.app.trueleap.R;
import com.app.trueleap.base.BaseActivity;
import com.app.trueleap.databinding.FragmentClassmaterialtypeBinding;
import com.app.trueleap.home.studentsubject.CalendarModel;
import com.app.trueleap.home.studentsubject.ClassModel;
import com.app.trueleap.home.studentsubject.DocumentsModel;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import static com.app.trueleap.external.CommonFunctions.parse_date;

public class ClassMaterialTypeActivity extends BaseActivity {
    FragmentClassmaterialtypeBinding binding;
    String sujectName = "", classId = "";
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
                try {
                    ArrayList<ClassnoteModel> classnoteModelArrayList = new ArrayList<>();
                    for (int i = 0; i < calendarModelArrayList.size(); i++) {
                        Log.d(TAG, "khkhf: " + calendarModelArrayList.size() + ", " + classId + " , " + calendarModelArrayList.get(i).getId());
                        //CalendarModel calendarModel = calendarModelArrayList.get(i);
                        if (Integer.parseInt(classId) == calendarModelArrayList.get(i).getId()) {

                            Log.d(TAG, "khkhf: " + calendarModelArrayList.get(i).getPeriodId());
                            for (int j = 0; j < classModelArrayList.size(); j++) {
                                ClassModel classModel = classModelArrayList.get(j);
                                Log.d(TAG, "vvcvc: " + calendarModelArrayList.get(i).getPeriodId() + "," + classModel.getUniqueperiodid());
                                if (calendarModelArrayList.get(i).getPeriodId().equalsIgnoreCase(classModel.getUniqueperiodid())) {
                                    Log.d(TAG, "hkghfg: " + classModel.getSubject());
                                    if (!classModel.getDocumentsModelArrayList().isEmpty()) {

                                        for (int k = 0; k < classModel.getDocumentsModelArrayList().size(); k++) {
                                            DocumentsModel documentsModel = classModel.getDocumentsModelArrayList().get(k);
                                            classnoteModelArrayList.add(new ClassnoteModel(
                                                    documentsModel.getId(),
                                                    documentsModel.getTitle(),
                                                    documentsModel.getNote(),
                                                    classModel.getStartdate(),
                                                    documentsModel.getFilename(),
                                                    documentsModel.getType()));
                                        }
                                        startActivity(new Intent(ClassMaterialTypeActivity.this, ClassNotesActivity.class)
                                                .putExtra("subject_code", sujectName)
                                                .putExtra("subject_name", sujectName)
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
                                                                    Log.d(TAG, "khkhf: " + calendarModelArrayList.size() + ", " + classId + " , " + calendarModelArrayList.get(i).getId());
                                                                    //CalendarModel calendarModel = calendarModelArrayList.get(i);
                                                                    if (Integer.parseInt(classId) == calendarModelArrayList.get(i).getId()) {

                                                                        Log.d(TAG, "khkhf: " + calendarModelArrayList.get(i).getPeriodId());
                                                                        for (int j = 0; j < classModelArrayList.size(); j++) {
                                                                            ClassModel classModel = classModelArrayList.get(j);
                                                                            Log.d(TAG, "vvcvc: " + calendarModelArrayList.get(i).getPeriodId() + "," + classModel.getUniqueperiodid());
                                                                            if (calendarModelArrayList.get(i).getPeriodId().equalsIgnoreCase(classModel.getUniqueperiodid())) {
                                                                                Log.d(TAG, "hkghfg: " + classModel.getSubject());
                                                                                if (!classModel.getAssgnmentModelArrayList().isEmpty()) {
                                                                                    for (int k = 0; k < classModel.getAssgnmentModelArrayList().size(); k++) {
                                                                                        DocumentsModel documentsModel = classModel.getAssgnmentModelArrayList().get(k);
                                                                                        AssignmentModelArrayList.add(new ClassnoteModel(
                                                                                                documentsModel.getId(),
                                                                                                documentsModel.getTitle(),
                                                                                                documentsModel.getNote(),
                                                                                                classModel.getStartdate(),
                                                                                                documentsModel.getFilename(),
                                                                                                documentsModel.getType()));
                                                                                    }
                                                                                }
                                                                                break;
                                                                            }
                                                                        }
                                                                        break;
                                                                    }
                                                                }


                                                                startActivity(new Intent(ClassMaterialTypeActivity.this, AssignmentActivity.class)
                                                                        .putExtra("subject_code", sujectName)
                                                                        .putExtra("subject_name", sujectName)
                                                                        .putExtra("assignment", AssignmentModelArrayList));
                                                            } catch (Exception e) {
                                                                e.printStackTrace();
                                                            }

                                                        }
                                                    }
        );

    }

    private void initdata() {
        try {
            Intent intent = getIntent();
            if (intent.getExtras() != null) {
                classModelArrayList = new ArrayList<>();
                calendarModelArrayList = new ArrayList<>();
                classId = intent.getStringExtra("class_id");
                sujectName = intent.getStringExtra("subject_name");
                //classModelArrayList = (ArrayList<ClassModel>) intent.getExtras().getSerializable("class_data");
                calendarModelArrayList = (ArrayList<CalendarModel>) intent.getExtras().getSerializable("calendar_data");
                Log.d(TAG, "classModelArrayList " + classId + "," + calendarModelArrayList.size() + " , " + classModelArrayList.size());
            }

            if (localStorage.getClassData() != null) {
                String jsonClass = localStorage.getClassData();
                Type type = new TypeToken<ArrayList<ClassModel>>() {
                }.getType();
                classModelArrayList = gson.fromJson(jsonClass, type);

            }

            for (int i = 0; i < calendarModelArrayList.size(); i++) {
                if (Integer.parseInt(classId) == calendarModelArrayList.get(i).getId()) {
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
