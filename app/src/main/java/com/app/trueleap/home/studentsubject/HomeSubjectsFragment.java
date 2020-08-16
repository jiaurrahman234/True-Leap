package com.app.trueleap.home.studentsubject;

import android.content.Context;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.trueleap.R;
import com.app.trueleap.databinding.FragmentSubjectsBinding;
import com.app.trueleap.external.LocalStorage;
import com.app.trueleap.external.Utils;
import com.app.trueleap.home.studentsubject.adapter.subject_adapter;
import com.app.trueleap.home.studentsubject.interfaces.subjectlickListener;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import static com.app.trueleap.external.CommonFunctions.loadAssetsJsonObj;

public class HomeSubjectsFragment extends Fragment implements subjectlickListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    LocalStorage localStorage;
    Context context;
    FragmentSubjectsBinding binding;
    ArrayList<SubjectModel> Subjects;
    JSONObject SubjectsJson;
    subject_adapter Subject_adpater;
    FragmentManager fragmentManager;

    public HomeSubjectsFragment() {
        // Required empty public constructor
    }

    public static HomeSubjectsFragment newInstance(String param1, String param2) {
        HomeSubjectsFragment fragment = new HomeSubjectsFragment();
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
        View v = inflater.inflate(R.layout.fragment_subjects, container, false);
        binding = DataBindingUtil.bind(v);
        context = getContext();
        fragmentManager = getActivity().getSupportFragmentManager();
        localStorage = LocalStorage.getInstance(context);
        initdata();
        initListeners();
        return binding.getRoot();
    }

    private void initListeners() {

    }

    private void initdata() {

        binding.studentClass.setText(localStorage.getClassId());
        binding.studentSection.setText(localStorage.getSectionId());

        Subjects = new ArrayList<>();
        SubjectsJson = loadAssetsJsonObj("subject.json", context);
        try{
            JSONArray SubjectsArray =  SubjectsJson.getJSONArray("subjects");
            for(int i = 0 ; i< SubjectsArray.length(); i++){
                Subjects.add(new SubjectModel(
                        SubjectsArray.getJSONObject(i).getString("id"),
                        SubjectsArray.getJSONObject(i).getString("subject_code"),
                        SubjectsArray.getJSONObject(i).getString("subject_name")));
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        populateSubjectListing();
    }

    private void populateSubjectListing() {
        Subject_adpater  = new subject_adapter(context,Subjects,this);
        LinearLayoutManager llayoutmanager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        binding.rvSubject.setLayoutManager(llayoutmanager);
        binding.rvSubject.setAdapter(Subject_adpater);
        Subject_adpater.notifyDataSetChanged();
    }

    @Override
    public void onClicked(int position) {
        ClassmaterialtypeFragment  clsmtypeFragment =  new ClassmaterialtypeFragment().newInstance(
                Subjects.get(position).getId(),
                Subjects.get(position).getSubject_title());

        fragmentManager
                .beginTransaction()
                .replace(R.id.frameContainer, clsmtypeFragment,
                        Utils.Subject_mat_type_Fragment).commit();
    }
}