package com.app.trueleap.settings;

import android.os.Bundle;
import android.util.Log;
import android.widget.CompoundButton;

import androidx.databinding.DataBindingUtil;

import com.app.trueleap.R;
import com.app.trueleap.base.BaseActivity;
import com.app.trueleap.databinding.ActivityLoginBinding;
import com.app.trueleap.databinding.ActivitySettingsBinding;

public class SettingsActivity extends BaseActivity {
    String TAG = SettingsActivity.class.getSimpleName();
    ActivitySettingsBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_settings);
        initData();
        initListner();
    }

    private void initListner() {
        try{
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
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void initData() {
        try{
            if (localStorage.getAutodownload()){
                binding.autoDownload.setChecked(true);
            }else{
                binding.autoDownload.setChecked(false);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
