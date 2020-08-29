package com.app.trueleap.classcalenderview;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.RectF;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.alamkanak.weekview.DateTimeInterpreter;
import com.alamkanak.weekview.MonthLoader;
import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;
import com.app.trueleap.R;
import com.app.trueleap.auth.LoginActivity;
import com.app.trueleap.base.BaseActivity;
import com.app.trueleap.databinding.FragmentSubjectsBinding;
import com.app.trueleap.external.LocalStorage;
import com.app.trueleap.home.studentsubject.ClassModel;
import com.app.trueleap.home.studentsubject.adapter.subject_adapter;
import com.app.trueleap.home.studentsubject.viewModel.SubjectViewModel;

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
import static com.app.trueleap.external.CommonFunctions.parse_date;

public  class CallenderViewActivity extends BaseActivity implements WeekView.EventClickListener, MonthLoader.MonthChangeListener, WeekView.EventLongPressListener, WeekView.EmptyViewLongPressListener {

    private WeekView mWeekView;
    Intent intent ;
    LocalStorage localStorage;
    Context context;
    ArrayList<ClassModel> Subjects;
    String selecteduniqueperiodid ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        // Get a reference for the week view in the layout.
        mWeekView = (WeekView) findViewById(R.id.weekView);

        // Show a toast message about the touched event.
        mWeekView.setOnEventClickListener(this);

        // The week view has infinite scrolling horizontally. We have to provide the events of a
        // month every time the month changes on the week view.
        mWeekView.setMonthChangeListener(this);

        // Set long press listener for events.
        mWeekView.setEventLongPressListener(this);

        // Set long press listener for empty view
        mWeekView.setEmptyViewLongPressListener(this);

        // Set up a date time interpreter to interpret how the date and time will be formatted in
        // the week view. This is optional.
        /*  setupDateTimeInterpreter(false);*/

        mWeekView.setNumberOfVisibleDays(4);
        mWeekView.setColumnGap((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, getResources().getDisplayMetrics()));
        mWeekView.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10, getResources().getDisplayMetrics()));
        mWeekView.setEventTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10, getResources().getDisplayMetrics()));
        initData();
    }

    private void initData() {
        Subjects = new ArrayList<>();
        intent = getIntent();
        if(intent.getExtras()!=null){
            selecteduniqueperiodid = intent.getStringExtra("uniqueperiodid");
        }
        JSONArray classes = getJSONFromCache(this);
        try {
            if (classes != null) {
                int position_to_show=0;
                for (int i = 0; i < classes.length(); i++) {
                    JSONArray level_1 = classes.getJSONArray(i);
                    JSONObject ClassObject = level_1.getJSONObject(0);
                    if(selecteduniqueperiodid.equals(ClassObject.getString("uniqueperiodid"))){
                        position_to_show = i;
                        break;
                    }
                }
                JSONArray subject_class = classes.getJSONArray(position_to_show);

                for (int j = 0; j<subject_class.length();j++){
                    Date curDate = new Date();
                    String startDate = subject_class.getJSONObject(j).getString("startdate");
                    Date classDate = getdateValue(startDate);
                    if(classDate.compareTo(curDate) < 0) {
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
                                        null));
                    }else{
                        break;
                    }

                }
            }
        }
        catch (JSONException e){
            e.printStackTrace();
        }

                    String[]  time = (Subjects.get(0).getStarttime().split("[:.]"));
                    mWeekView.goToHour(Integer.parseInt(time[0]));
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(getdateValue(Subjects.get(0).getStartdate()));
                    mWeekView.goToDate(calendar);
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


    @Override
    public List<? extends WeekViewEvent> onMonthChange(int newYear, int newMonth) {
        // Populate the week view with some events.
        List<WeekViewEvent> events = new ArrayList<WeekViewEvent>();

        Log.d("fvbdvd","dscsc "+Subjects.size());
        int count =0;
        for (int i = Subjects.size()-1; i >=0 &&count<8 ; i--) {
            Log.d("fdvvvbdvd","dscsc "+Subjects.get(i).getStartdate());
            Calendar cal = Calendar.getInstance();
            cal.setTime(getdateValue(Subjects.get(i).getStartdate()));
            int month = cal.get(Calendar.MONTH);
            int year = cal.get(Calendar.YEAR);
            int day = cal.get(Calendar.DATE);

            String start_time = Subjects.get(i).getStarttime();
            String[] starttime_value_separated = start_time.split("[://.]");
            String end_time = Subjects.get(i).getEndtime();
            String[] end_time_value_separated = end_time.split("[://.]");


            int rdsv = Integer.parseInt(end_time_value_separated[0]);
            int sdc= Integer.parseInt(end_time_value_separated[1]);

            Log.d(TAG,"csacc"+ rdsv );
            Log.d(TAG,"cszcczcsc"+ sdc);

            Calendar startTime = Calendar.getInstance();
            startTime.set(Calendar.DAY_OF_MONTH, day);
            startTime.set(Calendar.HOUR_OF_DAY, Integer.parseInt(starttime_value_separated[0]));
            startTime.set(Calendar.MINUTE, Integer.parseInt(starttime_value_separated[1]));
            startTime.set(Calendar.MONTH, month);
            startTime.set(Calendar.YEAR, year);

            Calendar endTime = (Calendar) startTime.clone();
            endTime.set(Calendar.HOUR_OF_DAY, Integer.parseInt(end_time_value_separated[0]));
            endTime.set(Calendar.MINUTE, Integer.parseInt(end_time_value_separated[1]));
            endTime.set(Calendar.MONTH, month);
            startTime.set(Calendar.YEAR, year);

           if((month==newMonth-1 )&& year ==newYear) {
               WeekViewEvent event = new WeekViewEvent(i, getClassTitle(Subjects.get(i).getSubject(), Subjects.get(i).getStarttime(), Subjects.get(i).getEndtime()), startTime, endTime);
               event.setColor(getResources().getColor(R.color.event_color_01));
               events.add(event);
           }
               count++;
        }

        return events;
    }

    private void setupDateTimeInterpreter(final boolean shortDate) {
        mWeekView.setDateTimeInterpreter(new DateTimeInterpreter() {
            @Override
            public String interpretDate(Calendar date) {
                SimpleDateFormat weekdayNameFormat = new SimpleDateFormat("EEE", Locale.getDefault());
                String weekday = weekdayNameFormat.format(date.getTime());
                SimpleDateFormat format = new SimpleDateFormat(" M/d", Locale.getDefault());
                if (shortDate)
                    weekday = String.valueOf(weekday.charAt(0));
                return weekday.toUpperCase() + format.format(date.getTime());
            }

            @Override
            public String interpretTime(int hour) {
                return hour > 11 ? (hour - 12) + " PM" : (hour == 0 ? "12 AM" : hour + " AM");
            }
        });
    }

    protected String getClassTitle(String name, String st, String et) {
        return name + "("+st+" - "+et+")";
    }

    @Override
    public void onEventClick(WeekViewEvent event, RectF eventRect) {
        Toast.makeText(this, "Clicked " + event.getName(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onEventLongPress(WeekViewEvent event, RectF eventRect) {
        Toast.makeText(this, "Long pressed event: " + event.getName(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onEmptyViewLongPress(Calendar time) {
        Toast.makeText(this, "Empty view long pressed: " , Toast.LENGTH_SHORT).show();
    }

    public WeekView getWeekView() {
        return mWeekView;
    }
}
