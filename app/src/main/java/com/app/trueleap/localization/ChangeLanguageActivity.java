package com.app.trueleap.localization;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.app.trueleap.R;
import com.app.trueleap.SplashActivity;
import com.app.trueleap.base.BaseActivity;
import com.app.trueleap.databinding.ActivityChangeLanguageBinding;
import com.app.trueleap.gradebook.model.GradeItem;
import com.app.trueleap.localization.model.LanguageModel;
import com.google.android.material.tabs.TabLayout;

import org.intellij.lang.annotations.Language;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.app.trueleap.external.CommonFunctions.loadAssetsJsonArray;

public class ChangeLanguageActivity extends BaseActivity {

    ActivityChangeLanguageBinding binding;
    Context context;
    Intent intent;
    public static ArrayList<LanguageModel> languagelist;
    public static String checked_code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        binding = DataBindingUtil.setContentView(this,R.layout.activity_change_language);
        context = ChangeLanguageActivity.this;
        intent = getIntent();
        initdata();
    }

    private void initdata() {
        loadlanguage();
        try {
            ViewPager viewPager = (ViewPager) findViewById(R.id.languageViewPager);
            viewPager.setOffscreenPageLimit(2);
            SimpleFragmentPagerAdapter adapter = new SimpleFragmentPagerAdapter(this, getSupportFragmentManager());
            viewPager.setAdapter(adapter);
            viewPager.setOffscreenPageLimit(2);
            TabLayout tabLayout = (TabLayout) findViewById(R.id.lang_tablayout);
            tabLayout.setupWithViewPager(viewPager);
            viewPager.setOffscreenPageLimit(2);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void loadlanguage() {
        languagelist = new ArrayList<>();
        try {
            JSONArray jsonArray = loadAssetsJsonArray("language.json", context );
            if(jsonArray.length()>0){
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject resultObject = jsonArray.getJSONObject(i);
                    languagelist.add(
                            new LanguageModel(resultObject.getInt("lang_id"),
                                    resultObject.getString("language_name"),
                                    resultObject.getString("language_code"),
                                    resultObject.getInt("type"))
                    );
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public class SimpleFragmentPagerAdapter extends FragmentPagerAdapter {

        private Context mContext;
        public SimpleFragmentPagerAdapter(Context context, FragmentManager fm) {
            super(fm);
            mContext = context;
        }
        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return new IndianLangFragment();
            }else {
                return new ForeignLangFragment();
            }
        }

        // This determines the number of tabs
        @Override
        public int getCount() {
            return 2;
        }

        // This determines the title for each tab
        @Override
        public CharSequence getPageTitle(int position) {
            // Generate title based on item position
            switch (position) {
                case 0:
                    return "Indian";
                case 1:
                    return "Foreign ";
                default:
                    return null;
            }
        }
    }

}