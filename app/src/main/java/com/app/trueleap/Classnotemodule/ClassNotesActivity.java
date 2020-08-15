package com.app.trueleap.Classnotemodule;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.app.trueleap.R;
import com.app.trueleap.base.BaseActivity;
import com.app.trueleap.databinding.ActivityClassNotesBindingImpl;
import com.app.trueleap.external.Utils;
import com.app.trueleap.home.studentsubject.HomeSubjectsFragment;

public class ClassNotesActivity extends BaseActivity {

    ActivityClassNotesBindingImpl binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_notes);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        loadFragment(new ClassnotesFragmentListing());
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(binding.frameContainer.getId(), fragment, Utils.Classnotes_fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

}