package com.app.trueleap.external;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


public class DBhelper extends SQLiteOpenHelper {

    private static String DB_NAME = "assignment.db";
    private SQLiteDatabase db;
    private final Context context;
    private String DB_PATH;

    public DBhelper(Context context) {
        super(context, DB_NAME, null, 1);
        this.context = context;
        Log.e("BaseActivity", "Package Name: " + context.getPackageName());
        DB_PATH = "/data/data/" + context.getPackageName() + "/" + "databases/";
    }

    public void createDataBase() throws IOException {

        boolean dbExist = checkDataBase();
        if (dbExist) {

        } else {
            this.getReadableDatabase();
            try {
                copyDataBase();
            } catch (IOException e) {
                Log.e("BaseActivity", "Helper Create Database Exception:" + e.getMessage());
//                throw new Error("Error copying database");
            }
        }

    }

    private boolean checkDataBase() {
        File dbFile = new File(DB_PATH + DB_NAME);
        return dbFile.exists();
    }

    private void copyDataBase() throws IOException {

        File databaseFile = new File(context.getFilesDir().getAbsolutePath()
                .replace("files", "databases"));

        // check if databases folder exists, if not create it.
        if (!databaseFile.exists()){
            Log.e("BaseActivity", "Database Folder Dont EXIST");
            databaseFile.mkdir();
        }

        InputStream myInput = context.getAssets().open(DB_NAME);
        String outFileName = DB_PATH + DB_NAME;
        OutputStream myOutput = new FileOutputStream(outFileName);
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }

        // Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();

    }

    public Cursor getData() {
        String myPath = DB_PATH + DB_NAME;
        db = SQLiteDatabase.openDatabase(myPath, null,
                SQLiteDatabase.OPEN_READONLY);
        Cursor c = db.rawQuery("SELECT * FROM assignment_status", null);
        // Note: Master is the one table in External db. Here we trying to access the records of table from external db.
        return c;
    }

    public void insertData(String title, String note, String uploadParam, String submittedBy, String section, String documentnumber, String assignmentperiod, String filePath) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("title", title);
        contentValues.put("note", note);
        contentValues.put("uploadparam", uploadParam);
        contentValues.put("submittedby", submittedBy);
        contentValues.put("section", section);
        contentValues.put("documentnumber", documentnumber);
        contentValues.put("assignmentperoid", assignmentperiod);
        contentValues.put("filePath", filePath);
        try {
            long i = db.insertOrThrow("assignment_status", null, contentValues);
            if (i < 0) {
                Log.e("BaseActivity", "Insert Not Successfull");
            } else {
                Log.e("BaseActivity", "Insert Successfull");
            }
        } catch (Exception e) {
            Log.e("BaseActivity", "Insert Exception: " + e.getMessage());
        }

    }

    public boolean deleteData(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int i = db.delete("assignment_status", "id = ? ", new String[] { Integer.toString(id) });
        if (i < 0) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onCreate(SQLiteDatabase arg0) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
    }

}
