package com.app.trueleap.Assignmentmodule;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.trueleap.Assignmentmodule.adapter.Asssignment_adapter;
import com.app.trueleap.Assignmentmodule.interfaces.assignmentClickListener;
import com.app.trueleap.Assignmentmodule.model.AssignmentModel;
import com.app.trueleap.Classnotemodule.adapter.classnote_adapter;
import com.app.trueleap.Classnotemodule.model.ClassnoteModel;
import com.app.trueleap.R;
import com.app.trueleap.databinding.FragmentAssignmentBinding;
import com.app.trueleap.databinding.FragmentClassnotesListingBinding;
import com.app.trueleap.external.LocalStorage;
import com.app.trueleap.external.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.app.trueleap.external.CommonFunctions.loadAssetsJsonObj;

public class AssignmentFragment extends Fragment implements assignmentClickListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    LocalStorage localStorage;
    Context context;
    FragmentAssignmentBinding binding;
    ArrayList<ClassnoteModel> class_assignment;
    JSONObject assignJson;
    Asssignment_adapter Assign_adpater;

    public AssignmentFragment() {
    }
    public static AssignmentFragment newInstance(String param1, String param2) {
        AssignmentFragment fragment = new AssignmentFragment();
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
        View v = inflater.inflate(R.layout.fragment_assignment, container, false);
        binding = DataBindingUtil.bind(v);
        context = getContext();
        localStorage = LocalStorage.getInstance(context);
        /*initdata();*/
        initListeners();
        return binding.getRoot();
    }

    private void initListeners() {

    }

   /* private void initdata() {

        binding.studentClass.setText(localStorage.getClassId());
        binding.studentSection.setText(localStorage.getSectionId());
        binding.sujectName.setText(mParam2 +" Assignments");

        class_assignment = new ArrayList<>();
        assignJson = loadAssetsJsonObj("assignment.json", context);
        try{
            JSONArray notesArray =  assignJson.getJSONArray("assignments");
            for(int i = 0 ; i< notesArray.length(); i++){
                class_assignment.add(new AssignmentModel(
                        notesArray.getJSONObject(i).getString("id"),
                        notesArray.getJSONObject(i).getString("note_title"),
                        notesArray.getJSONObject(i).getString("note_text"),
                        notesArray.getJSONObject(i).getString("uploaded_date"),
                        notesArray.getJSONObject(i).getString("note_doc_file"),
                        notesArray.getJSONObject(i).getString("doc_type"),
                        notesArray.getJSONObject(i).getString("doc_path")));
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        populateCategories();
    }*/

    private void populateCategories() {
        Assign_adpater  = new Asssignment_adapter(context,class_assignment,this);
        LinearLayoutManager llayoutmanager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        binding.rvClassNote.setLayoutManager(llayoutmanager);
        binding.rvClassNote.setAdapter(Assign_adpater);
        Assign_adpater.notifyDataSetChanged();
    }

    @Override
    public void onClicked(int position) {

        Intent assignintent  = new Intent(getActivity(), AssignmentViewActivity.class);
        assignintent.putExtra("title", class_assignment.get(position).getNote_title());
        assignintent.putExtra("text", class_assignment.get(position).getNote_text());
        assignintent.putExtra("date", class_assignment.get(position).getUploaded_date());
        startActivity(assignintent);



    }


}