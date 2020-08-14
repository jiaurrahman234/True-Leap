package com.app.trueleap.auth;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.app.trueleap.MainActivity;
import com.app.trueleap.R;
import com.app.trueleap.base.BaseActivity;
import com.app.trueleap.databinding.ActivityLoginBinding;
import com.app.trueleap.external.CommonFunctions;
import com.app.trueleap.external.Constants;
import com.app.trueleap.external.DatabaseHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.RequestBody;

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
    AuthViewModel viewModel;
    JSONObject updateWrapperObj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        viewModel = ViewModelProviders.of(this).get(AuthViewModel.class);
        updateWrapperObj = new JSONObject();
        initObserver();
        initLister();
    }

    private void initLister() {
        binding.loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CommonFunctions.isInternetOn(context)){
                    try{
                        updateWrapperObj.put("email", binding.loginEmail.getText().toString());
                        updateWrapperObj.put("password", binding.loginPassword.getText().toString());
                        Log.d(TAG, "update address: " + updateWrapperObj.toString());

                        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),
                                (new JSONObject(updateWrapperObj.toString())).toString());
                        viewModel.loginUser(body);
                        showProgressBar();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }else{
                    Toast.makeText(context, Constants.NO_INTERNET, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initObserver() {
        viewModel.isApiSuccess.observe(this, it -> {
            hideProgressBar();

        });
        viewModel.userData.observe(this,it->{
            Log.d(TAG,"Successfully login");
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
        });
        viewModel.errorData.observe(this,it->{
            /*CommonFunctions.showSnackView(binding.rootlayout, it.errorDesc_);
            supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.frameContainer, new MobileFragment().newInstance(binding.phone.getText().toString()),
                            Utils.Mobile_Fragment).commit();*/
        });
    }
}
