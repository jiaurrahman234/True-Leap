package com.app.trueleap.auth;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import com.app.trueleap.R;
import com.app.trueleap.base.BaseActivity;
import com.app.trueleap.databinding.ActivityOfflineLoginBinding;

public class OfflineLoginActivity extends BaseActivity {

    ActivityOfflineLoginBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_offline_login);
    }
}