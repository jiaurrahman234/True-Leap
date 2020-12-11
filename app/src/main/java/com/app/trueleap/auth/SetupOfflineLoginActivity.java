package com.app.trueleap.auth;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import androidx.databinding.DataBindingUtil;
import com.app.trueleap.R;
import com.app.trueleap.base.BaseActivity;
import com.app.trueleap.databinding.ActivitySetupOfflineLoginBinding;
import com.google.android.material.snackbar.Snackbar;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;

import static com.app.trueleap.external.CommonFunctions.loadAssetsJsonArray;
import static com.app.trueleap.external.CommonFunctions.setErrorInputLayout;

public class SetupOfflineLoginActivity extends BaseActivity {
    ArrayList<String> questions;
    Context context;
    ActivitySetupOfflineLoginBinding binding;
    Boolean validate =  false;
    Snackbar snackbar;
    int selected = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = SetupOfflineLoginActivity.this;
        binding = DataBindingUtil.setContentView(this, R.layout.activity_setup_offline_login);
        initdata();
        setUI();
        initListener();
    }

    private void setUI(){
        ArrayAdapter adapter = new ArrayAdapter<String>(context,R.layout.spinner_item_custom_simple,questions);
        binding.selectquestions.setAdapter(adapter);
        binding.selectquestions.setSelection(0);
    }

    private void initdata() {
        questions = new ArrayList<>();
        try {
            JSONArray jsonArray = loadAssetsJsonArray("secret_question.json", context );
            if(jsonArray.length()>0){
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject resultObject = jsonArray.getJSONObject(i);
                    questions.add(resultObject.getString("question"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initListener() {
        binding.selectquestions.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG,"question "+questions.get(position));
                selected = position;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        binding.setSecretQuestion.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                String answer =  binding.answer.getText().toString().trim();
                String conf_answer =  binding.confAnswer.getText().toString().trim();
                validate(answer,conf_answer);
            }

            private void validate(String answer,String conf_answer ) {
                if(answer.isEmpty()){
                    setErrorInputLayout(binding.answer, getString(R.string.err_empty_answer));
                    validate = false;
                    return;
                }
                if( conf_answer.isEmpty()) {
                    setErrorInputLayout(binding.confAnswer, getString(R.string.err_empty_c_answer));
                    validate = false;
                    return;
                }
                if( !(answer.equals(conf_answer))){
                    setErrorInputLayout(binding.confAnswer, getString(R.string.err_invalid_answer_confirm));
                    validate = false;
                    return;
                }
                else {
                    validate = true;
                }
                if (validate) {
                    localStorage.setSecretQuestion(questions.get(selected));
                    localStorage.setSecretAnswer(answer);
                    localStorage.setQuestionSet(true);
                    startActivity(new Intent(context, OfflineLoginActivity.class));
                    finish();
                } else {
                    snackbar = Snackbar
                            .make(binding.rootlayout, "Please Enter Valid Details", Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            }
        });
    }
}