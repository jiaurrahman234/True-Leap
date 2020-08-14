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
    public static final String KEY_USER_TOKEN = "user_token";
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
    private static final String ACTIVITYCALLED = "activityCalled";


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

    public void createUserLoginSession(String user,boolean isLogin) {
        editor = sharedPreferences.edit();
        editor.putBoolean(IS_USER_LOGIN, isLogin);
        editor.putString(KEY_USER, user);
        editor.commit();
    }
    public void setUserToken(String token) {
        Editor editor = sharedPreferences.edit();
        editor.putString(KEY_USER_TOKEN, token);
        editor.commit();
    }
    public String getKeyUserToken() {
        return sharedPreferences.getString(KEY_USER_TOKEN, "");
    }

    public void setUserPin(String token) {
        Editor editor = sharedPreferences.edit();
        editor.putString(USER_PIN, token);
        editor.commit();
    }
    public String getUserPin() {
        return sharedPreferences.getString(USER_PIN, "");
    }

    public void setUserPhone(String userPhone) {
        Editor editor = sharedPreferences.edit();
        editor.putString(USER_PHONE, userPhone);
        editor.commit();
    }
    public String getUserPhone() {
        return sharedPreferences.getString(USER_PHONE, "");
    }

    public void setActivitycalled(boolean activityCalled) {
        Editor editor = sharedPreferences.edit();
        editor.putBoolean(ACTIVITYCALLED, activityCalled);
        editor.commit();
    }
    public boolean getActivitycalled() {
        return sharedPreferences.getBoolean(ACTIVITYCALLED, false);
    }

    public String getUserLogin() {
        return sharedPreferences.getString(KEY_USER, "");
    }

    public void logoutUser() {
        editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
    }

    public boolean checkLogin() {
        // Check login status
        return !this.isUserLoggedIn();
    }

    public String getKeySelectedCity() {
        return sharedPreferences.getString(KEY_SELECTED_CITY, "");
    }
    public int getKeySelectedCityID() {
        return sharedPreferences.getInt(KEY_SELECTED_CITY_ID, 0);
    }

    public void setKeySelectedCity(String SelectedCity) {
        Editor editor = sharedPreferences.edit();
        editor.putString(KEY_SELECTED_CITY, SelectedCity);
        editor.commit();
    }

    public void setKeySelectedCityID(int SelectedCityID) {
        Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_SELECTED_CITY_ID, SelectedCityID);
        editor.commit();
    }
    public void addLocationData(String locationData,Double latitude, Double longitude) {
        Editor editor = sharedPreferences.edit();
        editor.putString(PREF_LOCATION, locationData).putLong(PREF_LATITUDE, (new Double(latitude)).longValue())
                .putLong(PREF_LONGITUDE, (new Double(longitude)).longValue()).apply();
    }

    public String getLocationDataAddress() {
        if (sharedPreferences.contains(PREF_LOCATION))
            return sharedPreferences.getString(PREF_LOCATION, null);
        else return null;
    }

    public boolean isUserLoggedIn() {
        return sharedPreferences.getBoolean(IS_USER_LOGIN, false);
    }

    public String getUserAddress() {
        if (sharedPreferences.contains(KEY_USER_ADDRESS))
            return sharedPreferences.getString(KEY_USER_ADDRESS, null);
        else return null;
    }

    public void setUserAddress(String user_address) {
        Editor editor = sharedPreferences.edit();
        editor.putString(KEY_USER_ADDRESS, user_address);
        editor.commit();
    }

    public String getUserLocation() {
        if (sharedPreferences.contains(KEY_USER_LOCATION))
            return sharedPreferences.getString(KEY_USER_LOCATION, null);
        else return null;
    }

    public void setUserLocation(String user_address) {
        editor = sharedPreferences.edit();
        editor.putString(KEY_USER_LOCATION, user_address);
        editor.commit();
    }

    public String getCart() {
        if (sharedPreferences.contains("CART"))
            return sharedPreferences.getString("CART", null);
        else return null;
    }


    public void setCart(String cart) {
        Editor editor = sharedPreferences.edit();
        editor.putString("CART", cart);
        editor.commit();
    }

    public void deleteCart() {
        Editor editor = sharedPreferences.edit();
        editor.remove("CART");
        editor.commit();
    }

    public String getOrder() {
        if (sharedPreferences.contains("ORDER"))
            return sharedPreferences.getString("ORDER", null);
        else return null;
    }

    public void setOrder(String order) {
        Editor editor = sharedPreferences.edit();
        editor.putString("ORDER", order);
        editor.commit();
    }

    public void deleteOrder() {
        Editor editor = sharedPreferences.edit();
        editor.remove("ORDER");
        editor.commit();
    }


}
