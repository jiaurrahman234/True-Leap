package com.app.trueleap.classcalenderview;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import com.app.trueleap.R;
import com.app.trueleap.base.BaseActivity;
import com.app.trueleap.databinding.ActivityCalenderviewBinding;
import com.app.trueleap.external.Converter;
import com.app.trueleap.home.ClassMaterialActivity;
import com.app.trueleap.home.subject.model.CalendarModel;
import com.app.trueleap.home.subject.model.ClassModel;
import com.app.trueleap.home.subject.model.DocumentsModel;
import com.app.trueleap.interfaces.responseCallback;
import com.app.trueleap.notification.NotificationActivity;
import com.app.trueleap.notification.NotificationModel;
import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.listeners.OnDayClickListener;
import com.google.gson.Gson;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.app.trueleap.external.CommonFunctions.calenderView;
import static com.app.trueleap.external.CommonFunctions.getJSONFromCache;
import static com.app.trueleap.external.CommonFunctions.getdateValue;
import static com.app.trueleap.external.CommonFunctions.simpleDateFormat;

public class CalenderViewActivity extends BaseActivity implements responseCallback {

    Intent intent;
    ArrayList<ClassModel> Subjects;
    String Subject_name;
    String selecteduniqueperiodid;
    ArrayList<ClassModel> classModelArrayList = new ArrayList<>();
    ArrayList<CalendarModel> calendarModelArrayList = new ArrayList<>();
    ActivityCalenderviewBinding binding;
    Calendar calendarMinDate = Calendar.getInstance();

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_calenderview);
        initToolbar();
        initData();
        getNotifications(this);
    }

    private void initData() {
        binding.studentClass.setText(localStorage.getClassId());
        binding.studentSection.setText(localStorage.getSectionId());
        Subjects = new ArrayList<>();
        intent = getIntent();
        if (intent.getExtras() != null) {
            selecteduniqueperiodid = intent.getStringExtra("uniqueperiodid");
            Subject_name = intent.getStringExtra("subject_name");
        }
        binding.sujectName.setText(Subject_name);
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
                for (int j = 1; j < subject_class.length(); j++) {
                    calendarMinDate.setTime(getdateValue(subject_class.getJSONObject(1).getString("startdate")));
                        ArrayList<DocumentsModel> documentsModelArrayList = new ArrayList<>();
                        ArrayList<DocumentsModel> assignmentModelArrayList = new ArrayList<>();
                        JSONArray documentArray = new JSONArray();
                        JSONArray AssignmentArray = new JSONArray();
                            if (subject_class.getJSONObject(j).has("documents") && subject_class.getJSONObject(j).has("assignments")) {
                                documentArray = subject_class.getJSONObject(j).getJSONArray("documents");
                                AssignmentArray = subject_class.getJSONObject(j).getJSONArray("assignments");
                            }
                            //fill assignments and classnotes to arraylist
                            if (documentArray.length() > 0 || AssignmentArray.length() > 0) {
                                for (int k = 0; k < documentArray.length(); k++) {
                                    JSONObject documentObj = documentArray.getJSONObject(k);
                                    String title = "", linkUrl = "";
                                    if (checkTextContains(documentObj.getString("title"))) {
                                        title = documentObj.getString("title").substring(0, documentObj.getString("title").indexOf("trueleaplinkurl"));
                                        linkUrl = documentObj.getString("title").substring(documentObj.getString("title").indexOf("trueleaplinkurl") + 15);
                                    } else {
                                        title = documentObj.getString("title");
                                        linkUrl = "";
                                    }
                                    documentsModelArrayList.add(new DocumentsModel(
                                            documentObj.getString("id"),
                                            documentObj.getString("filename"),
                                            documentObj.getString("type"),
                                            title,
                                            linkUrl,
                                            documentObj.getString("note"),
                                            documentObj.optString("validupto")
                                    ));
                                }
                                for (int k = 0; k < AssignmentArray.length(); k++) {
                                    JSONObject documentObj = AssignmentArray.getJSONObject(k);
                                    String title = "", linkUrl = "";
                                    if (checkTextContains(documentObj.getString("title"))) {
                                        title = documentObj.getString("title").substring(0, documentObj.getString("title").indexOf("trueleaplinkurl"));
                                        linkUrl = documentObj.getString("title").substring(documentObj.getString("title").indexOf("trueleaplinkurl") + 15);
                                    } else {
                                        title = documentObj.getString("title");
                                        linkUrl = "";
                                    }
                                    assignmentModelArrayList.add(new DocumentsModel(
                                            documentObj.getString("id"),
                                            documentObj.getString("filename"),
                                            documentObj.getString("type"),
                                            title,
                                            linkUrl,
                                            documentObj.getString("note"),
                                            documentObj.optString("validupto")
                                    ));
                                }
                            }
                            // end of
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
            }else{

            }
            for (int i = Subjects.size() - 1; i >= 0 ; i--) {
                classModelArrayList.add(Subjects.get(i));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        setupCalander();
    }

    private void setupCalander() {
        CalendarView calendarView;
        calendarView = findViewById(R.id.calendarView);
        calendarView.setMinimumDate(calendarMinDate);
        List<Calendar> calendars = new ArrayList<>();
        List<EventDay> events = new ArrayList<>();
        for (int i = Subjects.size() - 1; i >= 0; i--) {
            Calendar calendar = Calendar.getInstance();
            Log.d(TAG, "cscd " + Subjects.get(i).getStartdate());
            calendar.setTime(getdateValue(Subjects.get(i).getStartdate()));
            calendars.add(calendar);
            Log.d(TAG, "dteeeeee: " + calenderView(Subjects.get(i).getStartdate()));
            calendarModelArrayList.add(new CalendarModel(calenderView(Subjects.get(i).getStartdate()), Subjects.get(i).getUniqueperiodid()));
            EventDay eventDay = new EventDay(calendar, R.drawable.ic_baseline_menu_book_24);
            events.add(eventDay);
            Log.d(TAG, " Events: " + events);
        }
        calendarView.setEvents(events);
        calendarView.setHighlightedDays(calendars);
        calendarView.setSelectedDates(calendars);
        calendarView.setOnDayClickListener(new OnDayClickListener() {
            @Override
            public void onDayClick(EventDay eventDay) {
                Calendar clickedDayCalendar = eventDay.getCalendar();
                try {
                    String dateFormat = simpleDateFormat.format(eventDay.getCalendar().getTime());
                    Log.d(TAG,"date dvsvc"+ dateFormat);
                    for (int i = 0; i < calendarModelArrayList.size(); i++) {
                        if (calendarModelArrayList.get(i).getDate().equalsIgnoreCase(dateFormat)) {
                            Gson gson = new Gson();
                            String classData = gson.toJson(classModelArrayList);
                            localStorage.setClass(classData);
                            startActivity(new Intent(CalenderViewActivity.this, ClassMaterialActivity.class)
                                    .putExtra("subject_name", Subject_name)
                                    .putExtra("classDate", dateFormat)
                                    .putExtra("calendar_data", calendarModelArrayList));
                            break;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    boolean checkTextContains(String text) {
        try {
            return text.indexOf("trueleaplinkurl") != -1 ? true : false;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem menuItem = menu.findItem(R.id.action_notification);
        menuItem.setIcon(Converter.convertLayoutToImage(this, localStorage.getNotificationCount(), R.drawable.ic_baseline_notifications_24));
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_notification:
                startActivity(new Intent(this, NotificationActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSuccess(@NonNull ArrayList<NotificationModel> value) {
        if(value.size()>0) {
            invalidateOptionsMenu();
        }
    }
}