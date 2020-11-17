package com.app.trueleap.external;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;


public class LocalStorage {
    public static final String TOKEN = "user_token";
    public static final String ID = "id";
    private static final String IS_USER_LOGIN = "IsUserLoggedIn";
    private static final String USER_PHONE = "user_phone";
    private static final String ROLL_NUMBER = "ROLL_NUMBER";
    private static final String CLASS_ID = "CLASS_ID";
    private static final String SECTION_ID = "SECTION_ID";
    private static final String SEMESTER = "SEMESTER";
    private static final String AUTODOWNLOAD = "AUTODOWNLOAD";
    private static final String SELECTED_LANGUAGE = "selected_language";
    private static final String NOTIFICATION_COUNT = "notification_count";
    private static final String SECRET_QUESTION = "s_question";
    private static final String SECRET_ANSWER = "s_ans";
    private static final String IS_QUESTION_SET = "is_set";
    private static final String IS_OFFLINE_LOGGEDIN = "is_logged_offline";
    private static final String FULL_NAME = "full_name";

    private static LocalStorage instance = null;
    SharedPreferences sharedPreferences;
    Editor editor;

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

    public void createUserLoginSession(String token,String id ,String rollNumber,String phoneNumber, String fullname, String classid,String section,String semester, boolean isLogin) {
        editor = sharedPreferences.edit();
        editor.putBoolean(IS_USER_LOGIN, isLogin);
        editor.putString(TOKEN, token);
        editor.putString(ID, id);
        editor.putString(ROLL_NUMBER, rollNumber);
        editor.putString(USER_PHONE, phoneNumber);
        editor.putString(FULL_NAME, fullname);
        editor.putString(CLASS_ID, classid);
        editor.putString(SECTION_ID, section);
        editor.putString(SEMESTER, semester);
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
    public String getSemester() {
        return sharedPreferences.getString(SEMESTER, "");
    }

    public int getNotificationCount() {
        return sharedPreferences.getInt(NOTIFICATION_COUNT, 0);
    }

    public void setNotificationCount(int count) {
        editor = sharedPreferences.edit();
        editor.putInt(NOTIFICATION_COUNT, count);
        editor.commit();
    }

    public void logoutUser() {
        editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
    }

    public String getSelectedLanguage() {
        return sharedPreferences.getString(SELECTED_LANGUAGE, null);
    }

    public void setSelectedLanguage(String lang) {
        editor = sharedPreferences.edit();
        editor.putString(SELECTED_LANGUAGE, lang);
        editor.commit();
    }

    public String getSecretQuestion() {
        return sharedPreferences.getString(SECRET_QUESTION, null);
    }

    public void setSecretQuestion(String lang) {
        editor = sharedPreferences.edit();
        editor.putString(SECRET_QUESTION, lang);
        editor.commit();
    }

    public String getSecretAnswer() {
        return sharedPreferences.getString(SECRET_ANSWER, null);
    }

    public void setSecretAnswer(String lang) {
        editor = sharedPreferences.edit();
        editor.putString(SECRET_ANSWER, lang);
        editor.commit();
    }

    public boolean isQuestionSet() {
        return sharedPreferences.getBoolean(IS_QUESTION_SET, false);
    }

    public void setQuestionSet(Boolean status) {
        editor = sharedPreferences.edit();
        editor.putBoolean(IS_QUESTION_SET, status);
        editor.commit();
    }

    public boolean IsOfflineLoggedin() {
        return sharedPreferences.getBoolean(IS_OFFLINE_LOGGEDIN, false);
    }

    public void setIsOfflineLoggedin(Boolean status) {
        editor = sharedPreferences.edit();
        editor.putBoolean(IS_OFFLINE_LOGGEDIN, status);
        editor.commit();
    }
}
