package com.app.trueleap.Classnotemodule;

import android.content.Context;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.trueleap.Classnotemodule.adapter.classnote_adapter;
import com.app.trueleap.Classnotemodule.interfaces.noteClickListener;
import com.app.trueleap.Classnotemodule.model.ClassnoteModel;
import com.app.trueleap.R;
import com.app.trueleap.databinding.FragmentClassnotesListingBinding;
import com.app.trueleap.external.LocalStorage;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.app.trueleap.external.CommonFunctions.loadAssetsJsonObj;

public class ClassnotesFragmentListing extends Fragment implements noteClickListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    LocalStorage localStorage;
    Context context;
    FragmentClassnotesListingBinding binding;
    ArrayList<ClassnoteModel> class_notes;
    JSONObject notesJson;
    classnote_adapter note_adpater;

    public ClassnotesFragmentListing() {
        // Required empty public constructor
    }


    public static ClassnotesFragmentListing newInstance(String param1, String param2) {
        ClassnotesFragmentListing fragment = new ClassnotesFragmentListing();
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
        View v = inflater.inflate(R.layout.fragment_classnotes_listing, container, false);
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
        binding.studentClass.setText(localStorage.getClassId());
        binding.studentSection.setText(localStorage.getSectionId());
        binding.sujectName.setText(mParam2 +" Class Notes");

        class_notes = new ArrayList<>();
        notesJson = loadAssetsJsonObj("class_note.json", context);
        try{
            JSONArray notesArray =  notesJson.getJSONArray("class_notes");
            for(int i = 0 ; i< notesArray.length(); i++){
                class_notes.add(new ClassnoteModel(
                        notesArray.getJSONObject(i).getString("id"),
                        notesArray.getJSONObject(i).getString("note_title"),
                        notesArray.getJSONObject(i).getString("note_text"),
                        notesArray.getJSONObject(i).getString("uploaded_date"),
                        notesArray.getJSONObject(i).getString("note_doc_file"),
                        notesArray.getJSONObject(i).getString("doc_type")));
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        populateCategories();
    }

    private void populateCategories() {
        note_adpater  = new classnote_adapter(context,class_notes,this);
        LinearLayoutManager llayoutmanager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        binding.rvClassNote.setLayoutManager(llayoutmanager);
        binding.rvClassNote.setAdapter(note_adpater);
        note_adpater.notifyDataSetChanged();
    }

    @Override
    public void onClicked(int position) {

    }
}