package com.app.trueleap.localization;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.app.trueleap.R;
import com.app.trueleap.SplashActivity;
import com.app.trueleap.base.BaseFragment;
import com.app.trueleap.databinding.FragmentForeignLangBinding;
import com.app.trueleap.localization.model.LanguageModel;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.Locale;

import static com.app.trueleap.external.Constants.LANG_FOREIGN;
import static com.app.trueleap.external.Constants.LANG_INDIAN;
import static com.app.trueleap.localization.ChangeLanguageActivity.checked_code;
import static com.app.trueleap.localization.ChangeLanguageActivity.languagelist;

public class ForeignLangFragment extends BaseFragment {
    FragmentForeignLangBinding binding;
    Context context;
    View v;

    public ForeignLangFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_foreign_lang, container, false);
        binding = DataBindingUtil.bind(v);
        context = getContext();
        fragmentManager = getActivity().getSupportFragmentManager();
        initdata();
        return binding.getRoot();
    }



    private void initdata() {
        if(languagelist.size()>0){
            for(int i=0; i <languagelist.size(); i++){
                if(languagelist.get(i).getLang_type()==LANG_FOREIGN) {
                    LanguageModel languageModel = languagelist.get(i) ;
                    Chip chip = (Chip) this.getLayoutInflater().inflate(R.layout.material_chip, null, false);
                    chip.setLayoutParams( new ChipGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT));
                    chip.setId(languagelist.get(i).getLang_id());
                    chip.setText(languagelist.get(i).getLang_name());
                    chip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if (isChecked) {
                                ((Chip) v.findViewById(languageModel.getLang_id())).setChecked(true);
                                checked_code = languageModel.getLang_code();
                                selectLanguage();
                            }
                        }
                    });

                    binding.language.addView(chip);
                }
            }
        }
        setPrevoiusSelected();
    }

    public void setPrevoiusSelected(){
        Log.d("TAG","language "+localStorage.getSelectedLanguage());
        if(languagelist.size()>0) {
            for (int i = 0; i < languagelist.size(); i++) {
                if(languagelist.get(i).getLang_type()==LANG_FOREIGN){
                    if((languagelist.get(i).getLang_code()).equals(localStorage.getSelectedLanguage())){
                        //((Chip) v.findViewById(languagelist.get(i).getLang_id())).setChecked(true);
                        checked_code = languagelist.get(i).getLang_code();
                    } else {
                        //((Chip) v.findViewById(languagelist.get(i).getLang_id())).setChecked(false);
                    }
                }
            }
        }
    }

    private void selectLanguage() {
        Locale locale = new Locale("en");
        Log.d("checked id", "fdfd " + checked_code);
        if (checked_code!=null) {
                locale = new Locale(checked_code);
                localStorage.setSelectedLanguage(checked_code);
                Locale.setDefault(locale);
                Configuration config = new Configuration();
                config.locale = locale;
                getContext().getResources().updateConfiguration(config, getContext().getResources().getDisplayMetrics());
        }
        //refresh activity
        Intent intent = new Intent(getActivity(), SplashActivity.class);
        getActivity().finish();
        startActivity(intent);
    }

}