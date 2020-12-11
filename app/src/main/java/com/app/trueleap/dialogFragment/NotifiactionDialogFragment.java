package com.app.trueleap.dialogFragment;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import com.app.trueleap.R;
import com.app.trueleap.base.BaseActivity;
import com.app.trueleap.databinding.FragmentNotifiactionDialogBinding;
import com.app.trueleap.external.LocalStorage;
import com.app.trueleap.home.MainActivity;
import com.app.trueleap.interfaces.recyclerviewClickListener;
import com.app.trueleap.notification.NotificationModel;
import com.app.trueleap.notification.adapter.notification_adapter;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
public class NotifiactionDialogFragment extends DialogFragment implements recyclerviewClickListener {

    Context context;
    FragmentNotifiactionDialogBinding binding;
    LocalStorage localStorage;
    ArrayList<NotificationModel> notificationlist;
    notification_adapter notificationAdapter;

    public NotifiactionDialogFragment() {

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        notificationlist = new ArrayList<>();
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            notificationlist  = bundle.getParcelableArrayList("notificationlist");
        }

        localStorage = new LocalStorage(context);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.fragment_notifiaction_dialog, null);
        binding = DataBindingUtil.bind(view);

        populatenewNotificationlist();
        builder.setView(view)
                // Add action buttons
                .setPositiveButton(R.string.close, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
       });
        return builder.create();
    }

    private void populatenewNotificationlist() {
        notificationAdapter = new notification_adapter(context, notificationlist, this);
        LinearLayoutManager llayoutmanager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        binding.notificationList.setLayoutManager(llayoutmanager);
        binding.notificationList.setAdapter(notificationAdapter);
        notificationAdapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
        /*final AlertDialog d = (AlertDialog) getDialog();
            if (d != null) {
                Button positiveButton = (Button) d.getButton(Dialog.BUTTON_POSITIVE);
                positiveButton.setEnabled(false);
            }*/
    }

    @Override
    public void onCancel(DialogInterface dialogInterface) {
        super.onCancel(dialogInterface);
    }

    @Override
    public void onClicked(int position) {
        ((BaseActivity) getActivity()).readNotifications(notificationlist.get(position).getNotificationid());
        notificationlist.get(position).setViewed(true);
        ViewNotification(notificationlist.get(position).getNote());
        notificationAdapter.notifyDataSetChanged();
    }

    public void ViewNotification(String content) {
        try {
            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(context, R.style.MyAlertDialogStyle);
            View titleView = getLayoutInflater().inflate(R.layout.alerdialog_title, null);
            builder.setCustomTitle(titleView);
            builder.setMessage(content)
                    .setPositiveButton(R.string.close, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            dialog.dismiss();
                            ((MainActivity)context).onResume();
                        }
                    });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}