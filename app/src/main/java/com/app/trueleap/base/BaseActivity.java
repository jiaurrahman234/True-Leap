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
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieProperty;
import com.airbnb.lottie.model.KeyPath;
import com.airbnb.lottie.value.LottieFrameInfo;
import com.airbnb.lottie.value.SimpleLottieValueCallback;
import com.app.trueleap.Assignmentmodule.interfaces.uploadResponseCallback;
import com.app.trueleap.MessagingModule.chatResponseCallback;
import com.app.trueleap.MessagingModule.model.TeacherModel;
import com.app.trueleap.MessagingModule.model.messageModel;
import com.app.trueleap.MessagingModule.notifyResponseCallback;
import com.app.trueleap.MessagingModule.teacherListResponseCallback;
import com.app.trueleap.R;
import com.app.trueleap.Retrofit.APIClient;
import com.app.trueleap.dialogFragment.sessionTimeoutFragment;
import com.app.trueleap.external.Constants;
import com.app.trueleap.external.DBhelper;
import com.app.trueleap.external.LocalStorage;
import com.app.trueleap.external.Utils;
import com.app.trueleap.gradebook.gradeResponseCallback;
import com.app.trueleap.gradebook.model.GradeItem;
import com.app.trueleap.home.subject.interfaces.subjectResponseCallback;
import com.app.trueleap.home.subject.model.ClassModel;
import com.app.trueleap.interfaces.responseCallback;
import com.app.trueleap.model.ErrorResponse;
import com.app.trueleap.notification.NotificationModel;
import com.app.trueleap.service.documentuploadService;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.Task;
import com.google.gson.Gson;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Converter;
import retrofit2.Response;
import static com.app.trueleap.external.CommonFunctions.saveJSONToCache;

public class BaseActivity extends AppCompatActivity {
    public static final String TAG = BaseActivity.class.getSimpleName();
    public Gson gson;
    public LocalStorage localStorage;
    ProgressDialog progressDialog;
    public Context context;
    Dialog dialog = null;
    private int REQUEST_CODE_UPDATE = 1201;
    public FragmentManager fragmentManager;
    DBhelper dBhelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            gson = new Gson();
            context = BaseActivity.this;
            fragmentManager = getSupportFragmentManager();
            localStorage = new LocalStorage(getApplicationContext());
            progressDialog = new ProgressDialog(BaseActivity.this);

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

    private void initialiseDbHelper() {
        dBhelper = new DBhelper(this);
        try {
            dBhelper.createDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public void getNotifications(responseCallback responsecallback) {
        showProgressBar();
        ArrayList<NotificationModel> notificationlist = new ArrayList<>();
        JSONObject userObj = new JSONObject();
        try {
            userObj.put("userid", localStorage.getId());
            RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),
                    (userObj.toString()));

            Call<ResponseBody> call = APIClient
                    .getInstance(localStorage.getSelectedCountry())
                    .getApiInterface()
                    .notification(localStorage.getKeyUserToken(), body);

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                    hideProgressBar();
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
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if (responsecallback != null) {
                            responsecallback.onSuccess(notificationlist);
                        }
                    }else {
                            String errorBody = response.errorBody().toString();
                            Log.d(TAG, "error data: " + errorBody);
                            Response<?> errorResponse = response;
                            ResponseBody errorbody = errorResponse.errorBody();
                            Converter<ResponseBody, ErrorResponse> converter = APIClient.getRetrofit().responseBodyConverter(ErrorResponse.class, new Annotation[0]);
                            try {
                                ErrorResponse errorObject = converter.convert(errorbody);
                                Log.d(TAG, " Error_code : " + errorObject.getError_code());
                                if (errorObject.getError_code().equals(Constants.ERR_INVALID_TOKEN) || errorObject.getError_code().equals(Constants.ERR_SESSION_TIMEOUT)) {
                                    showSesstionTimeout();
                                } else {
                                    // CommonFunctions.showSnackView(rootlayout, errorObject.getError_message());
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                // CommonFunctions.showSnackView(rootlayout, errorObject.getError_message());
                            }
                    }
                }
                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    hideProgressBar();
                    hideProgressBar();
                    if (t instanceof IOException) {
                        Toast.makeText(context, Constants.NO_INTERNET, Toast.LENGTH_SHORT).show();
                        showSesstionTimeout();
                    }
                    else {

                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            hideProgressBar();
        }
    }

    public void getchatHistory(chatResponseCallback chatresponsecallback, String subject, String peroid_id ) {
        showProgressBar();
        ArrayList<messageModel> chatlist = new ArrayList<>();
        JSONObject userObj = new JSONObject();
        try {
            userObj.put("class", localStorage.getClassId());
            userObj.put("subject", subject);
            userObj.put("section", localStorage.getSectionId());
            userObj.put("semester", localStorage.getSemester());
            userObj.put("period", peroid_id);
            userObj.put("user", localStorage.getFullName());

            RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),
                    (userObj.toString()));

            Call<ResponseBody> call = APIClient
                    .getInstance(localStorage.getSelectedCountry())
                    .getApiInterface()
                    .chatHistory(localStorage.getKeyUserToken(), body);

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                    hideProgressBar();

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
                    } else {
                        String errorBody = response.errorBody().toString();
                        Log.d(TAG, "error data: " + errorBody);
                        Response<?> errorResponse = response;
                        ResponseBody errorbody = errorResponse.errorBody();
                        Converter<ResponseBody, ErrorResponse> converter = APIClient.getRetrofit().responseBodyConverter(ErrorResponse.class, new Annotation[0]);
                        try {
                            ErrorResponse errorObject = converter.convert(errorbody);
                            Log.d(TAG, " Error_code : " + errorObject.getError_code());
                            if (errorObject.getError_code().equals(Constants.ERR_INVALID_TOKEN) || errorObject.getError_code().equals(Constants.ERR_SESSION_TIMEOUT)) {
                                showSesstionTimeout();
                            } else {
                                // CommonFunctions.showSnackView(rootlayout, errorObject.getError_message());
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            // CommonFunctions.showSnackView(rootlayout, errorObject.getError_message());
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    hideProgressBar();
                    if (t instanceof IOException) {
                        Toast.makeText(context, Constants.NO_INTERNET, Toast.LENGTH_SHORT).show();
                        showSesstionTimeout();
                    }
                    else {

                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            hideProgressBar();
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
                    .getInstance(localStorage.getSelectedCountry())
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
                    hideProgressBar();
                    if (t instanceof IOException) {
                        Toast.makeText(context, Constants.NO_INTERNET, Toast.LENGTH_SHORT).show();
                        showSesstionTimeout();
                    }
                    else {

                    }

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getTeacherList(teacherListResponseCallback teacherListResponseCallback) {
        showProgressBar();
        ArrayList<TeacherModel> teacherList = new ArrayList<>();
        try {
            Call<ResponseBody> call = APIClient
                    .getInstance(localStorage.getSelectedCountry())
                    .getApiInterface()
                    .getTeacher(localStorage.getKeyUserToken());

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {

                    hideProgressBar();

                    if(response.isSuccessful()) {
                        try {
                            String response_data = response.body().string();
                            Log.d(TAG,"fcsef "+ response_data);
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
                        String errorBody = response.errorBody().toString();
                        Log.d(TAG, "error data: " + errorBody);
                        Response<?> errorResponse = response;
                        ResponseBody errorbody = errorResponse.errorBody();
                        Converter<ResponseBody, ErrorResponse> converter = APIClient.getRetrofit().responseBodyConverter(ErrorResponse.class, new Annotation[0]);
                        try {
                            ErrorResponse errorObject = converter.convert(errorbody);
                            Log.d(TAG, " Error_code : " + errorObject.getError_code());
                            if (errorObject.getError_code().equals(Constants.ERR_INVALID_TOKEN) || errorObject.getError_code().equals(Constants.ERR_SESSION_TIMEOUT)) {
                                showSesstionTimeout();
                            } else {
                                // CommonFunctions.showSnackView(rootlayout, errorObject.getError_message());
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            // CommonFunctions.showSnackView(rootlayout, errorObject.getError_message());
                        }
                    }
                }
                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    hideProgressBar();
                    hideProgressBar();
                    if (t instanceof IOException) {
                        Toast.makeText(context, Constants.NO_INTERNET, Toast.LENGTH_SHORT).show();
                        showSesstionTimeout();
                    }
                    else {

                    }

                }
            });
        } catch (Exception e) {
            hideProgressBar();
            e.printStackTrace();
        }
    }

    public void notifyTeacher(String teacher_id, String note , notifyResponseCallback notifyResponseCallback) {
        showProgressBar();
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
            Log.d(TAG,"teacher id " + localStorage.getFullName());
            Log.d(TAG,"teacher id " + Constants.INITIATOR);
            userObj.put("initiatortype", Constants.INITIATOR);
            Log.d(TAG, " body obj " +userObj.toString(4));

            RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),
                    (userObj.toString()));

            Call<ResponseBody> call = APIClient
                    .getInstance(localStorage.getSelectedCountry())
                    .getApiInterface()
                    .notify(localStorage.getKeyUserToken(), body);

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                    hideProgressBar();
                    if(response.isSuccessful()) {
                        try {
                            String response_data = response.body().string();
                            JSONObject responseObj = new JSONObject(response_data);
                            if (responseObj.getInt("ok") == 1) {
                                Log.d(TAG, "successfully notified");
                                if (notifyResponseCallback != null) {
                                    notifyResponseCallback.onSuccessfullNotify();
                                }
                            }
                            //responseObj.getInt("nModified");
                            //responseObj.getInt("n");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }else {
                        String errorBody = response.errorBody().toString();
                        Log.d(TAG, "error data: " + errorBody);
                        Response<?> errorResponse = response;
                        ResponseBody errorbody = errorResponse.errorBody();
                        Converter<ResponseBody, ErrorResponse> converter = APIClient.getRetrofit().responseBodyConverter(ErrorResponse.class, new Annotation[0]);
                        try {
                            ErrorResponse errorObject = converter.convert(errorbody);
                            Log.d(TAG, " Error_code : " + errorObject.getError_code());
                            if (errorObject.getError_code().equals(Constants.ERR_INVALID_TOKEN) || errorObject.getError_code().equals(Constants.ERR_SESSION_TIMEOUT)) {
                                showSesstionTimeout();
                            } else {
                                // CommonFunctions.showSnackView(rootlayout, errorObject.getError_message());
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            // CommonFunctions.showSnackView(rootlayout, errorObject.getError_message());
                        }
                    }
                }
                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    hideProgressBar();
                    if (t instanceof IOException) {
                        Toast.makeText(context, Constants.NO_INTERNET, Toast.LENGTH_SHORT).show();
                        showSesstionTimeout();
                    }
                    else {

                    }
                }
            });

        } catch (Exception e) {
            hideProgressBar();
            e.printStackTrace();

        }
    }

    public void getGrades(String subject, gradeResponseCallback graderesponseCallback) {
        showProgressBar();
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
                    .getInstance(localStorage.getSelectedCountry())
                    .getApiInterface()
                    .getGrades(localStorage.getKeyUserToken(),body);

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {

                    hideProgressBar();
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
                        String errorBody = response.errorBody().toString();
                        Log.d(TAG, "error data: " + errorBody);
                        Response<?> errorResponse = response;
                        ResponseBody errorbody = errorResponse.errorBody();
                        Converter<ResponseBody, ErrorResponse> converter = APIClient.getRetrofit().responseBodyConverter(ErrorResponse.class, new Annotation[0]);
                        try {
                            ErrorResponse errorObject = converter.convert(errorbody);
                            Log.d(TAG, " Error_code : " + errorObject.getError_code());
                            if (errorObject.getError_code().equals(Constants.ERR_INVALID_TOKEN) || errorObject.getError_code().equals(Constants.ERR_SESSION_TIMEOUT)) {
                                showSesstionTimeout();
                            } else {
                                // CommonFunctions.showSnackView(rootlayout, errorObject.getError_message());
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            // CommonFunctions.showSnackView(rootlayout, errorObject.getError_message());
                        }
                    }
                }
                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    hideProgressBar();
                    if (t instanceof IOException) {
                        Toast.makeText(context, Constants.NO_INTERNET, Toast.LENGTH_SHORT).show();
                        showSesstionTimeout();
                    }
                    else {

                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            hideProgressBar();
        }
    }

    public void getclasses(subjectResponseCallback subjectResponseCallback) {
        ArrayList<ClassModel> Subjects = new ArrayList<>();
        try {
            showProgressBar();
            Call<ResponseBody> call = APIClient
                    .getInstance(localStorage.getSelectedCountry())
                    .getApiInterface()
                    .getSubjects(localStorage.getKeyUserToken(), true);

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                    try {
                        Log.d(TAG, "call request: " + call.request());
                        hideProgressBar();
                        if (response.isSuccessful()) {
                            String response_data = response.body().string();
                            saveJSONToCache(BaseActivity.this, response_data);
                            JSONArray jsonArray = new JSONArray(response_data);
                            Log.d(TAG, "subject response: " + jsonArray.length());
                            if (jsonArray.length() > 0) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONArray json_array_at_level_1 = jsonArray.getJSONArray(i);
                                    if (json_array_at_level_1.length() > 0) {
                                        JSONObject classJsonObject = json_array_at_level_1.getJSONObject(0);

                                        ArrayList<String> daysArraylist = new ArrayList<>();
                                        JSONArray days = classJsonObject.getJSONArray("days");
                                        for (int j = 0; j < days.length(); j++) {
                                            daysArraylist.add(days.getString(j));
                                        }

                                        Subjects.add(new ClassModel(classJsonObject.getString("uniqueperiodid"),
                                                classJsonObject.getString("teacher"),
                                                classJsonObject.getString("uniqueteacherid"),
                                                classJsonObject.getString("startdate"),
                                                classJsonObject.getString("enddate"),
                                                classJsonObject.getString("starttime"),
                                                classJsonObject.getString("endtime"), daysArraylist,
                                                classJsonObject.getString("class"),
                                                classJsonObject.getString("section"),
                                                classJsonObject.getString("subject"), null, null));
                                    }
                                }
                            }
                            if (subjectResponseCallback != null) {
                                subjectResponseCallback.onSuccesSubject(Subjects);
                            }
                        } else {
                            String errorBody = response.errorBody().toString();
                            Log.d(TAG, "error data: " + errorBody);
                            Response<?> errorResponse = response;
                            ResponseBody errorbody = errorResponse.errorBody();
                            Converter<ResponseBody, ErrorResponse> converter = APIClient.getRetrofit().responseBodyConverter(ErrorResponse.class, new Annotation[0]);
                            try {
                                ErrorResponse errorObject = converter.convert(errorbody);
                                Log.d(TAG, " Error_code : " + errorObject.getError_code());
                                if (errorObject.getError_code().equals(Constants.ERR_INVALID_TOKEN) || errorObject.getError_code().equals(Constants.ERR_SESSION_TIMEOUT)) {
                                    showSesstionTimeout();
                                } else {
                                  // CommonFunctions.showSnackView(rootlayout, errorObject.getError_message());
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                // CommonFunctions.showSnackView(rootlayout, errorObject.getError_message());
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        hideProgressBar();
                        // CommonFunctions.showSnackView(rootlayout, errorObject.getError_message());
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                    hideProgressBar();
                    if (t instanceof IOException) {
                        Toast.makeText(context, Constants.NO_INTERNET, Toast.LENGTH_SHORT).show();
                        showSesstionTimeout();
                    }
                    else {

                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void uploadFile(uploadResponseCallback uploadResponseCallback, String imagepath , String document_id , String period_id ) {

        initialiseDbHelper();

        RequestBody nameBody = RequestBody.create(MediaType.parse("text/plain"), localStorage.getFullName());
        String upload_param = localStorage.getClassId() + ":AS" + ":" + localStorage.getId();
        RequestBody RequestBodyFile = null;
        MultipartBody.Part assignment_file = null;
        File file = new File(imagepath);
        RequestBodyFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        assignment_file = MultipartBody.Part.createFormData("file", file.getName(), RequestBodyFile);
        if (imagepath.length() == 0) {
            if (uploadResponseCallback != null) {
                uploadResponseCallback.uploadresponse(getResources().getString(R.string.select_one_file));
            }
        } else {
            RequestBody note = RequestBody.create(MediaType.parse("text/plain"),
                    "");
            RequestBody title = RequestBody.create(MediaType.parse("text/plain"),
                    "");
            RequestBody sectionBody = RequestBody.create(MediaType.parse("text/plain"),
                    localStorage.getSectionId());
            RequestBody documentnumberBody = RequestBody.create(MediaType.parse("text/plain"),
                    document_id);
            RequestBody uniqueperiodidBody = RequestBody.create(MediaType.parse("text/plain"),
                    period_id);
            RequestBody uploadparam = RequestBody.create(MediaType.parse("text/plain"),
                    upload_param);

            showProgressBar();
            Call<ResponseBody> call = null;
            call =   APIClient
                    .getInstance(localStorage.getSelectedCountry())
                    .getApiInterface()
                    .uploadDoc(localStorage.getKeyUserToken(), assignment_file, title, note, uploadparam, nameBody, sectionBody, documentnumberBody, uniqueperiodidBody);

            Log.d(TAG, "request:" + call.request());
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    hideProgressBar();
                    Log.d(TAG, "log response:" + response.toString());
                        if (response.isSuccessful()) {
                            dBhelper.insertData("", "", upload_param, localStorage.getFullName(), localStorage.getSectionId(), document_id, period_id, imagepath,1);
                            Log.d(TAG, "server contact failed");
                            String errorBody = response.body().toString();
                            Log.d(TAG, "error data: " + errorBody);
                            Response<?> errorResponse = response;
                            ResponseBody errorbody = errorResponse.errorBody();
                            Converter<ResponseBody, ErrorResponse> converter = APIClient.getRetrofit().responseBodyConverter(ErrorResponse.class, new Annotation[0]);
                            try {
                                ErrorResponse errorObject = converter.convert(errorbody);
                                Log.d(TAG, " Error_code : " + errorObject.getError_code());
                                Log.d(TAG, " Error_message : " + errorObject.getError_message());
                                if (uploadResponseCallback != null) {
                                        uploadResponseCallback.uploadresponse(errorObject.getError_message());
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                if (uploadResponseCallback != null) {
                                    uploadResponseCallback.uploadresponse(getResources().getString(R.string.uploaded_successfully));
                                }
                            }
                        }
                        else {
                        dBhelper.insertData("", "", upload_param, localStorage.getFullName(), localStorage.getSectionId(), document_id, period_id, imagepath,0 );

                        Log.d(TAG, "server contact failed");
                        String errorBody = response.errorBody().toString();
                        Log.d(TAG, "error data: " + errorBody);
                        Response<?> errorResponse = response;
                        ResponseBody errorbody = errorResponse.errorBody();
                        Converter<ResponseBody, ErrorResponse> converter = APIClient.getRetrofit().responseBodyConverter(ErrorResponse.class, new Annotation[0]);
                        try {
                            ErrorResponse errorObject = converter.convert(errorbody);
                            Log.d(TAG, " Error_code : " + errorObject.getError_code());
                            Log.d(TAG, " Error_message : " + errorObject.getError_message());
                            if (errorObject.getError_code().equals(Constants.ERR_INVALID_TOKEN) || errorObject.getError_code().equals(Constants.ERR_SESSION_TIMEOUT)) {
                                showSesstionTimeout();
                            } else {
                                if (uploadResponseCallback != null) {
                                    uploadResponseCallback.uploadresponse(errorObject.getError_message());
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            if (uploadResponseCallback != null) {
                                uploadResponseCallback.uploadresponse(getResources().getString(R.string.upload_failed));
                            }
                        }
                    }
                }
                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.e(TAG, "error internet");
                    hideProgressBar();
                    if (uploadResponseCallback != null) {
                        uploadResponseCallback.uploadresponse(getResources().getString(R.string.upload_failed));
                    }
                    if (localStorage.getAutoupload()) {
                        dBhelper.insertData("", "", upload_param, localStorage.getFullName(), localStorage.getSectionId(), document_id, period_id, imagepath, 0 );
                        startService(document_id);
                    }
                }
            });
        }
    }

    public void startService(String document_id){
        Log.d(TAG,"starting Service");
        Intent upload = new Intent(context, documentuploadService.class);
        upload.putExtra("document_id",document_id);
        context.startService(upload);
    }

    public void showSesstionTimeout() {

        Fragment timeout_dialog = getSupportFragmentManager().findFragmentByTag( Utils.timeout_dialog_fragment);
        DialogFragment timeoutDialog = (DialogFragment) timeout_dialog;
        if (timeoutDialog != null
                && timeoutDialog.getDialog() != null
                && timeoutDialog.getDialog().isShowing()
                && !timeoutDialog.isRemoving()) {

        } else {
            Log.d(TAG, "session timeout !");
            sessionTimeoutFragment alert = new sessionTimeoutFragment();
            FragmentManager transaction = getSupportFragmentManager();
            alert.show(transaction, Utils.timeout_dialog_fragment);
            alert.setCancelable(false);
        }
    }
}