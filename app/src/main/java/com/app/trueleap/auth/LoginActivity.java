package com.app.trueleap.auth;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import androidx.databinding.DataBindingUtil;
import com.app.trueleap.R;
import com.app.trueleap.Retrofit.APIClient;
import com.app.trueleap.base.BaseActivity;
import com.app.trueleap.databinding.ActivityLoginBinding;
import com.app.trueleap.external.CommonFunctions;
import com.app.trueleap.external.Constants;
import com.app.trueleap.home.MainActivity;
import com.app.trueleap.model.ErrorResponse;

import org.json.JSONObject;

import java.lang.annotation.Annotation;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Converter;
import retrofit2.Response;

public class LoginActivity extends BaseActivity {
    String TAG = LoginActivity.class.getSimpleName();
    ActivityLoginBinding binding;
    JSONObject updateWrapperObj;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        updateWrapperObj = new JSONObject();
        initListner();
    }
    private void initListner() {

        binding.loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CommonFunctions.isInternetOn(context)) {
                   validate();
                } else {
                    CommonFunctions.showSnackView( binding.rootlayout, Constants.NO_INTERNET);
                }
            }
        });

    }

    private void validate() {
        try {
            if (!TextUtils.isEmpty(binding.loginEmail.getText().toString())) {
                binding.loginEmail.setError(null);
                if (Patterns.EMAIL_ADDRESS.matcher(binding.loginEmail.getText().toString()).matches()) {
                    binding.loginEmail.setError(null);
                    if (!TextUtils.isEmpty(binding.loginPassword.getText().toString())) {
                        binding.loginPassword.setError(null);
                        updateWrapperObj.put("email", binding.loginEmail.getText().toString());
                        updateWrapperObj.put("password", binding.loginPassword.getText().toString());
                        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),
                                (new JSONObject(updateWrapperObj.toString())).toString());
                        getlogin(body);
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
            CommonFunctions.showSnackView( binding.rootlayout , Constants.NO_INTERNET);
        }
    }

    private void getlogin(RequestBody body) {
        try {
            showProgressBar();
            Call<ResponseBody> call = APIClient
                    .getInstance(localStorage.getSelectedCountry())
                    .getApiInterface()
                    .loginUser(body);

            Log.d(TAG,"log :" + localStorage.getSelectedCountry());
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                    try {
                        hideProgressBar();
                        if (response.isSuccessful()&&response.code()==200){
                            String response_data = response.body().string();
                            JSONObject jsonObject = new JSONObject(response_data);
                            localStorage.createUserLoginSession(
                                    jsonObject.getString("token"), jsonObject.getString("id"),
                                    jsonObject.getJSONObject("profile").getString("rollNumber"),
                                    jsonObject.getJSONObject("profile").getString("phoneNumber"),
                                    jsonObject.getJSONObject("profile").getString("fullname"),
                                    jsonObject.getJSONObject("profile").getJSONArray("class").getJSONObject(0).getString("classid"),
                                    jsonObject.getJSONObject("profile").getJSONArray("class").getJSONObject(0).getString("section"),
                                    jsonObject.getJSONObject("profile").getJSONArray("class").getJSONObject(0).getString("semester"), true);
                            localStorage.setAutodownload(true);
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finish();
                        }else{
                            String errorBody = response.errorBody().toString();
                            Log.d(TAG, "error data: " + errorBody);
                            Response<?> errorResponse = response;
                            ResponseBody errorbody = errorResponse.errorBody();
                            Converter<ResponseBody, ErrorResponse> converter = APIClient.getRetrofit().responseBodyConverter(ErrorResponse.class, new Annotation[0]);
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
                            }
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