package com.app.trueleap.external;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

    DatabaseHelper databaseHelper;
    private static final String LOG = "DatabaseHelper";
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "TRUELEAP";
    public static final String TABLE_FILE_UPLOAD = "TABLE_FILE_UPLOAD";
    public static final String FCM_TOKEN = "FCM_TOKEN";


    private static final String CREATE_TABLE_FCM = "CREATE TABLE " + TABLE_FILE_UPLOAD + "("
            + FCM_TOKEN + " TEXT " + ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_FCM);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean saveInLocalStorage(String Table_Name, ContentValues values) {
        Log.d("ddfd", "table data: " + values.get(FCM_TOKEN) + " " + Table_Name);
        try {
            long id = -1;
            SQLiteDatabase db = this.getWritableDatabase();
            id = db.insertWithOnConflict(Table_Name, null, values, SQLiteDatabase.CONFLICT_REPLACE);
            if (id == -1) {
                Log.d("Data Inserted Failed :", " On Table " + Table_Name);
                return false;
            } else {
                Log.d("Data record Inserted :", " On Table " + Table_Name);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    public String getFcmToken(String TABLE_NAME) {
        Cursor cursor = null;
        String data = "";
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            String selectQuery = "SELECT  * FROM " + TABLE_NAME;
            cursor = db.rawQuery(selectQuery, null);
            //cursor = SQLiteDatabaseInstance_.rawQuery("SELECT EmployeeName FROM Employee WHERE EmpNo=?", new String[] {empNo + ""});
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                data = cursor.getString(cursor.getColumnIndex(FCM_TOKEN));
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }


}