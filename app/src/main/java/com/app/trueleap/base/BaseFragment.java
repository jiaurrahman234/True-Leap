package com.app.trueleap.base;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.app.trueleap.R;
import com.app.trueleap.external.LocalStorage;
import com.google.gson.Gson;

public class BaseFragment extends Fragment {
    String TAG = BaseFragment.class.getSimpleName();
    public static View view;
    public static LocalStorage localStorage;
    public static ProgressDialog progressDialog;
    public static FragmentManager fragmentManager;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        localStorage = new LocalStorage(getContext());
        progressDialog = new ProgressDialog(getContext());
        fragmentManager = getActivity().getSupportFragmentManager();
    }

    public void vibrate(int duration) {
        Vibrator vibs = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
        vibs.vibrate(duration);
    }

    public void showProgressView() {
        ((BaseActivity)getActivity()).showProgressBar();
    }

    public void hideProgressView() {
        ((BaseActivity)getActivity()).hideProgressBar();
    }

}
