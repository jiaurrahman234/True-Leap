package com.app.trueleap.auth;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.app.trueleap.Retrofit.APIClient;
import com.app.trueleap.databinding.ActivityLoginBinding;
import com.app.trueleap.home.MainActivity;
import com.app.trueleap.R;
import com.app.trueleap.base.BaseActivity;
import com.app.trueleap.external.CommonFunctions;
import com.app.trueleap.external.Constants;
import com.app.trueleap.external.DatabaseHelper;
import com.app.trueleap.home.studentsubject.model.ClassModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

import static com.app.trueleap.external.CommonFunctions.saveJSONToCache;

public class LoginActivity extends BaseActivity {

    String TAG = LoginActivity.class.getSimpleName();
    private static EditText phone, password;
    private static Button loginButton;
    private static TextView forgotPassword, signUp;
    private static CheckBox show_hide_password;
    private static LinearLayout loginLayout;
    DatabaseHelper databaseHelper;
    String fcm_token;
    ActivityLoginBinding binding;
    JSONObject updateWrapperObj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        updateWrapperObj = new JSONObject();
        //initObserver();
        initLister();
    }

    private void initLister() {
        binding.loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CommonFunctions.isInternetOn(context)) {
                    try {
                        if (!TextUtils.isEmpty(binding.loginEmail.getText().toString())) {
                            binding.loginEmail.setError(null);
                            if (Patterns.EMAIL_ADDRESS.matcher(binding.loginEmail.getText().toString()).matches()) {
                                binding.loginEmail.setError(null);
                                if (!TextUtils.isEmpty(binding.loginPassword.getText().toString())) {
                                    binding.loginPassword.setError(null);
                                    updateWrapperObj.put("email", binding.loginEmail.getText().toString());
                                    updateWrapperObj.put("password", binding.loginPassword.getText().toString());
                                    Log.d(TAG, "login detail: " + updateWrapperObj.toString());
                                    RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),
                                            (new JSONObject(updateWrapperObj.toString())).toString());
                                    //viewModel.loginUser(body);
                                    callApiData(body);

                                } else {
                                    binding.loginPassword.setError(getString(R.string.required_field));
                                }

                            } else {
                                binding.loginEmail.setError(getString(R.string.email_error));
                            }
                        } else {
                            binding.loginEmail.setError(getString(R.string.required_field));
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(context, Constants.NO_INTERNET, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void callApiData(RequestBody body) {
        try {
            showProgressBar();
            Call<ResponseBody> call = APIClient
                    .getInstance()
                    .getApiInterface()
                    .loginUser(body);

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                    try {
                        hideProgressBar();
                        if (response.code()==200){
                            String response_data = response.body().string();
                            JSONObject jsonObject = new JSONObject(response_data);

                            localStorage.createUserLoginSession(jsonObject.getString("token"), jsonObject.getString("id"),
                                    jsonObject.getJSONObject("profile").getString("rollNumber"), jsonObject.getJSONObject("profile").getString("phoneNumber"),
                                    jsonObject.getJSONObject("profile").getJSONArray("class").getJSONObject(0).getString("classid"),
                                    jsonObject.getJSONObject("profile").getJSONArray("class").getJSONObject(0).getString("section"), true);
                            localStorage.setAutodownload(true);
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finish();
                        }else{
                            String errorBody = response.errorBody().string();
                            Log.d(TAG, "error data: " + errorBody);
                            JSONObject jsonObject = new JSONObject(errorBody);
                            CommonFunctions.showSnackView(binding.rootlayout,jsonObject.getString("desc") );
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
            hideProgressBar();
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        exitApp();
    }


}
