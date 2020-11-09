package com.app.trueleap.auth;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import com.app.trueleap.R;
import com.app.trueleap.base.BaseActivity;
import com.app.trueleap.databinding.ActivitySetupOfflineLoginBinding;

public class SetupOfflineLoginActivity extends BaseActivity {

    ActivitySetupOfflineLoginBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_setup_offline_login);
    }

}