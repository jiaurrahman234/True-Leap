package com.app.trueleap.classcalenderview;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.alamkanak.weekview.DateTimeInterpreter;
import com.alamkanak.weekview.MonthLoader;
import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;
import com.app.trueleap.R;
import com.app.trueleap.auth.LoginActivity;
import com.app.trueleap.base.BaseActivity;
import com.app.trueleap.home.ClassMaterialTypeActivity;
import com.app.trueleap.home.studentsubject.model.CalendarModel;
import com.app.trueleap.home.studentsubject.model.ClassModel;
import com.app.trueleap.home.studentsubject.model.DocumentsModel;
import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.listeners.OnDayClickListener;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.app.trueleap.external.CommonFunctions.getJSONFromCache;
import static com.app.trueleap.external.CommonFunctions.getdateValue;

public class CalenderViewActivity extends BaseActivity  {
    Intent intent;
    Context context;
    ArrayList<ClassModel> Subjects;
    String Subject_name;
    String selecteduniqueperiodid;
    ArrayList<ClassModel> classModelArrayList = new ArrayList<>();
    ArrayList<CalendarModel> calendarModelArrayList = new ArrayList<>();

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        initData();
    }
    private void initData() {
        Subjects = new ArrayList<>();
        context= CalenderViewActivity.this;
        intent = getIntent();
        if (intent.getExtras() != null) {
            selecteduniqueperiodid = intent.getStringExtra("uniqueperiodid");
            Subject_name = intent.getStringExtra("subject_name");
        }
        JSONArray classes = getJSONFromCache(this);
        try {
            if (classes != null) {
                int position_to_show = 0;
                for (int i = 0; i < classes.length(); i++) {
                    JSONArray level_1 = classes.getJSONArray(i);
                    JSONObject ClassObject = level_1.getJSONObject(0);
                    if (selecteduniqueperiodid.equals(ClassObject.getString("uniqueperiodid"))) {
                        position_to_show = i;
                        break;
                    }
                }
                JSONArray subject_class = classes.getJSONArray(position_to_show);

                for (int j = 0; j < subject_class.length(); j++) {
                    Date curDate = new Date();
                    String startDate = subject_class.getJSONObject(j).getString("startdate");
                    Date classDate = getdateValue(startDate);
                    if (classDate.compareTo(curDate) < 0) {
                        ArrayList<DocumentsModel> documentsModelArrayList = new ArrayList<>();
                        ArrayList<DocumentsModel> assignmentModelArrayList = new ArrayList<>();
                        if (subject_class.getJSONObject(j).has("documents") && subject_class.getJSONObject(j).has("assignments")) {
                            JSONArray documentArray = subject_class.getJSONObject(j).getJSONArray("documents");
                            JSONArray AssignmentArray = subject_class.getJSONObject(j).getJSONArray("assignments");
                            Log.d(TAG, "document " + documentArray.length());
                            if (documentArray.length() > 0 || AssignmentArray.length() > 0) {

                                for (int k = 0; k < documentArray.length(); k++) {
                                    JSONObject documentObj = documentArray.getJSONObject(k);
                                    documentsModelArrayList.add(new DocumentsModel(
                                            documentObj.getString("id"),
                                            documentObj.getString("filename"),
                                            documentObj.getString("type"),
                                            documentObj.getString("title"),
                                            documentObj.getString("note")
                                    ));
                                }

                                for (int k = 0; k < AssignmentArray.length(); k++) {
                                    JSONObject documentObj = AssignmentArray.getJSONObject(k);
                                    assignmentModelArrayList.add(new DocumentsModel(
                                            documentObj.getString("id"),
                                            documentObj.getString("filename"),
                                            documentObj.getString("type"),
                                            documentObj.getString("title"),
                                            documentObj.getString("note")
                                    ));
                                }

                                Log.d(TAG, "jgljfkj: " + documentsModelArrayList.size() + "," + subject_class.getJSONObject(j).getString("startdate"));
                                Subjects.add(
                                        new ClassModel(
                                                subject_class.getJSONObject(j).getString("uniqueperiodid"),
                                                subject_class.getJSONObject(j).getString("teacher"),
                                                subject_class.getJSONObject(j).getString("uniqueteacherid"),
                                                subject_class.getJSONObject(j).getString("startdate"),
                                                subject_class.getJSONObject(j).getString("enddate"),
                                                subject_class.getJSONObject(j).getString("starttime"),
                                                subject_class.getJSONObject(j).getString("endtime"),
                                                subject_class.getJSONObject(0).getString("subject"),
                                                documentsModelArrayList,
                                                assignmentModelArrayList));

                            } else {
                                Subjects.add(
                                        new ClassModel(
                                                subject_class.getJSONObject(j).getString("uniqueperiodid"),
                                                subject_class.getJSONObject(j).getString("teacher"),
                                                subject_class.getJSONObject(j).getString("uniqueteacherid"),
                                                subject_class.getJSONObject(j).getString("startdate"),
                                                subject_class.getJSONObject(j).getString("enddate"),
                                                subject_class.getJSONObject(j).getString("starttime"),
                                                subject_class.getJSONObject(j).getString("endtime"),
                                                subject_class.getJSONObject(0).getString("subject"),
                                                documentsModelArrayList,
                                                assignmentModelArrayList));
                            }
                        } else {
                            Subjects.add(
                                    new ClassModel(
                                            subject_class.getJSONObject(j).getString("uniqueperiodid"),
                                            subject_class.getJSONObject(j).getString("teacher"),
                                            subject_class.getJSONObject(j).getString("uniqueteacherid"),
                                            subject_class.getJSONObject(j).getString("startdate"),
                                            subject_class.getJSONObject(j).getString("enddate"),
                                            subject_class.getJSONObject(j).getString("starttime"),
                                            subject_class.getJSONObject(j).getString("endtime"),
                                            subject_class.getJSONObject(0).getString("subject"),
                                            documentsModelArrayList,
                                            assignmentModelArrayList));
                        }
                    } else {
                        break;
                    }
                }
            }

            int count = 0;
            for (int i = Subjects.size() - 1; i >= 0 && count < 8; i--) {
                classModelArrayList.add(Subjects.get(i));
                count++;
            }


            String[] time = (Subjects.get(0).getStarttime().split("[:.]"));
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(getdateValue(Subjects.get(0).getStartdate()));
        } catch (JSONException e) {
            e.printStackTrace();
        }

       setupCalander();
    }

    private void setupCalander() {
        int countx = 1;
        CalendarView calendarView;
        calendarView = findViewById(R.id.calendarView);

        List<Calendar> calendars = new ArrayList<>();
        List<EventDay> events = new ArrayList<>();
        for (int i = Subjects.size() - 1; i >= 0 && countx < 8; i--) {
            Calendar calendar = Calendar.getInstance();
            Log.d(TAG,"cscd "+Subjects.get(i).getStartdate());
            calendar.setTime(getdateValue(Subjects.get(i).getStartdate()));
            calendars.add(calendar);
            calendarModelArrayList.add(new CalendarModel(i, Subjects.get(i).getUniqueperiodid()));
            EventDay eventDay = new EventDay(calendar, R.drawable.ic_baseline_menu_book_24);
            events.add(eventDay);
            // events = calendarView.getEvents();
            Log.d(TAG, " Events: " + events);
            countx++ ;
        }
        calendarView.setEvents(events);
        calendarView.setHighlightedDays(calendars);
        calendarView.setSelectedDates(calendars);

        calendarView.setOnDayClickListener(new OnDayClickListener() {
            @Override
            public void onDayClick(EventDay eventDay) {
                Calendar clickedDayCalendar = eventDay.getCalendar();
                try {
                    Toast.makeText(context, "Clicked " + classModelArrayList.size(), Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "ghlglk: " + classModelArrayList.size());
                    Gson gson = new Gson();
                    String classData = gson.toJson(classModelArrayList);
                    localStorage.setClass(classData);
                    startActivity(new Intent(CalenderViewActivity.this, ClassMaterialTypeActivity.class)
                            .putExtra("subject_name", Subject_name)
                            .putExtra("class_id", Integer.toString(1))
                            .putExtra("calendar_data", calendarModelArrayList));

            /*for (int i=0;i<classModelArrayList.size();i++){
                ClassModel classModel = classModelArrayList.get(i);
                Log.d(TAG,"jhljgj: "+classModel.getStartdate());
                if (!classModel.getDocumentsModelArrayList().isEmpty()){
                    Log.d(TAG,"xcxcx: "+classModel.getDocumentsModelArrayList().size());
                }
            }*/

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
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

    protected String getClassTitle(String name, String st, String et) {
        return name + "(" + st + " - " + et + ")";
    }
}
