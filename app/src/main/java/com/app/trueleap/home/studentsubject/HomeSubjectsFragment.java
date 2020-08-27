package com.app.trueleap.home.studentsubject;

import android.content.Context;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.trueleap.R;
import com.app.trueleap.Retrofit.APIClient;
import com.app.trueleap.auth.AuthViewModel;
import com.app.trueleap.base.BaseFragment;
import com.app.trueleap.databinding.FragmentSubjectsBinding;
import com.app.trueleap.external.LocalStorage;
import com.app.trueleap.external.Utils;
import com.app.trueleap.home.studentsubject.adapter.subject_adapter;
import com.app.trueleap.home.studentsubject.interfaces.subjectlickListener;
import com.app.trueleap.home.studentsubject.viewModel.SubjectViewModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

import static com.app.trueleap.external.CommonFunctions.loadAssetsJsonObj;
import static com.app.trueleap.external.Utils.dateFormat;

public class HomeSubjectsFragment extends BaseFragment implements subjectlickListener {
    String TAG = HomeSubjectsFragment.class.getSimpleName();
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
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_subjects, container, false);

        binding = DataBindingUtil.bind(v);
        context = getContext();
        fragmentManager = getActivity().getSupportFragmentManager();
        localStorage = LocalStorage.getInstance(context);
        viewModel = ViewModelProviders.of(this).get(SubjectViewModel.class);
        initdata();
        //initObserver();
        initListeners();
        return binding.getRoot();
    }

    private void initObserver() {
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
    }

    private void initListeners() {

    }

    private void initdata() {

        binding.studentClass.setText(localStorage.getClassId());
        binding.studentSection.setText(localStorage.getSectionId());

        /*viewModel.subjectData(localStorage.getKeyUserToken());
        showProgressView();*/

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
                                for (int j=0;j<10;j++){
                                    JSONObject jsonObject = (JSONObject) jsonArray1.get(j);
                                    Date dateobj = new Date();
                                    String startDate = jsonObject.getString("startdate");
                                    Log.d(TAG,"sub date "+startDate.substring(0,10)+" "+dateFormat.format(dateobj));
                                    classDateArrayList.add(new ClassDate(
                                            startDate
                                    ));
                                    if (jsonObject.has("subject")) {
                                        subject = jsonObject.getString("subject");
                                        startTime = jsonObject.getString("starttime");
                                        endTime = jsonObject.getString("endtime");

                                    }
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
        ClassmaterialtypeFragment clsmtypeFragment = new ClassmaterialtypeFragment().newInstance(
                Subjects.get(position).getId(),
                Subjects.get(position).getSubject_title());

        fragmentManager
                .beginTransaction()
                .replace(R.id.frameContainer, clsmtypeFragment,
                        Utils.Subject_mat_type_Fragment).commit();
    }
}