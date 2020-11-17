package com.app.trueleap.auth;
import androidx.databinding.DataBindingUtil;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.app.trueleap.R;
import com.app.trueleap.base.BaseActivity;
import com.app.trueleap.databinding.ActivityOfflineLoginBinding;
import com.app.trueleap.home.MainActivity;
import com.google.android.material.snackbar.Snackbar;
import java.util.ArrayList;

import static com.app.trueleap.external.CommonFunctions.setErrorInputLayout;

public class OfflineLoginActivity extends BaseActivity {

    ActivityOfflineLoginBinding binding;
    Context context;
    Boolean validate =  false;
    Snackbar snackbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = OfflineLoginActivity.this;
        binding = DataBindingUtil.setContentView(this, R.layout.activity_offline_login);
        initData();
        initListener();
    }

    private void initListener() {
        binding.confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String answer = binding.answer.getText().toString().trim();
                if(!answer.isEmpty()){
                    if(answer.equals(localStorage.getSecretAnswer())){
                        localStorage.setIsOfflineLoggedin(true);
                        startActivity(new Intent(context, MainActivity.class));
                        snackbar = Snackbar
                                .make(binding.rootlayout,"Successfully Logged In", Snackbar.LENGTH_LONG);
                        snackbar.show();
                        finish();
                    }{
                        snackbar = Snackbar
                                .make(binding.rootlayout, "Answer doesnot matched", Snackbar.LENGTH_LONG);
                        snackbar.show();
                    }

                } else {
                    setErrorInputLayout(binding.answer, getString(R.string.err_empty_answer));
                }
            }
        });
    }

    private void initData() {
        binding.question.setText(localStorage.getSecretQuestion());
    }
}