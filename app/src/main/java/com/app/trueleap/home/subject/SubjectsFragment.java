package com.app.trueleap.home.subject;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.app.trueleap.R;
import com.app.trueleap.base.BaseActivity;
import com.app.trueleap.base.BaseFragment;
import com.app.trueleap.classcalenderview.CalenderViewActivity;
import com.app.trueleap.databinding.FragmentSubjectsBinding;
import com.app.trueleap.documentSearch.SearchActivity;
import com.app.trueleap.external.LocalStorage;
import com.app.trueleap.home.subject.adapter.subject_adapter;
import com.app.trueleap.home.subject.interfaces.subjectResponseCallback;
import com.app.trueleap.home.subject.interfaces.subjectlickListener;
import com.app.trueleap.home.subject.model.ClassModel;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import static com.app.trueleap.external.CommonFunctions.getJSONFromCache;
import static com.app.trueleap.external.CommonFunctions.isInternetOn;

public class SubjectsFragment extends BaseFragment implements subjectResponseCallback, subjectlickListener {
    String TAG = SubjectsFragment.class.getSimpleName();
    private String CACHE_FILE = "classes";
    LocalStorage localStorage;
    Context context;
    FragmentSubjectsBinding binding;
    ArrayList<ClassModel> Subjects;
    subject_adapter Subject_adpater;
    FragmentManager fragmentManager;

    public SubjectsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_subjects, container, false);
        binding = DataBindingUtil.bind(v);
        context = getContext();
        fragmentManager = getActivity().getSupportFragmentManager();
        localStorage = LocalStorage.getInstance(context);
        initdata();
        initListeners();
        return binding.getRoot();
    }

    private void initdata() {
        binding.studentClass.setText(localStorage.getClassId());
        binding.studentSection.setText(localStorage.getSectionId());
        Subjects = new ArrayList<>();

        if (!isInternetOn(context)||localStorage.IsOfflineLoggedin()){
            loadfromCache();
        } else {
            ((BaseActivity) getActivity()).getclasses(this);
        }
    }

    private void initListeners() {
        binding.searchLyt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), SearchActivity.class));
            }
        });
    }

    private void loadfromCache() {
        try {
            JSONArray jsonArray = getJSONFromCache(getActivity());
            Log.d(TAG, "subject response: " + jsonArray.length());
            if (jsonArray.length() > 0) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONArray json_array_at_level_1 = jsonArray.getJSONArray(i);
                    if (json_array_at_level_1.length() > 0) {
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
                                classJsonObject.getString("subject"), null, null));

                        populateSubjectListing(Subjects);

                    }else {
                        binding.noResult.setVisibility(View.VISIBLE);
                    }
                }
            } else {
                binding.noResult.setVisibility(View.VISIBLE);
            }
        }catch (Exception e) {
            e.printStackTrace();
            binding.noResult.setVisibility(View.VISIBLE);
        }
    }

    private void populateSubjectListing(ArrayList<ClassModel> Subjectslist) {
        Subject_adpater = new subject_adapter(context, Subjectslist, this);
        LinearLayoutManager llayoutmanager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        binding.rvSubject.setLayoutManager(llayoutmanager);
        binding.rvSubject.setAdapter(Subject_adpater);
        Subject_adpater.notifyDataSetChanged();
    }

    @Override
    public void onClicked(int position) {
        Intent intent = new Intent(getContext(), CalenderViewActivity.class);
        intent.putExtra("uniqueperiodid", Subjects.get(position).getUniqueperiodid());
        intent.putExtra("subject_name", Subjects.get(position).getSubject());
        startActivity(intent);
    }

    @Override
    public void onSuccesSubject(@NonNull ArrayList<ClassModel> value) {
        if(value.size()>0){
            Subjects = value;
            Log.d(TAG,"size"+value.size());
            populateSubjectListing(value);
        } else {
            binding.noResult.setVisibility(View.VISIBLE);
        }
    }
}