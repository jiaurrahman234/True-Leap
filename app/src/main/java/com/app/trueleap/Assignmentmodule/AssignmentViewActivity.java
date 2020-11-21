package com.app.trueleap.Assignmentmodule;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Context;
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
import android.provider.MediaStore;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.TextView;
import android.widget.Toast;
import com.app.trueleap.Classnotemodule.model.ClassnoteModel;
import com.app.trueleap.R;
import com.app.trueleap.Retrofit.ApiClientFile;
import com.app.trueleap.base.BaseActivity;
import com.app.trueleap.databinding.ActivityAssignmentViewBinding;
import com.app.trueleap.external.DBhelper;
import com.app.trueleap.service.documentuploadService;
import com.google.android.material.snackbar.Snackbar;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.security.PrivilegedAction;

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
import static com.app.trueleap.external.Constants.MY_CAMERA_PERMISSION_CODE;
import static com.app.trueleap.external.Constants.PICK_IMAGE;
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
        initdata();
        initListener();
        initialiseDbHelper();
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
            binding.submitAssignment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (hasPermissionToDownload(((Activity) context))) {
                        uploadFile(imagepath);
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
                Uri selectedImage = data.getData();

                String extension = selectedImage.getPath().substring(selectedImage.getPath().lastIndexOf("."));
                imagepath = saveFromURI(selectedImage, extension);
                Log.e("BaseActivity", "Document File Path: " + imagepath);
                binding.imagesToUpload.setVisibility(View.VISIBLE);
                binding.imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_baseline_picture_as_pdf_24));
                //binding.docName.setText(displayName);
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

            billFile  = new File(Environment.getExternalStorageDirectory().getPath() + File.separator + "trueleap" +File.separator+"upload"+File.separator+"file"+extension);
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
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        try {
            File uploadDirectory = new File(Environment.getExternalStorageDirectory().getPath() + File.separator + "trueleap" + File.separator + "upload");
            if (!uploadDirectory.isDirectory()) {
                uploadDirectory.mkdirs();
            }
            File uploadablefile = new File(uploadDirectory, "asssign" + ".jpg");
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

    public String saveDocument(String filename, File file) {
        try {
            File uploadDirectory = new File(Environment.getExternalStorageDirectory().getPath() + File.separator + "trueleap" + File.separator + "upload");
            if (!uploadDirectory.isDirectory()) {
                uploadDirectory.mkdir();
            }
            File uploadablefile = new File(uploadDirectory, filename);
            if (uploadablefile.exists()) {
                uploadablefile.delete();
            }

            FileChannel in = new FileInputStream(file).getChannel();
            FileChannel out = new FileOutputStream(uploadablefile).getChannel();

            try {
                in.transferTo(0, in.size(), out);
            } catch(Exception e){
                Log.d(TAG, "First Exception: " + e.getMessage());
                e.printStackTrace();
            } finally {
                if (in != null)
                    in.close();
                if (out != null)
                    out.close();
            }
        } catch (Exception e) {
            Log.d(TAG, "Second Exception: " + e.getMessage());
            e.printStackTrace();
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

    public void uploadFile(String imagepath) {

        RequestBody nameBody = RequestBody.create(MediaType.parse("text/plain"), localStorage.getFullName());
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
                        dBhelper.insertData(
                                class_note.getNote_title(),
                                class_note.getNote_title(),
                                upload_param,
                                localStorage.getFullName(),
                                localStorage.getSectionId(),
                                class_note.getId(),
                                period_id,
                                imagepath
                        );
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
                    dBhelper.insertData(
                            "",
                            "",
                            upload_param,
                            "",
                            localStorage.getSectionId(),
                            class_note.getId(),
                            period_id,
                            imagepath
                    );
                    startService();
                }
            });
        }
    }

    public void startService(){
       /* AlarmManager alarmManager;
        alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);*/
        Intent upload = new Intent(context, documentuploadService.class);
        context.startService(upload);

           /* if(((MainActivity)getActivity()).CheckGpsStatus()) {
                session.setKeyUserStatus(true);
                ((MainActivity)getActivity()).sendUserLocation(session.getKeyAddrLatitude(), session.getKeyAddrLongitude(), session.getKeyLocationName(), true);
                Intent intent = new Intent(context, LocationSenderCommandReceiver.class);
                intent.putExtra("command",1);
                pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), pendingIntent);
                Log.d(TAG,"broadcast 1 called");
            }else {
                provider_status.setChecked(false);
                ((MainActivity)getActivity()).showBookingGpsInternetError("gpsdisbaled");
            }*/


    }
}