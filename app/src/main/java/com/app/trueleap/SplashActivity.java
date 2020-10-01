package com.app.trueleap;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import com.app.trueleap.auth.LoginActivity;
import com.app.trueleap.base.BaseActivity;
import com.app.trueleap.documentSearch.SearchActivity;
import com.app.trueleap.home.MainActivity;


public class SplashActivity extends BaseActivity {
    String TAG = SplashActivity.class.getSimpleName();
    int app_version;
    TextView versionName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
            setContentView(R.layout.activity_splash);
            PackageInfo pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
            app_version = pInfo.versionCode;
            versionName = findViewById(R.id.versionName);
            versionName.setText(pInfo.versionName);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mainMethod();
                }
            }, 3000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void mainMethod() {
        try {
            if (localStorage.isUserLoggedIn()) {
                startActivity(new Intent(getApplicationContext(), SearchActivity.class));
                finish();
            } else {
                startActivity(new Intent(getApplicationContext(), SearchActivity.class));
                finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
