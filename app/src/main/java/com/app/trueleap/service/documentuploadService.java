package com.app.trueleap.service;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import com.app.trueleap.R;
import com.app.trueleap.Retrofit.ApiClientFile;
import com.app.trueleap.external.DBhelper;
import com.app.trueleap.external.DatabaseHelper;
import com.app.trueleap.external.LocalStorage;
import com.app.trueleap.home.MainActivity;

import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import com.app.trueleap.external.CommonFunctions;
import com.google.android.material.snackbar.Snackbar;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.app.trueleap.external.CommonFunctions.isInternetOn;
import static com.app.trueleap.external.DatabaseHelper.TABLE_FILE_UPLOAD;

public class documentuploadService extends Service {

    private Context context;
    int notify_delay = 0;
    LocalStorage localStorage;
    private static final String TAG = "BaseActivity";
    public static final String CHANNEL_ID = "AllbaziCaptain";
    public static final String PATH = "/storage/emulated/0/trueleap/upload/asssign.jpg";
    private BroadcastReceiver networkChangeReceiver;
    private boolean internetFlag = true;

    DBhelper dBhelper;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        localStorage = new LocalStorage(this);
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        this.context = this;
        dBhelper =  new DBhelper(context);

        try {
            dBhelper.createDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ConnectivityManager.NetworkCallback networkCallback = new ConnectivityManager.NetworkCallback() {
            @Override
            public void onAvailable(Network network) {
                // network available
                Log.d(TAG,"Network Available");
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        final Cursor cursor = dBhelper.getData();
                        Log.e(TAG, "Cursor Count: " + cursor.getCount());
                        if(cursor.getCount()>0) {
                            while (cursor.moveToNext()) {
                                String filePath = cursor.getString(cursor.getColumnIndex("filePath"));
                                String periodString = cursor.getString(cursor.getColumnIndex("assignmentperoid"));
                                String titleString = cursor.getString(cursor.getColumnIndex("title"));
                                String noteString = cursor.getString(cursor.getColumnIndex("note"));
                                String sectionString = cursor.getString(cursor.getColumnIndex("section"));
                                String documentString = cursor.getString(cursor.getColumnIndex("documentnumber"));
                                int id = cursor.getInt(cursor.getColumnIndex("id"));

                                Log.e(TAG, "fILE pATH: " + filePath);
                                Log.e(TAG, "Period Id Service: " + periodString);
                                Log.e(TAG, "titleString Id Service: " + titleString);
                                Log.e(TAG, "noteString Id Service: " + noteString);
                                Log.e(TAG, "sectionString Id Service: " + sectionString);
                                Log.e(TAG, "documentString Id Service: " + documentString);

                                uploadFile(id, noteString, titleString, sectionString, documentString, periodString, filePath);
                            }
                        } else {
                            stopSelf();
                        }                    }
                }, 4000);
            }

            @Override
            public void onLost(Network network) {
                // network unavailable
                Log.d(TAG,"Network Not Available");
            }
        };

        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            connectivityManager.registerDefaultNetworkCallback(networkCallback);
        } else {
            NetworkRequest request = new NetworkRequest.Builder()
                    .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET).build();
            connectivityManager.registerNetworkCallback(request, networkCallback);
        }

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        final Cursor cursor = dBhelper.getData();
        Log.e(TAG, "Cursor Count Destroy: " + cursor.getCount());
        if(cursor.getCount()>0) {
            Intent i = new Intent("RestartService");
            i.putExtra("startService", true);
            i.setClass(this, RestartService.class);
            sendBroadcast(i);
        }
    }

    @Override
    public boolean stopService(Intent name) {
        return super.stopService(name);
    }

    public void uploadFile(int id, String noteString, String titleString, String sectionString, String documentNumberString, String periodString, String imagepath) {
        String name="Manoj";
        RequestBody nameBody = RequestBody.create(MediaType.parse("text/plain"), name);
        String upload_param = localStorage.getClassId() + ":AS" + ":" + localStorage.getId();
        RequestBody coverRequestFile = null;
        MultipartBody.Part photo1 = null;
        try {
            File file1 = new File(imagepath);
            coverRequestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file1);
            photo1 = MultipartBody.Part.createFormData("file", file1.getName(), coverRequestFile);
        } catch (Exception e) {
            Log.d(TAG, "File Exception: " + e.getMessage());
        }

        if (imagepath.length() == 0) {
            Toast.makeText(context, "Please select one file", Toast.LENGTH_SHORT).show();
        } else {
            RequestBody note = RequestBody.create(MediaType.parse("text/plain"),
                    noteString);
            RequestBody title = RequestBody.create(MediaType.parse("text/plain"),
                    titleString);
            RequestBody sectionBody = RequestBody.create(MediaType.parse("text/plain"),
                    sectionString);
            RequestBody documentnumberBody = RequestBody.create(MediaType.parse("text/plain"),
                    documentNumberString);
            RequestBody uniqueperiodidBody = RequestBody.create(MediaType.parse("text/plain"),
                    periodString);
            RequestBody uploadparam = RequestBody.create(MediaType.parse("text/plain"),
                    upload_param);
            Call<ResponseBody> call = null;
            call = ApiClientFile
                    .getInstance()
                    .getApiInterface()
                    .uploadDoc(localStorage.getKeyUserToken(), photo1, title, note, uploadparam,nameBody,sectionBody,documentnumberBody,uniqueperiodidBody);
            Log.d(TAG, "khhkjhkh:" + call.request());
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        if(response.body() != null){
                            Log.d(TAG, "Response:" + response.body().string());
                        } else if(response.errorBody() != null){
                            Log.d(TAG, "Response:" + response.errorBody().string());
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (response.isSuccessful()) {
                        Log.d(TAG, "Success");
                        if (dBhelper.deleteData(id)) {
                            Log.e("BaseActivity", "Data Deleted Successfully");
                            File file = new File(imagepath);
                            if (file.delete()) {
                                Log.e("BaseActivity", "File Deleted Successfuly");
                            } else {
                                Log.e("BaseActivity", "File Not Deleted");
                            }
                        } else {
                            Log.e("BaseActivity", "Data Deletion Unsuccessfull");
                        }
                    } else {
                        Log.d(TAG, "server contact failed");
                    }
                }
                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.d(TAG, "Server Exception: " + t.getMessage());
                }
            });
        }
    }

}
