package com.app.trueleap.documentSearch;
import android.content.Intent;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.app.trueleap.Assignmentmodule.AssignmentActivity;
import com.app.trueleap.R;
import com.app.trueleap.Retrofit.APIClient;
import com.app.trueleap.Retrofit.ApiClientFile;
import com.app.trueleap.base.BaseActivity;
import com.app.trueleap.databinding.ActivitySearchBinding;
import com.app.trueleap.documentSearch.adapter.search_result_adapter;
import com.app.trueleap.documentSearch.model.SresultItem;
import com.app.trueleap.home.studentsubject.adapter.subject_adapter;
import com.app.trueleap.home.studentsubject.model.ClassModel;
import com.app.trueleap.interfaces.recyclerviewClickListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

public class SearchActivity extends BaseActivity implements recyclerviewClickListener {

    ActivitySearchBinding binding;
    Intent intent;
    ArrayList<SresultItem> searchresult;
    search_result_adapter searchresultAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_search);
        intent = getIntent();
        context = SearchActivity.this;
        initToolbar();
        initData();
    }

    private void initToolbar() {
        TextView toolbar_tv;
        Toolbar toolbar;
        toolbar_tv = (TextView) findViewById(R.id.toolbar_tv);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void initData() {
        searchresult = new ArrayList<>();
        try {
            showProgressBar();
            Call<ResponseBody> call = APIClient
                    .getInstance()
                    .getApiInterface()
                    .globalsearch("test");
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                    try {
                        Log.d(TAG,"hkghkf: "+call.request());
                        hideProgressBar();
                        String response_data = response.body().string();
                        JSONArray jsonArray = new JSONArray(response_data);
                        Log.d(TAG, "subject response: " + jsonArray.length());

                        String count ;

                        if (jsonArray.length()>1){
                            JSONObject resultObject = jsonArray.getJSONObject(0);
                            count = resultObject.getString("count");
                            for (int i = 1; i < jsonArray.length(); i++) {
                                resultObject = jsonArray.getJSONObject(i);
                                    searchresult.add(new SresultItem(resultObject.getString("file"),
                                    resultObject.getString("content")));
                            }
                            populateSearchResult();
                        }
                        else {
                           count = jsonArray.getJSONObject(0).getString("count");
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
    }

    private void populateSearchResult() {
        searchresultAdapter = new search_result_adapter(context, searchresult, this);
        LinearLayoutManager llayoutmanager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        binding.rvSearchresult.setLayoutManager(llayoutmanager);
        binding.rvSearchresult.setAdapter(searchresultAdapter);
        searchresultAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClicked(int position) {
        startActivity(new Intent(SearchActivity.this , SearchDetailActivity.class)
                .putExtra("filename",searchresult.get(position).getFilename())
                .putExtra("content",searchresult.get(position).getContent()));
    }
}
