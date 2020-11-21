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
import com.app.trueleap.MessagingModule.chatResponseCallback;
import com.app.trueleap.MessagingModule.model.TeacherModel;
import com.app.trueleap.MessagingModule.model.messageModel;
import com.app.trueleap.MessagingModule.notifyResponseCallback;
import com.app.trueleap.MessagingModule.teacherListResponseCallback;
import com.app.trueleap.R;
import com.app.trueleap.Retrofit.APIClient;
import com.app.trueleap.Retrofit.ApiClientChat;
import com.app.trueleap.dialogFragment.NotifiactionDialogFragment;
import com.app.trueleap.dialogFragment.sessionTimeoutFragment;
import com.app.trueleap.external.CommonFunctions;
import com.app.trueleap.external.Constants;
import com.app.trueleap.external.LocalStorage;
import com.app.trueleap.external.Utils;
import com.app.trueleap.gradebook.gradeResponseCallback;
import com.app.trueleap.gradebook.model.GradeItem;
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

                    if(response.isSuccessful()) {
                        try {
                            String response_data = response.body().string();
                            JSONArray jsonArray = new JSONArray(response_data);
                            if (jsonArray.length() > 0) {
                                int count = 0;
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
                                    if (!resultObject.getBoolean("viewed")) {
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
                    }else {
                        try {
                            String errorBody = response.errorBody().string();
                            Log.d(TAG, "error data: " + errorBody);
                            JSONObject jsonObject = new JSONObject(errorBody);
                            if(jsonObject.getString("code").equals("402-AUTH-001")){
                                showSesstionTimeout();
                            }else {

                            }
                        }catch (Exception e) {
                            e.printStackTrace();
                        }
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

    public void getchatHistory(chatResponseCallback chatresponsecallback, String subject, String peroid_id ) {
        ArrayList<messageModel> chatlist = new ArrayList<>();
        JSONObject userObj = new JSONObject();
        try {
            userObj.put("class", localStorage.getClassId());
            userObj.put("subject", subject);
            userObj.put("section", localStorage.getSectionId());
            userObj.put("semester", localStorage.getSemester());
            userObj.put("period", peroid_id);
            userObj.put("user", localStorage.getId());

            RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),
                    (userObj.toString()));

            Call<ResponseBody> call = ApiClientChat
                    .getInstance()
                    .getApiInterface()
                    .chatHistory(localStorage.getKeyUserToken(), body);

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {

                    if (response.isSuccessful()){
                        Log.d(TAG,"chat"+response.toString());
                        try {
                            String response_data = response.body().string();
                            JSONObject jsonObj = new JSONObject(response_data);
                            Log.d(TAG,"chat "+jsonObj.getBoolean("success"));
                            if (jsonObj.getBoolean("success")) {
                                JSONArray chat = jsonObj.getJSONArray("data");
                                for (int i = 0; i < chat.length(); i++) {
                                    JSONObject resultObject = chat.getJSONObject(i);
                                    chatlist.add(new messageModel(
                                            resultObject.getString("user"),
                                            resultObject.getString("chatText"))
                                    );
                                }
                            } else {
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    if (chatresponsecallback != null) {
                        chatresponsecallback.onSucceschat(chatlist);
                    }
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

    private void showSesstionTimeout() {
        sessionTimeoutFragment alert = new sessionTimeoutFragment();
        FragmentManager transaction = getSupportFragmentManager();
        alert.show(transaction, Utils.timeout_dialog_fragment);
        alert.setCancelable(false);
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

    public void getTeacherList(teacherListResponseCallback teacherListResponseCallback) {
        ArrayList<TeacherModel> teacherList = new ArrayList<>();
        try {

            Call<ResponseBody> call = APIClient
                    .getInstance()
                    .getApiInterface()
                    .getTeacher(localStorage.getKeyUserToken());

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {

                    if(response.isSuccessful()) {
                        try {
                            String response_data = response.body().string();
                            JSONArray jsonArray = new JSONArray(response_data);
                            if (jsonArray.length() > 0) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject resultObject = jsonArray.getJSONObject(i);
                                    teacherList.add(
                                            new TeacherModel(
                                                    resultObject.getString("_id"),
                                                    resultObject.getJSONObject("profile").getString("fullname") ));
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if (teacherListResponseCallback != null) {
                            teacherListResponseCallback.onSuccessteacherList(teacherList);
                        }
                    }else {
                        try {
                            String errorBody = response.errorBody().string();
                            Log.d(TAG, "error data: " + errorBody);
                            JSONObject jsonObject = new JSONObject(errorBody);
                            if(jsonObject.getString("code").equals("402-AUTH-001")){
                                showSesstionTimeout();
                            }else {

                            }
                        }catch (Exception e) {
                            e.printStackTrace();
                        }
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

    public void notifyTeacher(String teacher_id, String note , notifyResponseCallback notifyResponseCallback) {
        JSONObject userObj = new JSONObject();
        try {
            userObj.put("teacherid", teacher_id);
            JSONObject notification = new JSONObject();
            notification.put("note",note);
            notification.put("document","");
            userObj.put("notification",notification);

            JSONObject Class = new JSONObject();
            Class.put("grade",localStorage.getClassId());
            Class.put("section",localStorage.getSectionId());
            userObj.put("class",Class);
            userObj.put("notificationby", localStorage.getFullName());
            userObj.put("initiatortype", Constants.INITIATOR);

            RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),
                    (userObj.toString()));

            Call<ResponseBody> call = APIClient
                    .getInstance()
                    .getApiInterface()
                    .notify(localStorage.getKeyUserToken(), body);

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                    try {
                        String response_data = response.body().string();
                        JSONObject responseObj = new JSONObject(response_data);
                        if(responseObj.getInt("ok")==1){
                            Log.d(TAG,"success");
                            if (notifyResponseCallback != null) {
                                notifyResponseCallback.onSuccessfullNotify();
                            }
                        }
                       /* responseObj.getInt("nModified");
                        responseObj.getInt("n");*/
                    } catch (Exception e) {
                        e.printStackTrace();
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

    public void getGrades(String subject, gradeResponseCallback graderesponseCallback) {
        ArrayList<GradeItem> Gradelist = new ArrayList<>();
        try {
            JSONObject Class = new JSONObject();
            Class.put("class",localStorage.getClassId());
            Class.put("section",localStorage.getSectionId());
            Class.put("semester",localStorage.getSemester());
            Class.put("subject", subject);

            RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),
                    (Class.toString()));

            Call<ResponseBody> call = APIClient
                    .getInstance()
                    .getApiInterface()
                    .getGrades(localStorage.getKeyUserToken(),body);

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                    if(response.isSuccessful()) {
                        try {
                            String response_data = response.body().string();
                            JSONArray jsonArray = new JSONArray(response_data);
                            JSONArray gradesArray = jsonArray.getJSONObject(0).getJSONArray("grades");

                            if (gradesArray.length() > 0) {
                                for (int i = 0; i < gradesArray.length(); i++) {
                                    JSONObject resultObject = jsonArray.getJSONObject(i);
                                    /*Gradelist.add(
                                            new GradeItem(
                                                    resultObject.getString("gradetype"),
                                                    Double.parseDouble(resultObject.getString("gradeweight")),
                                                    resultObject.getString("gradename"),
                                                    resultObject.getString("compulsary"),
                                                    Double.parseDouble(resultObject.getString("compulsarypassmark")),
                                                    resultObject.getString("assessmentdate"),
                                                    Double.parseDouble(resultObject.getString("outof")),
                                                    Double.parseDouble(resultObject.getString("bestoutof")),
                                                    resultObject.getString("partofmidtermgrade"));*/
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if (graderesponseCallback != null) {
                            graderesponseCallback.onSuccesGrade(Gradelist);
                        }
                    }else {
                        try {
                            String errorBody = response.errorBody().string();
                            Log.d(TAG, "error data: " + errorBody);
                            JSONObject jsonObject = new JSONObject(errorBody);
                            if(jsonObject.getString("code").equals("402-AUTH-001")){
                                showSesstionTimeout();
                            }else {

                            }
                        }catch (Exception e) {
                            e.printStackTrace();
                        }
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
}