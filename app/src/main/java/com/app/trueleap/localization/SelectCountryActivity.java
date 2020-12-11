package com.app.trueleap.localization;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.app.trueleap.R;
import com.app.trueleap.SplashActivity;
import com.app.trueleap.base.BaseActivity;
import com.app.trueleap.databinding.ActivitySelectCountryBinding;
import com.app.trueleap.external.LocalStorage;
import com.app.trueleap.interfaces.recyclerviewClickListener;
import com.app.trueleap.localization.adapter.country_adapter;
import com.app.trueleap.localization.model.CountryModel;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import static com.app.trueleap.external.CommonFunctions.loadAssetsJsonArray;

public class SelectCountryActivity extends BaseActivity implements recyclerviewClickListener {
    ArrayList<CountryModel> countrylist;
    Intent intent;
    ActivitySelectCountryBinding binding;
    country_adapter country_adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_select_country);
        context = SelectCountryActivity.this;
        intent = getIntent();
        loadcountry();
    }

    private void loadcountry() {
        countrylist = new ArrayList<>();
        try {
            JSONArray jsonArray = loadAssetsJsonArray("base_url.json", context );
            if(jsonArray.length()>0){
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject resultObject = jsonArray.getJSONObject(i);
                    countrylist.add(
                            new CountryModel(resultObject.getInt("id"),
                                    resultObject.getString("base_url"),
                                    resultObject.getString("name"))
                    );
                }
                populatecountry();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void populatecountry(){
        country_adapter = new country_adapter(context, countrylist, this);
        LinearLayoutManager llayoutmanager = new GridLayoutManager(context,2, LinearLayoutManager.VERTICAL, false);
        binding.countryList.setLayoutManager(llayoutmanager);
        binding.countryList.setAdapter(country_adapter);
        country_adapter.notifyDataSetChanged();
    }

    @Override
    public void onClicked(int position) {
        localStorage.setSelectedCountry(countrylist.get(position).getBase_url());
        Intent intent = new Intent(context, SplashActivity.class);
        this.finish();
        startActivity(intent);
    }
}