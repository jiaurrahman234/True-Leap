package com.app.trueleap.external;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;


public class LocalStorage {
    public static final String KEY_HASH = "hash";
    public static final String RECIPE_SLIDER = "recipeSlider";
    public static final String KEY_USER = "User";
    public static final String KEY_USER_ADDRESS = "user_address";
    public static final String KEY_USER_LOCATION = "user_location";
    public static final String TOKEN = "user_token";
    public static final String ID = "id";
    public static final String KEY_PREFERENCES = "preferences";
    public static final String USER_PREFERENCES = "user_preferences";
    public static final String USER_NAME = "user_name";
    public static final String USER_EMAIL = "user_email";
    public static final String SLIDER_IMAGE = "slider_image";
    public static final String ADVERTISE_IMAGE = "advertise_image";
    public static final String CATEGORY = "category";
    public static final String FAVORITE_CATEGORY = "fav_category";
    public static final String KEY_SELECTED_CITY = "user_city";
    public static final String KEY_SELECTED_CITY_ID = "user_city_id";
    public static final String PREF_LOCATION = "location_data";
    public static final String PREF_LATITUDE = "location_data_latitude";
    public static final String PREF_LONGITUDE = "location_data_longitude";
    private static final String IS_USER_LOGIN = "IsUserLoggedIn";
    private static final String USER_PIN = "userpin";
    private static final String USER_PHONE = "user_phone";
    private static final String ROLL_NUMBER = "ROLL_NUMBER";
    private static final String CLASS_ID = "CLASS_ID";
    private static final String SECTION_ID = "SECTION_ID";
    private static final String AUTODOWNLOAD = "AUTODOWNLOAD";


    private static LocalStorage instance = null;
    SharedPreferences sharedPreferences;
    Editor editor;
    int PRIVATE_MODE = 0;
    Context _context;

    public LocalStorage(Context context) {
        sharedPreferences = context.getSharedPreferences("Preferences", 0);
    }

    public static LocalStorage getInstance(Context context) {
        if (instance == null) {
            synchronized (LocalStorage.class) {
                if (instance == null) {
                    instance = new LocalStorage(context);
                }
            }
        }
        return instance;
    }
    public void setClass(String classData) {
        Editor editor = sharedPreferences.edit();
        editor.putString("class", classData);
        editor.commit();
    }

    public String getClassData() {
        if (sharedPreferences.contains("class"))
            return sharedPreferences.getString("class", null);
        else return null;
    }
    public void setAutodownload(Boolean data) {
        editor = sharedPreferences.edit();
        editor.putBoolean(AUTODOWNLOAD, data);
        editor.commit();
    }


    public Boolean getAutodownload() {
        return sharedPreferences.getBoolean(AUTODOWNLOAD, false);
    }

    public void createUserLoginSession(String token,String id ,String rollNumber,String phoneNumber,String classid,String section,boolean isLogin) {
        editor = sharedPreferences.edit();
        editor.putBoolean(IS_USER_LOGIN, isLogin);
        editor.putString(TOKEN, token);
        editor.putString(ID, token);
        editor.putString(ROLL_NUMBER, rollNumber);
        editor.putString(USER_PHONE, phoneNumber);
        editor.putString(CLASS_ID, classid);
        editor.putString(SECTION_ID, section);
        editor.commit();
    }

    public boolean isUserLoggedIn() {
        return sharedPreferences.getBoolean(IS_USER_LOGIN, false);
    }

    public String getKeyUserToken() {
        return sharedPreferences.getString(TOKEN, "");
    }

    public String getId() {
        return sharedPreferences.getString(ID, "");
    }

    public String getRollNumber() {
        return sharedPreferences.getString(ROLL_NUMBER, "");
    }

    public String getUserPhone() {
        return sharedPreferences.getString(USER_PHONE, "");
    }

    public String getClassId() {
        return sharedPreferences.getString(CLASS_ID, "");
    }

    public String getSectionId() {
        return sharedPreferences.getString(SECTION_ID, "");
    }


    public void logoutUser() {
        editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
    }



}
