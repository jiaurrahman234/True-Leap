package com.app.trueleap.dialogFragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;

import com.app.trueleap.R;
import com.app.trueleap.databinding.FragmentLanguageDialogBinding;
import com.app.trueleap.external.LocalStorage;
import com.google.android.material.chip.ChipGroup;
import java.util.Locale;

public class LanguageDialogFragment extends DialogFragment {

    FragmentLanguageDialogBinding binding;
    Activity activity;
    Context context;
    ChipGroup language;
    int checked_id;
    LocalStorage localStorage;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        context = getActivity();
        activity = (Activity)context;
        localStorage = new LocalStorage(context);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_language_dialog, null);
        binding = DataBindingUtil.bind(view);

        language = view.findViewById(R.id.language);
        switch (localStorage.getSelectedLanguage()) {
            case "en" :
                binding.english.setChecked(true);
                checked_id = binding.english.getId();
            case "hi" :
                binding.hindi.setChecked(true);
                checked_id = binding.hindi.getId();
        }

        binding.english.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    checked_id = binding.english.getId();
                    Log.d("cdsc", "eng checked");
                }
            }
        });

        binding.hindi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    checked_id = binding.hindi.getId();
                    Log.d("cdsc", "hind checked");
                }
            }
        });


        builder.setView(view)
                // Add action buttons
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        selectLanguage(dialog);
                    }

                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        return builder.create();
    }

    private void selectLanguage(DialogInterface dialog) {
        Locale locale;
        locale = new Locale("en");
        Log.d("checked id" ,"fdfd "+checked_id);
        if(checked_id!=0 ) {
            if(checked_id==binding.hindi.getId()) {
                locale = new Locale("hi");
                localStorage.setSelectedLanguage("hi");
                Log.d("ds","hindi");
            }else if(checked_id==binding.english.getId()){
                locale = new Locale("en");
                localStorage.setSelectedLanguage("en");
                Log.d("ds","englsh");
            }
            Locale.setDefault(locale);
            Configuration config = new Configuration();
            config.locale = locale;
            getContext().getResources().updateConfiguration(config, getContext().getResources().getDisplayMetrics());
            }
        dialog.cancel();
    }

    @Override
    public void onResume() {
        super.onResume();
        final AlertDialog d = (AlertDialog) getDialog();
            /*if (d != null) {
                Button positiveButton = (Button) d.getButton(Dialog.BUTTON_POSITIVE);
                positiveButton.setEnabled(false);
            }*/
        }


    @Override
    public void onCancel(DialogInterface dialogInterface){
        super.onCancel(dialogInterface);
    }
}