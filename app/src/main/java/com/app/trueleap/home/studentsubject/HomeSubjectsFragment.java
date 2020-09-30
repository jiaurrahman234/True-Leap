package com.app.trueleap.home.studentsubject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.trueleap.R;
import com.app.trueleap.Retrofit.APIClient;
import com.app.trueleap.base.BaseFragment;
import com.app.trueleap.classcalenderview.CalenderViewActivity;
import com.app.trueleap.databinding.FragmentSubjectsBinding;
import com.app.trueleap.external.LocalStorage;
import com.app.trueleap.home.studentsubject.adapter.subject_adapter;
import com.app.trueleap.home.studentsubject.interfaces.subjectlickListener;
import com.app.trueleap.home.studentsubject.model.ClassModel;
import com.app.trueleap.home.studentsubject.viewModel.SubjectViewModel;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

import static com.app.trueleap.external.CommonFunctions.saveJSONToCache;

public class HomeSubjectsFragment extends BaseFragment implements subjectlickListener {
    String TAG = HomeSubjectsFragment.class.getSimpleName();
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    private String CACHE_FILE = "classes";

    LocalStorage localStorage;
    Context context;
    FragmentSubjectsBinding binding;
    ArrayList<ClassModel> Subjects;
    subject_adapter Subject_adpater;
    FragmentManager fragmentManager;
    SubjectViewModel viewModel;

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
        // Inflate the search_result_item for this fragment
        View v = inflater.inflate(R.layout.fragment_subjects, container, false);

        binding = DataBindingUtil.bind(v);
        context = getContext();
        fragmentManager = getActivity().getSupportFragmentManager();
        localStorage = LocalStorage.getInstance(context);
     /* viewModel = ViewModelProviders.of(this).get(SubjectViewModel.class);*/
        initdata();
        //initObserver();
        initListeners();
        return binding.getRoot();
    }

   /* private void initObserver() {
        try{
            viewModel.isApiSuccess.observe(this, it -> {
                hideProgressView();
            });

            viewModel.subjectData.observe(this,it->{
                Log.d(TAG, "Successfully login " + it.uniqueperiodid);
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }*/

    private void initListeners() {

    }


    // author -> Jia

    /*private void initdata() {

        binding.studentClass.setText(localStorage.getClassId());
        binding.studentSection.setText(localStorage.getSectionId());

        *//*viewModel.subjectData(localStorage.getKeyUserToken());
        showProgressView();*//*

        Subjects = new ArrayList<>();
        try {
            showProgressView();
            Call<ResponseBody> call = APIClient
                    .getInstance()
                    .getApiInterface()
                    .getSubjects(localStorage.getKeyUserToken());

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                    try {
                        hideProgressView();
                        String response_data = response.body().string();

                        JSONArray jsonArray = new JSONArray(response_data);
                        Log.d(TAG, "subject response: " + jsonArray.length());
                        if (jsonArray.length()>0){
                            for (int i = 0; i < jsonArray.length(); i++) {
                                ArrayList<ClassDate> classDateArrayList = new ArrayList<>();
                                JSONArray jsonArray1 = jsonArray.getJSONArray(i);
                                String subject="",startTime="",endTime="";
                                int currentPosition=0;
                                for (int j=0;j<jsonArray1.length();j++){
                                    JSONObject jsonObject = (JSONObject) jsonArray1.get(j);
                                    if (jsonObject.has("subject")) {
                                        subject = jsonObject.getString("subject");
                                        startTime = jsonObject.getString("starttime");
                                        endTime = jsonObject.getString("endtime");
                                    }

                                    Date curDate = new Date();
                                    String startDate = jsonObject.getString("startdate");
                                    Date classDate = dateFormat.parse(startDate.substring(0,10));

                                    if(classDate.compareTo(curDate) < 0) {
                                        currentPosition = j;
                                        Log.d(TAG,"sub date "+classDate+" "+dateFormat.format(curDate));
                                    }else{
                                        break;
                                    }

                                }
                                if (currentPosition>6){
                                    currentPosition = 6;
                                }
                                for (int k=currentPosition;k>=0;k--){
                                    JSONObject jsonObject = (JSONObject) jsonArray1.get(k);
                                    String startDate = jsonObject.getString("startdate");
                                    classDateArrayList.add(new ClassDate(
                                            startDate
                                    ));
                                }
                                Subjects.add(new SubjectModel(
                                        subject,
                                        subject,
                                        subject,
                                        startTime,
                                        endTime,
                                        classDateArrayList));
                            }

                        }
                        populateSubjectListing();
                    } catch (Exception e) {
                        e.printStackTrace();
                        hideProgressView();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    hideProgressView();
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }

    }*/

    /* auth manoj */

    /* Offline  */

    /*private void initdata() {

        binding.studentClass.setText(localStorage.getClassId());
        binding.studentSection.setText(localStorage.getSectionId());
        Subjects = new ArrayList<>();

                    try {
                        hideProgressView();
                        JSONArray jsonArray = loadAssetsJsonArray("subject.json", context);
                        saveJSONToCache(getActivity(),jsonArray.toString());
                        *//*JSONArray jsonArray = new JSONArray(assignJson);*//*
                        Log.d(TAG, "subject response: " + jsonArray.length());
                        if (jsonArray.length()>0){
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONArray json_array_at_level_1 = jsonArray.getJSONArray(i);
                                if (json_array_at_level_1.length()>0){
                                    JSONObject classJsonObject = json_array_at_level_1.getJSONObject(0);

                                    ArrayList<String> daysArraylist = new ArrayList<>();
                                    JSONArray days = classJsonObject.getJSONArray("days");
                                    for (int j = 0; j < days.length(); j++) {
                                        daysArraylist.add(days.getString(j));
                                    }

                                    Subjects.add(new ClassModel(classJsonObject.getString("uniqueperiodid"),
                                            classJsonObject.getString("teacher"),
                                            classJsonObject.getString("uniqueteacherid"),
                                            classJsonObject.getString("startdate"),
                                            classJsonObject.getString("enddate"),
                                            classJsonObject.getString("starttime"),
                                            classJsonObject.getString("endtime"),
                                            daysArraylist,
                                            classJsonObject.getString("class"),
                                            classJsonObject.getString("section"),
                                            classJsonObject.getString("subject"),null));
                                }
                            }
                        }
                        else {

                        }
                        populateSubjectListing();
                    } catch (Exception e) {
                        e.printStackTrace();
                        hideProgressView();
                    }
    }*/


    private void initdata() {
        binding.studentClass.setText(localStorage.getClassId());
        binding.studentSection.setText(localStorage.getSectionId());
        Subjects = new ArrayList<>();
        try {
            showProgressView();
            Call<ResponseBody> call = APIClient
                    .getInstance()
                    .getApiInterface()
                    .getSubjects(localStorage.getKeyUserToken(),true);

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                    try {
                        Log.d(TAG,"hkghkf: "+call.request());
                        hideProgressView();
                        String response_data = response.body().string();
                        saveJSONToCache(getActivity(),response_data);
                        JSONArray jsonArray = new JSONArray(response_data);
                        Log.d(TAG, "subject response: " + jsonArray.length());
                        if (jsonArray.length()>0){
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONArray json_array_at_level_1 = jsonArray.getJSONArray(i);
                                if (json_array_at_level_1.length()>0){
                                    JSONObject classJsonObject = json_array_at_level_1.getJSONObject(0);

                                    ArrayList<String> daysArraylist = new ArrayList<>();
                                    JSONArray days = classJsonObject.getJSONArray("days");
                                    for (int j = 0; j < days.length(); j++) {
                                        daysArraylist.add(days.getString(j));
                                    }

                                    Subjects.add(new ClassModel(classJsonObject.getString("uniqueperiodid"),
                                    classJsonObject.getString("teacher"),
                                    classJsonObject.getString("uniqueteacherid"),
                                    classJsonObject.getString("startdate"),
                                    classJsonObject.getString("enddate"),
                                    classJsonObject.getString("starttime"),
                                    classJsonObject.getString("endtime"), daysArraylist,
                                    classJsonObject.getString("class"),
                                    classJsonObject.getString("section"),
                                    classJsonObject.getString("subject"),null,null));
                                }
                            }
                        }
                        else {

                        }
                        populateSubjectListing();
                    } catch (Exception e) {
                        e.printStackTrace();
                        hideProgressView();
                    }
                }
                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    hideProgressView();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void populateSubjectListing() {
        Subject_adpater = new subject_adapter(context, Subjects, this);
        LinearLayoutManager llayoutmanager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        binding.rvSubject.setLayoutManager(llayoutmanager);
        binding.rvSubject.setAdapter(Subject_adpater);
        Subject_adpater.notifyDataSetChanged();
    }

    @Override
    public void onClicked(int position) {

        Intent intent = new Intent(getContext(), CalenderViewActivity.class);
        intent.putExtra("uniqueperiodid",Subjects.get(position).getUniqueperiodid());
        intent.putExtra("subject_name",Subjects.get(position).getSubject());
        startActivity(intent);

        /* ClassmaterialtypeFragment clsmtypeFragment = new ClassmaterialtypeFragment().newInstance(
                Subjects.get(position).getUniqueperiodid(),
                Subjects.get(position).getSubject());

        fragmentManager
                .beginTransaction()
                .replace(R.id.frameContainer, clsmtypeFragment,
                        Utils.Subject_mat_type_Fragment).commit();*/
    }
}