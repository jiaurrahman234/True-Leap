package com.app.trueleap.auth;

import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.trueleap.R;
import com.app.trueleap.base.BaseActivity;
import com.app.trueleap.external.DatabaseHelper;

public class LoginActivity extends BaseActivity {

    String TAG = LoginActivity.class.getSimpleName();
    private static EditText phone, password;
    private static Button loginButton;
    private static TextView forgotPassword, signUp;
    private static CheckBox show_hide_password;
    private static LinearLayout loginLayout;
    DatabaseHelper databaseHelper;
    String fcm_token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
