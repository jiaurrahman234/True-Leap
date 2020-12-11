package com.app.trueleap.Assignmentmodule;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.app.trueleap.Assignmentmodule.interfaces.uploadResponseCallback;
import com.app.trueleap.Classnotemodule.model.ClassnoteModel;
import com.app.trueleap.R;
import com.app.trueleap.Retrofit.APIClient;
import com.app.trueleap.base.BaseActivity;
import com.app.trueleap.databinding.ActivityAssignmentViewBinding;
import com.app.trueleap.external.CommonFunctions;
import com.app.trueleap.external.Constants;
import com.app.trueleap.external.DBhelper;
import com.app.trueleap.model.ErrorResponse;
import com.google.android.material.snackbar.Snackbar;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.annotation.Annotation;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Converter;
import retrofit2.Response;

import static com.app.trueleap.external.CommonFunctions.hasPermissionToDownload;
import static com.app.trueleap.external.CommonFunctions.parse_date;
import static com.app.trueleap.external.CommonFunctions.showSnackView;
import static com.app.trueleap.external.CommonFunctions.writeResponseBodyToDisk;
import static com.app.trueleap.external.Constants.CAMERA_REQUEST;
import static com.app.trueleap.external.Constants.MY_CAMERA_PERMISSION_CODE;
import static com.app.trueleap.external.Constants.PICK_IMAGE;
import static com.app.trueleap.external.Constants.REQUEST_DOCUMENT;
import static com.app.trueleap.external.Constants.TAKE_IMAGE;

public class AssignmentViewActivity extends BaseActivity implements uploadResponseCallback {
    Intent intent;
    ActivityAssignmentViewBinding binding;
    Bitmap uplaod_image;
    private static String IMAGE_DIRECTORY = "";
    private String imagepath = "";
    ClassnoteModel class_note;
    String subject_name, period_id, class_date;
    Snackbar snackbar;
    Uri uridata = null;
    public static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1;
    DBhelper dBhelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_assignment_view);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        IMAGE_DIRECTORY = "/" + getString(R.string.app_name);
        initToolbar();
        initialiseDbHelper();
        initdata();
        initListener();
    }

    private void initialiseDbHelper() {
        dBhelper = new DBhelper(this);
        try {
            dBhelper.createDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initListener() {
        try{
            binding.takePhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    takeImage();
                }
            });
            binding.addPhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PickImage();
                }
            });
            binding.uploadDoc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    performFileSearch();
                }
            });
            binding.openDocument.setOnClickListener(new View.OnClickListener() {
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
            binding.download.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isStoragePermissionGranted()) {
                        downLoadFile();
                    }
                }
            });
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
            binding.submitAssignment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (hasPermissionToDownload(((Activity) context))) {
                        uploadFile(AssignmentViewActivity.this, imagepath , class_note.getId(), period_id );
                    }
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void downLoadFile() {
        try {
            File directory = new File(Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY);
            if (!directory.exists()) {
                directory.mkdirs();
            }
            File docfile = new File(Environment.getExternalStorageDirectory().getPath() + File.separator + "trueleap" + File.separator + class_note.getId() + "_" + class_note.getNote_doc_file());
            if (docfile.exists()) {
                openFile(docfile);
            } else {
                if (hasPermissionToDownload(((Activity) context))) {
                    download_file();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG, "Permission is granted");
                return true;
            } else {
                Log.v(TAG, "Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
                return false;
            }
        } else {
            Log.v(TAG, "Permission is granted");
            return true;
        }
    }

    private void PickImage() {
        try{
            Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
            getIntent.setType("image/*");
            Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            pickIntent.setType("image/*");
            Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});
            startActivityForResult(chooserIntent, PICK_IMAGE);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void takeImage() {
        if (context.checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
        } else {
            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, TAKE_IMAGE);
        }
    }

    public void performFileSearch() {
//        Intent intent = new Intent();
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        intent.setType("*/*");
//        startActivityForResult(intent, REQUEST_DOCUMENT);

        Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
        chooseFile.setType("*/*");
        chooseFile = Intent.createChooser(chooseFile, "Choose a file");
        startActivityForResult(chooseFile, REQUEST_DOCUMENT);
    }

    private void initdata() {
        try {
            intent = getIntent();
            if (intent.getExtras() != null) {
                class_note = (ClassnoteModel) intent.getExtras().getParcelable("assignment");
                subject_name = (String) intent.getStringExtra("subject_name");
                class_date = (String) intent.getStringExtra("class_date");
                period_id = intent.getStringExtra("period_id");
                Log.e("BaseActivity", "Period Id Actiivty: " + period_id);
            }
            binding.studentClass.setText(localStorage.getClassId());
            binding.studentSection.setText(localStorage.getSectionId());
            binding.classDate.setText(class_date);
            binding.sujectName.setText(subject_name + " Assignments");
            renderContent();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void renderContent() {
        try{
            if (class_note.getFile_url().length() != 0) {
                binding.documentLink.setVisibility(View.VISIBLE);
                String styledText = "Document Link: <font color='" + getResources().getColor(R.color.colorPrimary) + "'>" + class_note.getFile_url() + "</font>";
                binding.documentLink.setText(Html.fromHtml(styledText), TextView.BufferType.SPANNABLE);
            } else {
                binding.documentLink.setVisibility(View.GONE);
            }
            binding.assignmentTitle.setText(class_note.getNote_title());
            binding.assignmentTextExcerpt.setText(class_note.getNote_text());
            binding.date.setText(parse_date(class_note.getUploaded_date()));
            if(class_note.getValidupto().trim().length() !=0) {
                binding.dueDate.setText(parse_date(class_note.getValidupto()));
            }else {
                binding.dueDate.setText("--");
            }
            if (class_note.getNote_doc_file().trim().length()!=0) {
                binding.fileName.setText(class_note.getNote_doc_file());
                binding.openDocument.setVisibility(View.VISIBLE);
                binding.download.setVisibility(View.VISIBLE);
            } else {
                binding.fileTitle.setVisibility(View.GONE);
                binding.openDocument.setVisibility(View.GONE);
                binding.download.setVisibility(View.GONE);
            }
            if(getAssignmentStatus()==1){
                binding.status.setText(getResources().getString(R.string.assignment_submitted));
            }else {
                binding.status.setText(getResources().getString(R.string.assignment_not_submitted));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TAKE_IMAGE && resultCode == Activity.RESULT_OK) {
            if (!(data == null)) {
                try {
                    uplaod_image = (Bitmap) data.getExtras().get("data");
                    imagepath = saveImage(uplaod_image);
                    Log.d(TAG, "image path " + imagepath);
                    binding.imagesToUpload.setVisibility(View.VISIBLE);
                    binding.imageView.setImageBitmap(uplaod_image);
                    binding.docName.setText("");
                } catch (Exception e) {
                    Log.e("BaseActivity", "Save Upload Image Error" +  e.getMessage());
                    e.printStackTrace();
                }
            }
        }
        if (requestCode == PICK_IMAGE) {
            if (!(data == null)) {
                try {
                    InputStream inputStream = context.getContentResolver().openInputStream(data.getData());
                    uplaod_image = BitmapFactory.decodeStream(inputStream);
                    imagepath = saveImage(uplaod_image);
                    Log.d(TAG, "image path " + imagepath);
                    binding.imagesToUpload.setVisibility(View.VISIBLE);
                    binding.imageView.setImageBitmap(uplaod_image);
                    binding.docName.setText("");
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }

        if (requestCode == REQUEST_DOCUMENT) {
            if (!(data == null)) {
                Uri selectedfile = data.getData();
                String extension = selectedfile.getPath().substring(selectedfile.getPath().lastIndexOf("."));
                Log.d(TAG,"extension: "+extension);
               // String fileneme = selectedfile.getPathSegments().lastIndexOf();
                imagepath = saveFromURI(selectedfile, extension);
                Log.e("BaseActivity", "Document File Path: " + imagepath);
                binding.imagesToUpload.setVisibility(View.VISIBLE);
                binding.imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_baseline_picture_as_pdf_24));
               // binding.docName.setText(fileneme);
            }
        }
    }

    private String saveFromURI(Uri selectedImage , String extension) {
        try {
            File billFile;
            File billDirectory = new File(Environment.getExternalStorageDirectory().getPath() + File.separator + "trueleap");
            if(!billDirectory.isDirectory()) {
                billDirectory.mkdir();
            }

            billFile  = new File(Environment.getExternalStorageDirectory().getPath() + File.separator + "trueleap" +File.separator+"upload"+File.separator+"document_"+class_note.getId()+extension);
            File myfile = new File(selectedImage.getPath());

            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                inputStream = getContentResolver().openInputStream(selectedImage);
                byte[] fileReader = new byte[4096];
                long fileSize = myfile.length();
                long fileSizeDownloaded = 0;
                outputStream = new FileOutputStream(billFile);

                while (true) {
                    int read = inputStream.read(fileReader);
                    if (read == -1) {
                        break;
                    }
                    outputStream.write(fileReader, 0, read);
                    fileSizeDownloaded += read;
                    Log.d("TAG", "file download: " + fileSizeDownloaded + " of " + fileSize);
                }
                outputStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
                return billFile.getAbsolutePath();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public String saveImage(Bitmap myBitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        try {
            File uploadDirectory = new File(Environment.getExternalStorageDirectory().getPath() + File.separator + "trueleap" + File.separator + "upload");
            if (!uploadDirectory.isDirectory()) {
                uploadDirectory.mkdirs();
            }
            File uploadablefile = new File(uploadDirectory, "document_"+class_note.getId()+ ".jpg");
            if (uploadablefile.exists()) {
                uploadablefile.delete();
            }
            uploadablefile.createNewFile();
            FileOutputStream fo = new FileOutputStream(uploadablefile);
            fo.write(bytes.toByteArray());
            fo.close();
            return uploadablefile.getAbsolutePath();
        } catch (Exception e1) {
            Log.e("BaseActivity", "File Image Uplaod Exception: " + e1.getMessage());
            e1.printStackTrace();
        }
        return "";
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(context, "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            } else {
                Toast.makeText(context, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        } else {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                downLoadFile();
            } else {
                Toast.makeText(context, "Can't download file", Toast.LENGTH_SHORT).show();
            }
            return;
        }
    }

    private void download_file() {
        try{
            showProgressBar();
            Call<ResponseBody> call = null;
            call =   APIClient
                    .getInstance(localStorage.getSelectedCountry())
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
                        String errorBody = response.errorBody().toString();
                        Log.d(TAG, "error data: " + errorBody);
                        Response<?> errorResponse = response;
                        ResponseBody errorbody = errorResponse.errorBody();
                        Converter<ResponseBody, ErrorResponse> converter = APIClient.getRetrofit().responseBodyConverter(ErrorResponse.class, new Annotation[0]);
                        try {
                            ErrorResponse errorObject = converter.convert(errorbody);
                            Log.d(TAG, " Error_code : " + errorObject.getError_code());
                            Log.d(TAG, " Error_messeage : " + errorObject.getError_message());
                            if (errorObject.getError_code().equals(Constants.ERR_INVALID_TOKEN) || errorObject.getError_code().equals(Constants.ERR_SESSION_TIMEOUT)) {
                                showSesstionTimeout();
                            } else {
                                CommonFunctions.showSnackView(binding.getRoot(), errorObject.getError_message());
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            snackbar = Snackbar.make(binding.getRoot(), "Download Failed!", Snackbar.LENGTH_LONG);
                            snackbar.show();
                        }

                    }
                }
                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    hideProgressBar();
                    snackbar = Snackbar.make(binding.getRoot(), "Download Failed!", Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            });
        } catch (Exception e){
            e.printStackTrace();
        }
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
    public void uploadresponse(String message) {
        initdata();
        showSnackView(binding.getRoot(), message);
    }

    public int getAssignmentStatus(){
        int assignment_status=0;
        final Cursor cursor = dBhelper.getData();
        Log.e(TAG, "Cursor Count: " + cursor.getCount());
        if(cursor.getCount()>0) {
            while (cursor.moveToNext()) {
                String documentString = cursor.getString(cursor.getColumnIndex("documentnumber"));
                Log.e(TAG, "documentString Id Service: " + documentString);
                if(documentString.equals(class_note.getId())) {
                    assignment_status = cursor.getInt(cursor.getColumnIndex("status"));
                    Log.e(TAG, "document status: " + assignment_status);
                }
            }
        }
        return assignment_status;
    }

    @Override
    protected void onResume() {
        super.onResume();
        initdata();
    }
}