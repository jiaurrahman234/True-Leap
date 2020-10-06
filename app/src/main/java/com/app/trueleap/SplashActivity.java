package com.app.trueleap;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.databinding.DataBindingUtil;

import com.app.trueleap.auth.LoginActivity;
import com.app.trueleap.base.BaseActivity;
import com.app.trueleap.databinding.ActivitySplashBinding;
import com.app.trueleap.documentSearch.SearchActivity;
import com.app.trueleap.gradebook.GradebookActivity;
import com.app.trueleap.home.MainActivity;
import com.app.trueleap.localization.ChangeLanguageActivity;

import org.w3c.dom.Text;


public class SplashActivity extends BaseActivity {
    String TAG = SplashActivity.class.getSimpleName();
    ActivitySplashBinding binding;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        context = SplashActivity.this;
        try {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
            binding = DataBindingUtil.setContentView(this,R.layout.activity_splash);
            PackageInfo pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
            binding.versionName.setText(pInfo.versionName);
            initListener();
            if(localStorage.getSelectedLanguage()!=null) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mainMethod();
                    }
                }, 3000);
            }else {
                binding.languageBox.setVisibility(View.VISIBLE);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initListener() {
        Log.d(TAG,"sdcsdc" );
        binding.languageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, ChangeLanguageActivity.class));
                finish();
            }
        });
    }

    private void mainMethod() {
        try {
                if (localStorage.isUserLoggedIn()) {
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();
                } else {
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                    finish();
                }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
