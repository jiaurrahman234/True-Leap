package com.app.trueleap.external;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;


public class CommonFunctions {
    public static String NO_INTERNET = "No Internet";

    public static void showSnackView(View view, String message) {
        Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show();
    }

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

    public static String dateFormat(String dateString) {
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

    public static String dateMonth(String dateString) {
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

    public static void showToast(Context context) {
        Toast.makeText(context, NO_INTERNET, Toast.LENGTH_SHORT).show();
    }
}
