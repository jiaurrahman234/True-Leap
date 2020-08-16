package com.app.trueleap.Assignmentmodule;

import android.content.Context;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.trueleap.R;
import com.app.trueleap.databinding.FragmentAssignmentBinding;
import com.app.trueleap.databinding.FragmentAssignmentViewBinding;
import com.app.trueleap.external.LocalStorage;


public class AssignmentViewFragment extends Fragment {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    LocalStorage localStorage;
    Context context;
    FragmentAssignmentViewBinding binding;

    private String mParam1;
    private String mParam2;

    public AssignmentViewFragment() {
        // Required empty public constructor
    }

    public static AssignmentViewFragment newInstance(String param1, String param2) {
        AssignmentViewFragment fragment = new AssignmentViewFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_assignment_view, container, false);
        binding = DataBindingUtil.bind(v);
        context = getContext();
        localStorage = LocalStorage.getInstance(context);
        initdata();
        initListeners();
        return binding.getRoot();
    }

    private void initListeners() {

    }

    private void initdata() {

    }
}