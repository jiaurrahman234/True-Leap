package com.app.trueleap.gradebook;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.app.trueleap.R;
import com.app.trueleap.base.BaseActivity;
import com.app.trueleap.databinding.ActivityGradebookBinding;
import com.app.trueleap.external.Converter;
import com.app.trueleap.gradebook.adapter.grade_book_adapter;
import com.app.trueleap.gradebook.model.GradeItem;
import com.app.trueleap.interfaces.recyclerviewClickListener;
import com.app.trueleap.interfaces.responseCallback;
import com.app.trueleap.notification.NotificationActivity;
import com.app.trueleap.notification.NotificationModel;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;

import static com.app.trueleap.external.CommonFunctions.loadAssetsJsonArray;

public class GradebookActivity extends BaseActivity implements recyclerviewClickListener, responseCallback, gradeResponseCallback {

    ActivityGradebookBinding binding;
    Intent intent;
    ArrayList<GradeItem> gradeItems;
    grade_book_adapter grade_book_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_gradebook);
        intent = getIntent();
        context = GradebookActivity.this;
        initToolbar();
        initdata();
        getNotifications(this);
        getGrades(intent.getStringExtra("subject_name"), this);
    }


   /* private void searchCall(String data) {
        gradeItems = new ArrayList<>();
        try {
            showProgressBar();
            Call<ResponseBody> call = APIClient
                    .getInstance()
                    .getApiInterface()
                    .globalsearch(data);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                    try {
                        hideProgressBar();
                        String response_data = response.body().string();
                        JSONArray jsonArray = new JSONArray(response_data);
                        int count=0 ;
                        count = jsonArray.getJSONObject(0).getInt("count");
                        binding.searchCount.setText(Integer.toString(count)+" records found");
                        if (count>0) {
                            binding.rvSearchresult.setVisibility(View.VISIBLE);
                            for (int i = 1; i < jsonArray.length(); i++) {
                                JSONObject resultObject = jsonArray.getJSONObject(i);
                                searchresult.add(new SresultItem(resultObject.getString("file"),
                                        resultObject.getString("content")));
                            }
                            populateSearchResult();
                        } else {
                            binding.rvSearchresult.setVisibility(View.GONE);
                            Toast.makeText(SearchActivity.this, "No data found", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        hideProgressBar();
                    }
                }


                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    hideProgressBar();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

    private void initdata() {
        try {
            binding.studentClass.setText(localStorage.getClassId());
            binding.studentSection.setText(localStorage.getSectionId());
            binding.sujectName.setText(intent.getStringExtra("subject_name"));
            gradeItems = new ArrayList<>();
            try {
                JSONArray jsonArray = loadAssetsJsonArray("gradebook.json", context);
                if (jsonArray.length() > 0) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject resultObject = jsonArray.getJSONObject(i);
                        gradeItems.add(
                                new GradeItem(resultObject.getString("gradetype"),
                                        resultObject.getDouble("gradeweight"),
                                        resultObject.getString("gradename"),
                                        resultObject.getBoolean("compulsary"),
                                        resultObject.getDouble("compulsarypassmark"),
                                        resultObject.getString("assessmentdate"),
                                        resultObject.getDouble("outof"),
                                        resultObject.getDouble("bestoutof"),
                                        resultObject.getBoolean("partofmidtermgrade"))
                        );
                    }
                    populateGradbook();
                } else {
                    binding.gradebookData.setVisibility(View.GONE);
                    Toast.makeText(context, "No data found", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void populateGradbook() {
        grade_book_adapter = new grade_book_adapter(context, gradeItems, this);
        LinearLayoutManager llayoutmanager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        binding.gradebookData.setLayoutManager(llayoutmanager);
        binding.gradebookData.setAdapter(grade_book_adapter);
        grade_book_adapter.notifyDataSetChanged();

    }

    @Override
    public void onClicked(int position) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
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

    @Override
    public void onSuccesGrade(@NonNull ArrayList<GradeItem> value) {
        /*if(value.size()>0){
            gradeItems = value;
            populateGradbook();
        }*/
    }
}