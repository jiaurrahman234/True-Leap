package com.app.trueleap.documentSearch;

import android.content.Intent;
import android.os.Bundle;
import androidx.databinding.DataBindingUtil;
import com.app.trueleap.R;
import com.app.trueleap.base.BaseActivity;
import com.app.trueleap.databinding.ActivitySearchDetailBinding;

public class SearchDetailActivity extends BaseActivity {

    ActivitySearchDetailBinding binding;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_search_detail);
        intent = getIntent();
        context = SearchDetailActivity.this;
        initdata();
        initToolbar();
        initData();
    }

    private void initdata() {
        binding.studentClass.setText(localStorage.getClassId());
        binding.studentSection.setText(localStorage.getSectionId());
    }

    private void initData() {
        if(intent.getExtras()!=null){
            if(!(intent.getStringExtra("filename").equals(null)))
                binding.fileName.setText(intent.getStringExtra("filename"));
            if(!(intent.getStringExtra("content").equals(null)))
                binding.content.setText(intent.getStringExtra("content"));
        }
    }

}