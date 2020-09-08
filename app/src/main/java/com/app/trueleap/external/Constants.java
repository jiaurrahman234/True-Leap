package com.app.trueleap.external;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;

import com.daimajia.androidanimations.library.BuildConfig;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Constants {

    public static String NO_INTERNET = "No internet connection!";
    public static String APP_LINK = "https://play.google.com/store/apps/details?id="+ BuildConfig.APPLICATION_ID;

    public static final int  PICK_IMAGE = 1001, TAKE_IMAGE = 1002, REQUEST_DOCUMENT = 1003 ;

    public static final int CAMERA_REQUEST = 1888;
    public static final int MY_CAMERA_PERMISSION_CODE = 100;

    public static final int EXCERPT_LENGTH = 120;

}
