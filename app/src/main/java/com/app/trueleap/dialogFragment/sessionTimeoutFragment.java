package com.app.trueleap.dialogFragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.app.trueleap.auth.LoginActivity;
import com.app.trueleap.auth.OfflineLoginActivity;
import com.app.trueleap.auth.SetupOfflineLoginActivity;
import com.app.trueleap.databinding.FragmentLanguageDialogBinding;
import com.app.trueleap.databinding.FragmentSessionTimoutBinding;
import com.app.trueleap.external.LocalStorage;
import com.google.android.material.chip.ChipGroup;

import java.util.Locale;

public class sessionTimeoutFragment extends DialogFragment {

    FragmentSessionTimoutBinding binding;
    Activity activity;
    Context context;
    ChipGroup language;
    int checked_id;
    LocalStorage localStorage;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        context = getActivity();
        activity = (Activity) context;
        localStorage = new LocalStorage(context);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_session_timout, null);
        binding = DataBindingUtil.bind(view);

        builder.setView(view)
                // Add action buttons
                .setPositiveButton(R.string.close, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        return builder.create();
    }

    @Override
    public void onResume() {
        super.onResume();
        final AlertDialog d = (AlertDialog) getDialog();
            /*if (d != null) {
                Button positiveButton = (Button) d.getButton(Dialog.BUTTON_POSITIVE);
                positiveButton.setEnabled(false);
            }*/
            binding.offlineLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(localStorage.isQuestionSet()) {
                        startActivity(new Intent(getActivity(), OfflineLoginActivity.class));
                        getActivity().finish();
                    }else {
                        startActivity(new Intent(getActivity(), SetupOfflineLoginActivity.class));
                        getActivity().finish();
                    }
                }
            });

            binding.loginBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                    getActivity().finish();
                }
            });
        }

    @Override
    public void onCancel(DialogInterface dialogInterface) {
        super.onCancel(dialogInterface);
    }
}