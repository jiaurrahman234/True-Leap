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
    public static int FILTER_CALL_BACK = 1;
    public static int PIN_CALL_BACK = 2;
    public static String MIN_VALUE = "min_value";
    public static String MAX_VALUE = "max_value";
    public static String CATEGORY_ID = "category_id";
    public static String PRODUCT_LIST = "product_list";
    public static String POPULAR_PRODUCT = "popular_product";
    public static String NEW_PRODUCT = "new_product";
    public static String ALL_PRODUCT = "all_product";
    public static String CATEGORY_NAME = "category_name";
    public static String NO_CART_ITEM = "No Cart item";
    public static String GO_TO_CART = "Go To Cart";
    public static String ADD_TO_CART = "Add To Cart";
    public static int SELECT_ITEM = 2;
    public static String LOCATION_NAME = "location_name";
    public static String LONGITUDE = "longitude_data";
    public static String LATITUDE = "latitude_data";
    public static String IS_EDIT = "is_edit";
    public static String HOUSE_NO = "house_no";
    public static String LANDMARK = "landmark";
    public static String PIN_NUMBER = "pin_number";
    public static String COUPON_DISCOUNT = "coupon_discount";
    public static String IS_COUPON_APPLY = "coupon_discount";
    public static String REDUCED_AMOUNT = "reduced_amount";
    public static String DISCOUNTED_PRICE = "discount_price";
    public static String COUPON_ID = "coupon_id";
    public static String BUNDLE_DATA = "bundle_data";
    public static String NO_INTERNET = "No internet connection!";
    public static String APP_LINK = "https://play.google.com/store/apps/details?id="+ BuildConfig.APPLICATION_ID;
    public static int CART_COUPON_APPLY_REQUEST_CODE = 9090;


    public static final int  PICK_IMAGE = 1001, TAKE_IMAGE = 1002, REQUEST_DOCUMENT = 1003 ;

    public static final int CAMERA_REQUEST = 1888;
    public static final int MY_CAMERA_PERMISSION_CODE = 100;

    public static void loadAnimation(View view) {
        YoYo.with(Techniques.ZoomIn)
                .duration(400)
                .repeat(0)
                .playOn(view);
        //view.animation = AnimationUtils.loadAnimation(view.context, R.anim.popup_show)
    }

    public static boolean isInternetOn(Context mContext) {
        ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean NisConnected = activeNetwork != null && activeNetwork.isConnected();
        if (NisConnected) {
            //  if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE || activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                return true;
            } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
                return true;
            else
                return false;
        }
        return false;
    }

    public static String dateFormat(String dateString){
        Date date = null;
        String date_s;
        try {
            SimpleDateFormat simpleDateFormatInput = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
            date = simpleDateFormatInput.parse(dateString);
            date_s = simpleDateFormat.format(date);

        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            date_s = dateString;
        }
        return date_s;
    }

    public static String dateMonth(String dateString){
        Date date = null;
        String date_s;
        try {
            SimpleDateFormat simpleDateFormatInput = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd  MMM");
            date = simpleDateFormatInput.parse(dateString);
            date_s = simpleDateFormat.format(date);

        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            date_s = dateString;
        }
        return date_s;
    }
}
