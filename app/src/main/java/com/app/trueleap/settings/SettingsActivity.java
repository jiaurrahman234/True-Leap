package com.app.trueleap.settings;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import androidx.databinding.DataBindingUtil;

import com.app.trueleap.R;
import com.app.trueleap.base.BaseActivity;
import com.app.trueleap.databinding.ActivitySettingsBinding;
import com.app.trueleap.home.MainActivity;
import com.app.trueleap.localization.ChangeLanguageActivity;

public class SettingsActivity extends BaseActivity {
    String TAG = SettingsActivity.class.getSimpleName();
    ActivitySettingsBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_settings);
        initToolbar();
        initData();
        initListner();
    }

    private void initListner() {

        binding.changeLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingsActivity.this, ChangeLanguageActivity.class));
                finish();
            }
        });
            binding.autoDownload.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    try{
                        Log.d(TAG,"ljf: "+isChecked);
                        if (isChecked){
                            localStorage.setAutodownload(true);
                            binding.autoDownload.setChecked(true);
                        }else{
                            localStorage.setAutodownload(false);
                            binding.autoDownload.setChecked(false);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });

        binding.autoUpload.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    Log.d(TAG,"ljf: "+isChecked);
                    if (isChecked){
                        localStorage.setAutoupload(true);
                        binding.autoUpload.setChecked(true);
                    }else{
                        localStorage.setAutoupload(false);
                        binding.autoUpload.setChecked(false);
                    }
            }
        });
    }

    private void initData() {
        if (localStorage.getAutodownload()){
                binding.autoDownload.setChecked(true);
        }else{
                binding.autoDownload.setChecked(false);
        }
        if (localStorage.getAutoupload()){
            binding.autoUpload.setChecked(true);
        }else{
            binding.autoUpload.setChecked(false);
        }
    }


}
