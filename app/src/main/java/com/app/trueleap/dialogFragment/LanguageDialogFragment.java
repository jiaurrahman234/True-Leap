package com.app.trueleap.dialogFragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.app.trueleap.R;
import com.app.trueleap.external.LocalStorage;
import com.google.android.material.chip.ChipGroup;

import java.util.Locale;


public class LanguageDialogFragment extends DialogFragment {

    Activity activity;
    Context context;
    ChipGroup language;
    int checked_id=0;
    LocalStorage localStorage;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        context = getActivity();
        activity = (Activity)context;
        localStorage = new LocalStorage(context);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_language_dialog, null);
        language = view.findViewById(R.id.language);
        switch (localStorage.getSelectedLanguage()) {
            case "en" :
                /*language.setcheckedLanguage();*/
            case "hi" :
                /*language.setcheckedLanguage();*/
        }

        language.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ChipGroup group, int checkedId) {
                checked_id =  checkedId;
            }
        });

        builder.setView(view)
                // Add action buttons
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        selectLanguage(dialog);
                    }

                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        return builder.create();
    }

    private void selectLanguage(DialogInterface dialog) {
        Locale locale;
        if(checked_id!=0 ) {
            if(checked_id==R.id.hindi) {
                locale = new Locale("hi");
                localStorage.setSelectedLanguage("hi");
            }else {
                locale = new Locale("en");
                localStorage.setSelectedLanguage("en");
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