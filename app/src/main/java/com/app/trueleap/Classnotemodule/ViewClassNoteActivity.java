package com.app.trueleap.Classnotemodule;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.view.View;

import androidx.databinding.DataBindingUtil;

import com.app.trueleap.Classnotemodule.model.ClassnoteModel;
import com.app.trueleap.R;
import com.app.trueleap.Retrofit.ApiClientFile;
import com.app.trueleap.base.BaseActivity;
import com.app.trueleap.databinding.ActivityViewClassNoteBinding;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.app.trueleap.external.CommonFunctions.hasPermissionToDownload;
import static com.app.trueleap.external.CommonFunctions.parse_date;
import static com.app.trueleap.external.CommonFunctions.writeResponseBodyToDisk;

public class ViewClassNoteActivity extends BaseActivity {

    ActivityViewClassNoteBinding binding;
    Intent intent;
    String subject_name,period_id;
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
    }

    private void initData() {
        try{
            if (intent.getExtras() != null) {
                class_note = (ClassnoteModel) intent.getExtras().getParcelable("class_note");
                subject_name = (String) intent.getStringExtra("subject_name");
                period_id = (String) intent.getStringExtra("period_id");
            }
            binding.studentClass.setText(localStorage.getClassId());
            binding.studentSection.setText(localStorage.getSectionId());
            binding.sujectName.setText(subject_name +" Class Notes");
            renderContent();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void renderContent() {
        binding.noteTitle.setText(class_note.getNote_title());
        binding.noteTextFull.setText(class_note.getNote_text());
        binding.noteDate.setText(parse_date(class_note.getUploaded_date()));
        if(class_note.getNote_doc_file()!=null){
            binding.fileName.setText(class_note.getNote_doc_file());
        }else {
            binding.fileTitle.setVisibility(View.GONE);
        }
    }

    private void initListeners() {
        binding.fileTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File docfile = new File(Environment.getExternalStorageDirectory().getPath() + File.separator + "trueleap" +File.separator+class_note.getId()+"_"+class_note.getNote_doc_file());
                if(docfile.exists()){
                    openFile(docfile);
                }else {
                    if(hasPermissionToDownload(((Activity)context))){
                        download_file();
                    }
                }
            }
        });
    }

    private void download_file(){
        showProgressBar();
        Call<ResponseBody> call = null;
        call =   ApiClientFile
                .getInstance()
                .getApiInterface()
                .getDocument(localStorage.getKeyUserToken(),period_id,class_note.getId());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                hideProgressBar();
                if (response.isSuccessful()) {
                    boolean writtenToDisk = writeResponseBodyToDisk(response.body() , class_note.getId() , class_note.getNote_doc_file(), class_note.getDoc_type() );
                    snackbar = Snackbar.make(binding.getRoot(), R.string.downloaded_successfully ,Snackbar.LENGTH_LONG )
                            .setAction(R.string.open, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    File billFile  = new File(Environment.getExternalStorageDirectory().getPath() + File.separator + "trueleap" +File.separator+class_note.getId()+"_"+class_note.getNote_doc_file());
                                    openFile(billFile);
                                }
                            });
                    snackbar.show();
                } else {
                    snackbar = Snackbar.make(binding.getRoot(), "Download Failed" ,Snackbar.LENGTH_LONG );
                    snackbar.show();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                hideProgressBar();
                snackbar = Snackbar.make(binding.getRoot(), "Download Failed" ,Snackbar.LENGTH_LONG );
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
}