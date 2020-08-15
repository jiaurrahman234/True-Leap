package com.app.trueleap.home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.app.trueleap.R;
import com.app.trueleap.base.BaseActivity;
import com.app.trueleap.databinding.ActivityMainBinding;
import com.app.trueleap.external.Utils;
import com.app.trueleap.home.studentsubject.HomeSubjectsFragment;

public class MainActivity extends BaseActivity {

    ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        loadFragment(new HomeSubjectsFragment());
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(binding.frameContainer.getId(), fragment, Utils.Home_Subject_Fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

}