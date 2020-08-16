package com.app.trueleap.Assignmentmodule;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
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
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.trueleap.R;
import com.app.trueleap.auth.LoginActivity;
import com.app.trueleap.base.BaseActivity;
import com.app.trueleap.databinding.ActivityAssignmentBinding;
import com.app.trueleap.databinding.ActivityAssignmentViewBinding;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static com.app.trueleap.external.Constants.CAMERA_REQUEST;
import static com.app.trueleap.external.Constants.MY_CAMERA_PERMISSION_CODE;
import static com.app.trueleap.external.Constants.PICK_IMAGE;
import static com.app.trueleap.external.Constants.REQUEST_DOCUMENT;
import static com.app.trueleap.external.Constants.TAKE_IMAGE;

public class AssignmentViewActivity extends BaseActivity {

    Intent intent;
    ActivityAssignmentViewBinding binding;
    String text, title, date;
    ImageView Assignmentimage;
    Bitmap weight_img_btmap;
    private static String IMAGE_DIRECTORY = "";
    private String imagepath = "";
    Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_assignment_view);
        intent = getIntent();
        if (intent.getExtras() != null) {
            title= intent.getStringExtra("title");
            text  = intent.getStringExtra("text");
            date  = intent.getStringExtra("date");
        }
        context = AssignmentViewActivity.this;
        initToolbar();
        initdata();
        initListener();
    }

    private void initToolbar() {
        TextView toolbar_tv;
        Toolbar toolbar;
        toolbar_tv = (TextView) findViewById(R.id.toolbar_tv);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void initListener() {
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

    }

    private void PickImage() {
        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getIntent.setType("image/*");

        Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");

        Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});
        startActivityForResult(chooserIntent, PICK_IMAGE);
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
        binding.assignmentTitle.setText(title);
        binding.assignmentTextExcerpt.setText(text);
        binding.date.setText(date);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TAKE_IMAGE && resultCode == Activity.RESULT_OK)
        {
            if (!(data == null)) {
                try {
                    weight_img_btmap = (Bitmap) data.getExtras().get("data");
                    imagepath = saveImage(weight_img_btmap);
                    binding.imagesToUpload.setVisibility(View.VISIBLE);
                    binding.imageView.setImageBitmap(weight_img_btmap);
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
                    weight_img_btmap = BitmapFactory.decodeStream(inputStream);
                    imagepath = saveImage(weight_img_btmap);
                    binding.imagesToUpload.setVisibility(View.VISIBLE);
                    binding.imageView.setImageBitmap(weight_img_btmap);
                    binding.docName.setText("");
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }

        if (requestCode == REQUEST_DOCUMENT) {
                // Get the Uri of the selected file
                Uri uri = data.getData();
                String uriString = uri.toString();
                File myFile = new File(uriString);
                String path = myFile.getAbsolutePath();
                String displayName = null;
                if (uriString.startsWith("content://")) {
                    Cursor cursor = null;
                    try {
                        cursor = getContentResolver().query(uri, null, null, null, null);
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(context, "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
            else
            {
                Toast.makeText(context, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    public String saveImage(Bitmap myBitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File wallpaperDirectory = new File(Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY);
        // have the object build the directory structure, if needed.
        if (!wallpaperDirectory.exists()) {
            wallpaperDirectory.mkdirs();
        }

        try {
            File f = new File(wallpaperDirectory, "weight_image" + ".jpg");
            if (f.exists()) {
                f.delete();
            }
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(context,
                    new String[]{f.getPath()},
                    new String[]{"image/jpeg"}, null);
            fo.close();
            Log.d("TAG", "File Saved::" + f.getAbsolutePath());

            return f.getAbsolutePath();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return "";
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_change_lang:
                return true;
            case R.id.action_logout:
                try {
                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context, R.style.MyAlertDialogStyle);

                    builder.setTitle("Confirm")
                            .setIcon(R.drawable.logo)
                            .setMessage("Do you really want to logout?")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    localStorage.logoutUser();
                                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            })
                            .setNegativeButton(android.R.string.no, null);
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                    alertTheme(alertDialog);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return true;
        }
        return super.onOptionsItemSelected(item);
    }


}