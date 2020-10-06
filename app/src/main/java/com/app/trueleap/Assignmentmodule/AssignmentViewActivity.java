package com.app.trueleap.Assignmentmodule;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.trueleap.Classnotemodule.model.ClassnoteModel;
import com.app.trueleap.R;
import com.app.trueleap.Retrofit.APIClient;
import com.app.trueleap.Retrofit.ApiClientFile;
import com.app.trueleap.auth.LoginActivity;
import com.app.trueleap.base.BaseActivity;
import com.app.trueleap.databinding.ActivityAssignmentBinding;
import com.app.trueleap.databinding.ActivityAssignmentViewBinding;
import com.google.android.material.snackbar.Snackbar;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.app.trueleap.external.CommonFunctions.getExtensionType;
import static com.app.trueleap.external.CommonFunctions.hasPermissionToDownload;
import static com.app.trueleap.external.CommonFunctions.parse_date;
import static com.app.trueleap.external.CommonFunctions.writeResponseBodyToDisk;
import static com.app.trueleap.external.Constants.CAMERA_REQUEST;
import static com.app.trueleap.external.Constants.DOC_ASSIGNMENT;
import static com.app.trueleap.external.Constants.MY_CAMERA_PERMISSION_CODE;
import static com.app.trueleap.external.Constants.PICK_IMAGE;
import static com.app.trueleap.external.Constants.PUBLIC;
import static com.app.trueleap.external.Constants.REQUEST_DOCUMENT;
import static com.app.trueleap.external.Constants.TAKE_IMAGE;

public class AssignmentViewActivity extends BaseActivity {
    Intent intent;
    ActivityAssignmentViewBinding binding;
    Bitmap uplaod_image;
    private static String IMAGE_DIRECTORY = "";
    private String imagepath = "";
    ClassnoteModel class_note;
    String subject_name, period_id, class_date;
    Snackbar snackbar;
    Uri uridata=null;
    public static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_assignment_view);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        IMAGE_DIRECTORY = "/" + getString(R.string.app_name);
        initToolbar();
        initdata();
        initListener();
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
            binding.submitAssignment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (hasPermissionToDownload(((Activity) context))) {
                        uploadFile();
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
        } else { //permission is automatically granted on sdk<23 upon installation
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
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("application/pdf");
        startActivityForResult(intent, REQUEST_DOCUMENT);
    }

    private void initdata() {
        try {
            intent = getIntent();
            if (intent.getExtras() != null) {
                class_note = (ClassnoteModel) intent.getExtras().getParcelable("assignment");
                subject_name = (String) intent.getStringExtra("subject_name");
                class_date = (String) intent.getStringExtra("class_date");
                period_id = intent.getStringExtra("period_id");
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
            binding.assignmentTitle.setText(class_note.getNote_title());
            binding.assignmentTextExcerpt.setText(class_note.getNote_text());
            binding.date.setText(parse_date(class_note.getUploaded_date()));
            if(!(class_note.getValidupto().equals(null))) {
                binding.dueDate.setText(parse_date(class_note.getValidupto()));
            }else {
                binding.dueDate.setText("--");
            }
            if (class_note.getNote_doc_file() != null) {
                binding.fileName.setText(class_note.getNote_doc_file());
            } else {
                binding.fileTitle.setVisibility(View.GONE);
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
                    binding.imagesToUpload.setVisibility(View.VISIBLE);
                    binding.imageView.setImageBitmap(uplaod_image);
                    binding.docName.setText("");
                } catch (Exception e) {
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
            //Uri uri = data.getData();
            uridata = data.getData();
            String uriString = uridata.toString();
            File myFile = new File(uriString);
            String path = myFile.getAbsolutePath();
            Log.d(TAG,"jkhkj: "+path);
            imagepath = path;
            String displayName = null;
            if (uriString.startsWith("content://")) {
                Cursor cursor = null;
                try {
                    cursor = getContentResolver().query(uridata, null, null, null, null);
                    if (cursor != null && cursor.moveToFirst()) {
                        displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                        binding.imagesToUpload.setVisibility(View.VISIBLE);
                        binding.imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_baseline_picture_as_pdf_24));
                        binding.docName.setText(displayName);
                    }
                } finally {
                    cursor.close();
                }
            } else if (uriString.startsWith("file://")) {
                displayName = myFile.getName();
                binding.imagesToUpload.setVisibility(View.VISIBLE);
                binding.imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_baseline_picture_as_pdf_24));
                binding.docName.setText(displayName);
            }
        }
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

    public String saveImage(Bitmap myBitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        try {
            File uploadDirectory = new File(Environment.getExternalStorageDirectory().getPath() + File.separator + "trueleap" + File.separator + "upload");
            if (!uploadDirectory.isDirectory()) {
                uploadDirectory.mkdir();
            }
            File uploadablefile = new File(uploadDirectory, "asssign" + ".jpg");
            if (uploadablefile.exists()) {
                uploadablefile.delete();
            }
            uploadablefile.createNewFile();
            FileOutputStream fo = new FileOutputStream(uploadablefile);
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(context,
                    new String[]{uploadablefile.getPath()},
                    new String[]{"image/jpeg"}, null);
            fo.close();
            return uploadablefile.getAbsolutePath();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return "";
    }

    private void download_file() {
        try{
            showProgressBar();
            Call<ResponseBody> call = null;
            call =   ApiClientFile
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
                        snackbar = Snackbar.make(binding.getRoot(), "Download Failed 1", Snackbar.LENGTH_LONG);
                        snackbar.show();
                    }
                }
                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    hideProgressBar();
                    snackbar = Snackbar.make(binding.getRoot(), "Download Failed 2", Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            });
        }catch (Exception e){
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

    public void uploadFile() {
        String name="Manoj";
        RequestBody nameBody = RequestBody.create(MediaType.parse("text/plain"), name);

        String upload_param = localStorage.getClassId() + ":AS" + ":" + localStorage.getId();

        RequestBody coverRequestFile = null;
        MultipartBody.Part photo1 = null;
        File file1 = new File(imagepath);
        coverRequestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file1);
        photo1 = MultipartBody.Part.createFormData("file", file1.getName(), coverRequestFile);
        if (imagepath.length() == 0) {
            Toast.makeText(context, "Please select one file", Toast.LENGTH_SHORT).show();
        } else {

            RequestBody note = RequestBody.create(MediaType.parse("text/plain"),
                    class_note.getNote_title());

            RequestBody title = RequestBody.create(MediaType.parse("text/plain"),
                    class_note.getNote_title());
            RequestBody sectionBody = RequestBody.create(MediaType.parse("text/plain"),
                    localStorage.getSectionId());
            RequestBody documentnumberBody = RequestBody.create(MediaType.parse("text/plain"),
                    class_note.getId());
            RequestBody uniqueperiodidBody = RequestBody.create(MediaType.parse("text/plain"),
                    period_id);

            RequestBody uploadparam = RequestBody.create(MediaType.parse("text/plain"),
                    upload_param);

           showProgressBar();
            Call<ResponseBody> call = null;
            call = ApiClientFile
                    .getInstance()
                    .getApiInterface()
                    .uploadDoc(localStorage.getKeyUserToken(), photo1, title, note, uploadparam,nameBody,sectionBody,documentnumberBody,uniqueperiodidBody);
            Log.d(TAG, "khhkjhkh:" + call.request());
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    hideProgressBar();
                    Log.d(TAG, "jljl:" + response.toString());
                    if (response.isSuccessful()) {
                        snackbar = Snackbar.make(binding.getRoot(), R.string.uploaded_successfully, Snackbar.LENGTH_LONG);
                        snackbar.show();
                    } else {
                        Log.d(TAG, "server contact failed");
                        snackbar = Snackbar.make(binding.getRoot(), "upload Failed", Snackbar.LENGTH_LONG);
                        snackbar.show();
                    }
                }
                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.e(TAG, "error");
                    hideProgressBar();
                    snackbar = Snackbar.make(binding.getRoot(), "upload Failed", Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            });
        }
    }
}