package com.app.trueleap.external;
import com.daimajia.androidanimations.library.BuildConfig;

import java.text.SimpleDateFormat;

public class Constants {
    public static String NO_INTERNET = "No internet connection!";
    public static String APP_LINK = "https://play.google.com/store/apps/details?id="+ BuildConfig.APPLICATION_ID;
    public static final int  PICK_IMAGE = 1001, TAKE_IMAGE = 1002, REQUEST_DOCUMENT = 1003 ;
    public static final int CAMERA_REQUEST = 1888;
    public static final int MY_CAMERA_PERMISSION_CODE = 100;
    public static final int EXCERPT_LENGTH = 120;
    public static final int NOTIF_EXCERPT_LENGTH = 20;

    public static final String DOC_ASSIGNMENT= "A";
    public static final String DOC_NOTE = "N";
    public static final String PUBLIC= "public";
    public static final String PRIVATE= "private";

    public static final String READNOTIFICATION= "r";

    public static final int LANG_INDIAN = 1;
    public static final int LANG_FOREIGN = 2;

}
