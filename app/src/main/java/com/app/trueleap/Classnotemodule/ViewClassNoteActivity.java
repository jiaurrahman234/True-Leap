package com.app.trueleap.Classnotemodule;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.app.trueleap.Classnotemodule.model.ClassnoteModel;
import com.app.trueleap.R;
import com.app.trueleap.Retrofit.ApiClientFile;
import com.app.trueleap.base.BaseActivity;
import com.app.trueleap.databinding.ActivityViewClassNoteBinding;
import com.app.trueleap.external.Converter;
import com.app.trueleap.interfaces.responseCallback;
import com.app.trueleap.notification.NotificationActivity;
import com.app.trueleap.notification.NotificationModel;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.app.trueleap.external.CommonFunctions.hasPermissionToDownload;
import static com.app.trueleap.external.CommonFunctions.parse_date;
import static com.app.trueleap.external.CommonFunctions.writeResponseBodyToDisk;

public class ViewClassNoteActivity extends BaseActivity implements responseCallback {

    ActivityViewClassNoteBinding binding;
    Intent intent;
    String subject_name, period_id, class_date;
    ClassnoteModel class_note;
    Snackbar snackbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_view_class_note);
        intent = getIntent();
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        initToolbar();
        initData();
        initListeners();
        getNotifications(this);
    }

    private void initData() {
        try {
            if (intent.getExtras() != null) {
                class_note = (ClassnoteModel) intent.getExtras().getParcelable("class_note");
                subject_name = (String) intent.getStringExtra("subject_name");
                period_id = (String) intent.getStringExtra("period_id");
                class_date = (String) intent.getStringExtra("class_date");
            }
            binding.studentClass.setText(localStorage.getClassId());
            binding.studentSection.setText(localStorage.getSectionId());
            binding.classDate.setText(class_date);
            binding.sujectName.setText(subject_name + " Class Notes");
            renderContent();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void renderContent() {
        if (class_note.getFile_url().length() != 0) {
            binding.documentLink.setVisibility(View.VISIBLE);
            String styledText = "Document Link: <font color='" + getResources().getColor(R.color.colorPrimary) + "'>" + class_note.getFile_url() + "</font>";
            binding.documentLink.setText(Html.fromHtml(styledText), TextView.BufferType.SPANNABLE);
        } else {
            binding.documentLink.setVisibility(View.GONE);
        }
        binding.noteTitle.setText(class_note.getNote_title());
        binding.noteTextFull.setText(class_note.getNote_text());
        binding.noteDate.setText(parse_date(class_note.getUploaded_date()));
        if (class_note.getNote_doc_file().trim().length() !=0) {
            binding.fileName.setText(class_note.getNote_doc_file());
        } else {
            binding.fileTitle.setVisibility(View.GONE);
        }
    }

    private void initListeners() {
        binding.fileTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File docfile = new File(Environment.getExternalStorageDirectory().getPath() + File.separator + "trueleap" + File.separator + class_note.getId() + "_" + class_note.getNote_doc_file());
                if (docfile.exists()) {
                    openFile(docfile);
                } else {
                    if (hasPermissionToDownload(((Activity) context))) {
                        download_file();
                    }
                }
            }
        });

        binding.documentLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(class_note.getFile_url()));
                    startActivity(browserIntent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void download_file() {
        showProgressBar();
        Call<ResponseBody> call = null;
        call = ApiClientFile
                .getInstance()
                .getApiInterface()
                .getDocument(localStorage.getKeyUserToken(), period_id, class_note.getId());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                hideProgressBar();
                if (response.isSuccessful()) {
                    boolean writtenToDisk = writeResponseBodyToDisk(response.body(), class_note.getId(), class_note.getNote_doc_file(), class_note.getDoc_type());
                    snackbar = Snackbar.make(binding.getRoot(), R.string.downloaded_successfully, Snackbar.LENGTH_LONG)
                            .setAction(R.string.open, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    File billFile = new File(Environment.getExternalStorageDirectory().getPath() + File.separator + "trueleap" + File.separator + class_note.getId() + "_" + class_note.getNote_doc_file());
                                    openFile(billFile);
                                }
                            });
                    snackbar.show();
                } else {
                    snackbar = Snackbar.make(binding.getRoot(), "Download Failed", Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                hideProgressBar();
                snackbar = Snackbar.make(binding.getRoot(), "Download Failed", Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        });
    }

    public void openFile(File file) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(file), class_note.getDoc_type());
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem menuItem = menu.findItem(R.id.action_notification);
        menuItem.setIcon(Converter.convertLayoutToImage(this, localStorage.getNotificationCount(), R.drawable.ic_baseline_notifications_24));
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_notification:
                startActivity(new Intent(this, NotificationActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSuccess(@NonNull ArrayList<NotificationModel> value) {
        if(value.size()>0) {
            invalidateOptionsMenu();
        }
    }
}