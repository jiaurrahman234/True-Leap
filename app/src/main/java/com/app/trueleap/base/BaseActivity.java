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
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieProperty;
import com.airbnb.lottie.model.KeyPath;
import com.airbnb.lottie.value.LottieFrameInfo;
import com.airbnb.lottie.value.SimpleLottieValueCallback;
import com.app.trueleap.R;
import com.app.trueleap.auth.LoginActivity;
import com.app.trueleap.dialogFragment.LanguageDialogFragment;
import com.app.trueleap.external.LocalStorage;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.Task;
import com.google.gson.Gson;

public class BaseActivity extends AppCompatActivity {
    public static final String TAG = BaseActivity.class.getSimpleName();
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 10;
    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 20;
    public Gson gson;
    public LocalStorage localStorage;
    String userJson;
    ProgressDialog progressDialog;
    public Context context;
    Dialog dialog = null;
    public Double longitudeFromMap = null;
    private int REQUEST_CODE_UPDATE = 1201;
    public Double latitudeFromMap = null;
    public FragmentManager fragmentManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            gson = new Gson();
            context = getApplicationContext();
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
        Fragment LanguageDialogFragment = getSupportFragmentManager().findFragmentByTag("language_dialog");
        DialogFragment LanguageDialog = (DialogFragment) LanguageDialogFragment;
        if (LanguageDialog == null) {
            com.app.trueleap.dialogFragment.LanguageDialogFragment languageDialog = new LanguageDialogFragment();
            FragmentManager transaction = getSupportFragmentManager();
            languageDialog.setCancelable(false);
            languageDialog.show(transaction, "language_dialog");
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
            builder.setTitle("Confirm")
                    .setIcon(R.drawable.logo)
                    .setMessage("Do you really want to exit?")
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

    
}
