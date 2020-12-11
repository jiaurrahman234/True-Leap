package com.app.trueleap.documentSearch;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.app.trueleap.R;
import com.app.trueleap.Retrofit.APIClient;
import com.app.trueleap.base.BaseActivity;
import com.app.trueleap.databinding.ActivitySearchBinding;
import com.app.trueleap.documentSearch.adapter.search_result_adapter;
import com.app.trueleap.documentSearch.model.SresultItem;
import com.app.trueleap.external.CommonFunctions;
import com.app.trueleap.external.Constants;
import com.app.trueleap.external.Converter;
import com.app.trueleap.interfaces.recyclerviewClickListener;
import com.app.trueleap.interfaces.responseCallback;
import com.app.trueleap.model.ErrorResponse;
import com.app.trueleap.notification.NotificationActivity;
import com.app.trueleap.notification.NotificationModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.annotation.Annotation;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends BaseActivity implements recyclerviewClickListener , responseCallback {

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
        initdata();
        initToolbar();
        initListner();
        getNotifications(this);
    }

    private void initdata() {
        binding.studentClass.setText(localStorage.getClassId());
        binding.studentSection.setText(localStorage.getSectionId());
    }

    private void initListner() {
        binding.searchTerm.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (binding.searchTerm.getText().toString().trim().length()>2){
                        searchCall(binding.searchTerm.getText().toString());
                    }
                    else{
                        CommonFunctions.showSnackView(binding.getRoot(), "Minimum search text length is 3");
                    }
                    return true;
                }
                return false;
            }
        });
    }

    private void searchCall(String data) {
        searchresult = new ArrayList<>();
        try {
            showProgressBar();
            Call<ResponseBody> call = APIClient
                    .getInstance(localStorage.getSelectedCountry())
                    .getApiInterface()
                    .globalsearch(localStorage.getKeyUserToken(), data);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                    if(response.isSuccessful()) {
                    try {
                        hideProgressBar();
                        String response_data = response.body().string();
                        JSONArray jsonArray = new JSONArray(response_data);
                        int count = 0;
                        count = jsonArray.getJSONObject(0).getInt("count");
                        binding.searchCount.setText(Integer.toString(count) + " records found");
                        if (count > 0) {
                            binding.rvSearchresult.setVisibility(View.VISIBLE);
                            for (int i = 1; i < jsonArray.length(); i++) {
                                JSONObject resultObject = jsonArray.getJSONObject(i);
                                searchresult.add(new SresultItem(resultObject.getString("file"),
                                        resultObject.getString("content")));
                            }
                            populateSearchResult();
                        } else {
                            binding.rvSearchresult.setVisibility(View.GONE);
                            CommonFunctions.showSnackView(binding.getRoot(), "No data found");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        hideProgressBar();
                        CommonFunctions.showSnackView(binding.getRoot(), "No data found");
                      }
                    }else {
                        String errorBody = response.errorBody().toString();
                        Log.d(TAG, "error data: " + errorBody);
                        Response<?> errorResponse = response;
                        ResponseBody errorbody = errorResponse.errorBody();
                        retrofit2.Converter<ResponseBody, ErrorResponse> converter = APIClient.getRetrofit().responseBodyConverter(ErrorResponse.class, new Annotation[0]);
                        try {
                            ErrorResponse errorObject = converter.convert(errorbody);
                            Log.d(TAG, " Error_code : " + errorObject.getError_code());
                            if (errorObject.getError_code().equals(Constants.ERR_INVALID_TOKEN) || errorObject.getError_code().equals(Constants.ERR_SESSION_TIMEOUT)) {
                                showSesstionTimeout();
                            } else {
                                CommonFunctions.showSnackView(binding.getRoot(), errorObject.getError_message());
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            // CommonFunctions.showSnackView(rootlayout, errorObject.getError_message());
                        }
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
}
