package com.app.trueleap.external;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import okhttp3.ResponseBody;

import com.app.trueleap.R;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

public class CommonFunctions {
    public static String NO_INTERNET = "No Internet";
    private static String CACHE_FILE = "classes.srl";
    final static long MAX_FILE_AGE = 1000 * 60 * 60 * 24 * 2;
    public static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

    public static void showSnackView(View view, String message) {
        Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show();
    }

    public static String getResponseData(int code, Context context) {
        String response = "";
        try {

            JSONObject jsonObject = loadAssetsJsonObj("response_code.json", context);
            JSONObject jsonObject1 = jsonObject.getJSONObject("registermsisdn");

            Iterator<String> iter = jsonObject1.keys();
            while (iter.hasNext()) {
                String key = iter.next();
                if (key.trim().equalsIgnoreCase(Integer.toString(code))) {
                    JSONObject jsonObject2 = jsonObject1.getJSONObject(key.trim());
                    response = jsonObject2.getString("resDesc");
                    return response;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    public static void saveJSONToCache(Activity context, String json){
        // Instantiate a JSON object from the request response
        try {
            // Save the JSONObject
            ObjectOutput out = null;
            out = new ObjectOutputStream(new FileOutputStream(new File(context.getCacheDir(),"")+ CACHE_FILE));

            out.writeObject( json );
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static  JSONArray getJSONFromCache(Activity context){
        // Load in an object
        try {
            ObjectInputStream in = null;
            File cacheFile = new File(new File(context.getCacheDir(),"")+ CACHE_FILE);
            in = new ObjectInputStream(new FileInputStream(cacheFile));
            String jsonArrayRaw = (String) in.readObject();
            in.close();

            //If the cache is not outdated
            if (cacheFile.lastModified()+ MAX_FILE_AGE > System.currentTimeMillis())
                return new JSONArray(jsonArrayRaw);
            else
                return null;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;

    }

    public static String calenderView (String date_to_parse){

        String datepattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

        SimpleDateFormat dateFormat = new SimpleDateFormat(datepattern);

        String formatted_date="";
        try {
            Date inputdateFormat = dateFormat.parse(date_to_parse);
            formatted_date = simpleDateFormat.format(inputdateFormat);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return formatted_date;
    }

    public static String parse_date_time (String date_to_parse){

        String datepattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
        String display_datepattern = "dd-MM-yyyy 'at' hh:mm a";

        SimpleDateFormat dateFormat = new SimpleDateFormat(datepattern);
        SimpleDateFormat display_dateFormat = new SimpleDateFormat(display_datepattern);

        String formatted_date="";
        try {
            Date inputdateFormat = dateFormat.parse(date_to_parse);
            formatted_date = display_dateFormat.format(inputdateFormat);
        } catch (Exception e) {
            e.printStackTrace();
        }

     /* DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH);
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd-MM-yyy 'at' HH:mm", Locale.ENGLISH);

        LocalDate date = LocalDate.parse(date_to_parse, inputFormatter);
        String formattedDate = outputFormatter.format(date);*/

        return formatted_date;
    }


    public static String parse_date (String date_to_parse){

        if(date_to_parse!=null) {

            String datepattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
            String display_datepattern = "dd-MM-yyyy";

            SimpleDateFormat dateFormat = new SimpleDateFormat(datepattern);
            SimpleDateFormat display_dateFormat = new SimpleDateFormat(display_datepattern);

            String formatted_date = "";
            try {
                Date inputdateFormat = dateFormat.parse(date_to_parse);
                formatted_date = display_dateFormat.format(inputdateFormat);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return formatted_date;
        }else {
            return  "";
        }


    }

    public static Date getdateValue(String date_to_parse){
    SimpleDateFormat DateFor = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        Date date = new Date();
        try{
        date = DateFor.parse(date_to_parse);
        }catch (ParseException e) {e.printStackTrace();}
     return  date;
    }

    public static boolean writeResponseBodyToDisk(ResponseBody body , String doc_id , String file_name , String mime_type) {
        try {
            File billFile;
            File billDirectory = new File(Environment.getExternalStorageDirectory().getPath() + File.separator + "trueleap");
            if(!billDirectory.isDirectory()) {
                billDirectory.mkdir();
            }

            billFile  = new File(Environment.getExternalStorageDirectory().getPath() + File.separator + "trueleap" +File.separator+doc_id+"_"+file_name);

            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                byte[] fileReader = new byte[4096];

                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;

                inputStream = body.byteStream();
                outputStream = new FileOutputStream(billFile);

                while (true) {
                    int read = inputStream.read(fileReader);

                    if (read == -1) {
                        break;
                    }

                    outputStream.write(fileReader, 0, read);

                    fileSizeDownloaded += read;

                    Log.d("TAG", "file download: " + fileSizeDownloaded + " of " + fileSize);
                }

                outputStream.flush();

                return true;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }

                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {

            e.printStackTrace();
            return false;
        }
    }

    public static boolean hasPermissionToDownload(final Activity context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M ||
                ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED )
            return true;

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(R.string.download_permission_explaination);
        builder.setPositiveButton(R.string.download_permission_grant, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // Fire off an async request to actually get the permission
                // This will show the standard permission request dialog UI
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                    context.requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
        return false;
    }

    /*public void store_data(){
        try {
            // Save the JSONObject
            ObjectOutput out = null;
            out = new ObjectOutputStream(new FileOutputStream(new File(context.getCacheDir(),"")+ CACHE_FILE));
            out.writeObject(productsInCart);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

   private retrivedata<CartProduct> retrieveCart(){
        try {
            // Load in an object
            ObjectInputStream in = null;
            File cacheFile = new File(new File(context.getCacheDir(),"")+ CACHE_FILE);
            in = new ObjectInputStream(new FileInputStream(cacheFile));
            ArrayList<CartProduct> productsList = (ArrayList<CartProduct>) in.readObject();
            in.close();

            return productsList;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
*/

    public static JSONObject loadAssetsJsonObj(String fileName, Context context) {
        String json = null;
        JSONObject array = null;
        try {
            InputStream is = context.getAssets().open(fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                array = new JSONObject(json);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return array;
    }

    public static JSONArray loadAssetsJsonArray(String fileName, Context context) {
        String json = null;
        JSONArray array = null;
        try {
            InputStream is = context.getAssets().open(fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                array = new JSONArray(json);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return array;
    }

    public static void loadAnimation(View view) {
        YoYo.with(Techniques.ZoomIn)
                .duration(400)
                .repeat(0)
                .playOn(view);
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

    public static String getMimeType(String url) {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        return type;
    }

    public static String getExtensionType(String mime_type) {
        String extension = null;
        if (mime_type != null) {
            extension = MimeTypeMap.getSingleton().getExtensionFromMimeType(mime_type);
        }
        return extension;
    }
}
