package com.app.trueleap.home.studentsubject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.trueleap.Assignmentmodule.AssignmentActivity;
import com.app.trueleap.Classnotemodule.ClassNotesActivity;
import com.app.trueleap.R;
import com.app.trueleap.databinding.FragmentClassmaterialtypeBinding;
import com.app.trueleap.databinding.FragmentSubjectsBinding;
import com.app.trueleap.external.LocalStorage;
import com.app.trueleap.home.studentsubject.adapter.subject_adapter;

import org.json.JSONObject;

import java.util.ArrayList;


public class ClassmaterialtypeFragment extends Fragment {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;

    FragmentClassmaterialtypeBinding binding;
    LocalStorage localStorage;
    Context context;
    FragmentManager fragmentManager;


    public ClassmaterialtypeFragment() {
        // Required empty public constructor
    }


    public static ClassmaterialtypeFragment newInstance(String param1, String param2) {
        ClassmaterialtypeFragment fragment = new ClassmaterialtypeFragment();
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
        View v = inflater.inflate(R.layout.fragment_classmaterialtype, container, false);
        binding = DataBindingUtil.bind(v);
        context = getContext();
        fragmentManager = getActivity().getSupportFragmentManager();
        localStorage = LocalStorage.getInstance(context);
        initdata();
        initListeners();
        return binding.getRoot();
    }

    private void initListeners() {

        binding.actionClassnote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent noteintent = new Intent(getActivity(), ClassNotesActivity.class);
                noteintent.putExtra("subject_code", mParam1);
                noteintent.putExtra("subject_name", mParam2);
                startActivity(noteintent);

            }
        });
        binding.actionAssignment.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            Intent assignintent = new Intent(getActivity(), AssignmentActivity.class);
                                                            assignintent.putExtra("subject_code", mParam1);
                                                            assignintent.putExtra("subject_name", mParam2);
                                                            startActivity(assignintent);
                                                        }
                                                    }
        );

    }

    private void initdata() {
        binding.studentClass.setText(localStorage.getClassId());
        binding.studentSection.setText(localStorage.getSectionId());

        if (!mParam2.equals(""))
            binding.sujectName.setText(mParam2);
    }
}