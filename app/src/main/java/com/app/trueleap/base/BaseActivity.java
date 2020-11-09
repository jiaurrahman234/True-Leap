package com.app.trueleap.base;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.Button;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieProperty;
import com.airbnb.lottie.model.KeyPath;
import com.airbnb.lottie.value.LottieFrameInfo;
import com.airbnb.lottie.value.SimpleLottieValueCallback;
import com.app.trueleap.R;
import com.app.trueleap.Retrofit.APIClient;
import com.app.trueleap.external.Constants;
import com.app.trueleap.external.LocalStorage;
import com.app.trueleap.interfaces.responseCallback;
import com.app.trueleap.localization.ChangeLanguageActivity;
import com.app.trueleap.notification.NotificationModel;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.Task;
import com.google.gson.Gson;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

public class BaseActivity extends AppCompatActivity {
    public static final String TAG = BaseActivity.class.getSimpleName();
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 10;
    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 20;
    public Gson gson;
    public LocalStorage localStorage;
    ProgressDialog progressDialog;
    public Context context;
    Dialog dialog = null;
    private int REQUEST_CODE_UPDATE = 1201;
    public FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            gson = new Gson();
            context = BaseActivity.this;
            fragmentManager = getSupportFragmentManager();
            localStorage = new LocalStorage(getApplicationContext());
            progressDialog = new ProgressDialog(BaseActivity.this);
            //NetworkCheck.isNetworkAvailable(getApplicationContext());

            AppUpdateManager appUpdateManager = AppUpdateManagerFactory.create(context);
            Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();

            appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {
                if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                        && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                    try {
                        Log.d(TAG,"update avail: "+appUpdateInfoTask);
                        appUpdateManager.startUpdateFlowForResult(
                                appUpdateInfo,
                                AppUpdateType.IMMEDIATE,
                                this,
                                REQUEST_CODE_UPDATE);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });
            initializeProgressDialog();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initializeProgressDialog() {
        if (dialog == null) {
            dialog = new Dialog(this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.progress_view);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            LottieAnimationView animation_view = dialog.findViewById(R.id.animation_view);
            addColorFilterToLottieView(animation_view);
        }
    }

    public void showLanguageDialog(){
        startActivity(new Intent(this, ChangeLanguageActivity.class));
    }


    private void addColorFilterToLottieView(LottieAnimationView view) {
        view.addValueCallback(
                new KeyPath("**"),
                LottieProperty.COLOR_FILTER,
                new SimpleLottieValueCallback<ColorFilter>() {
                    @Override
                    public ColorFilter getValue(LottieFrameInfo<ColorFilter> frameInfo) {
                        return new PorterDuffColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
                    }
                }
        );
    }

    public void showProgressBar() {
        if (dialog == null) {
            return;
        }
        if (!dialog.isShowing()) {
            dialog.show();
        }
    }

    public void hideProgressBar() {
        if (dialog == null) {
            return;
        }
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    public void alertTheme(AlertDialog alertDialog) {
        Button positiveBtn = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
        positiveBtn.setTextColor(getResources().getColor(R.color.colorPrimary));
        positiveBtn.setBackgroundColor(getResources().getColor(R.color.white));
        Button negativeBtn = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
        negativeBtn.setTextColor(getResources().getColor(R.color.colorPrimary));
        negativeBtn.setBackgroundColor(Color.WHITE);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
        finish();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_UPDATE) {
            if (resultCode != RESULT_OK) {
                Log.d(TAG, "Update flow failed! Result code: " + resultCode);
                // If the update is cancelled or fails,
                // you can request to start the update again.
            }
        }
    }


    public void exitApp() {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.MyAlertDialogStyle);
            builder.setTitle(R.string.confirm)
                    .setIcon(R.drawable.logo)
                    .setMessage(R.string.logout_msg)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
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
    }

    public void initToolbar() {
        Toolbar toolbar;
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar;
        actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    public void getNotifications(responseCallback responsecallback) {
        ArrayList<NotificationModel> notificationlist = new ArrayList<>();
        JSONObject userObj = new JSONObject();
        try {
            userObj.put("userid", localStorage.getId());
            RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),
                    (userObj.toString()));

            Call<ResponseBody> call = APIClient
                    .getInstance()
                    .getApiInterface()
                    .notification(localStorage.getKeyUserToken(), body);

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                    try {
                        String response_data = response.body().string();
                        JSONArray jsonArray = new JSONArray(response_data);
                        if (jsonArray.length()>0) {
                            int count=0;
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject resultObject = jsonArray.getJSONObject(i);
                                notificationlist.add(
                                        new NotificationModel(
                                                resultObject.getString("notificationid"),
                                                resultObject.getJSONObject("message").getString("note"),
                                                resultObject.getJSONObject("message").getString("document"),
                                                resultObject.getString("sentby"),
                                                resultObject.getString("date"),
                                                resultObject.getBoolean("viewed"))
                                );
                                if(!resultObject.getBoolean("viewed")){
                                    count++;
                                }
                            }
                            localStorage.setNotificationCount(count);
                        } else {
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if (responsecallback != null) {
                        responsecallback.onSuccess(notificationlist);
                    }
                }
                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void readNotifications(String notificationid) {
        JSONObject userObj = new JSONObject();
        try {
            userObj.put("userid", localStorage.getId());
            userObj.put("notificationid", notificationid);
            userObj.put("status", Constants.READNOTIFICATION);
            RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),
                    (userObj.toString()));

            Call<ResponseBody> call = APIClient
                    .getInstance()
                    .getApiInterface()
                    .updatenotification(localStorage.getKeyUserToken(), body);

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                    try {
                        String response_data = response.body().string();
                        JSONObject responseObj = new JSONObject(response_data);
                        responseObj.getString("code");
                        responseObj.getString("desc");

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    /*if (responsecallback != null) {
                        responsecallback.onSuccess(notificationlist);
                    }*/
                }
                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
