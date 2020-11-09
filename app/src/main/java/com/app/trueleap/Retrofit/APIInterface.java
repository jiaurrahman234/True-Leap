package com.app.trueleap.Retrofit;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import static com.app.trueleap.Retrofit.APIConstants.DOCUMENT;
import static com.app.trueleap.Retrofit.APIConstants.GLOBALSEARCH;
import static com.app.trueleap.Retrofit.APIConstants.LOGIN;
import static com.app.trueleap.Retrofit.APIConstants.NOTIFICATION;
import static com.app.trueleap.Retrofit.APIConstants.SUBJECTS;
import static com.app.trueleap.Retrofit.APIConstants.UPDATENOTIFICATION;
import static com.app.trueleap.Retrofit.APIConstants.UPLOAD;

public interface APIInterface {

    @Headers("Content-Type: application/json")
    @POST(LOGIN)
    Call<ResponseBody> loginUser(@Body RequestBody body);

    @GET(SUBJECTS)
    Call<ResponseBody> getSubjects(@Header("Authorization") String token, @Query("ismobile") boolean ismobile);

    @GET(DOCUMENT)
    Call<ResponseBody> getDocument(@Header("Authorization") String token, @Query("periodid")  String periodid , @Query("documentid") String documentid);

    @GET(GLOBALSEARCH)
    Call<ResponseBody> globalsearch(@Header("Authorization") String token, @Query("searchfor")  String searchterm );

    @Multipart
    @POST(UPLOAD)
    Call<ResponseBody> uploadDoc(@Header("Authorization") String token,
                                 @Part MultipartBody.Part file,
                                 @Part("title") RequestBody title,
                                 @Part("note") RequestBody note,
                                 @Part("uploadparam") RequestBody uploadparam,
                                 @Part("submittedby") RequestBody submittedby,
                                 @Part("section") RequestBody section,
                                 @Part("documentnumber") RequestBody documentnumber,
                                 @Part("assignmentperiod") RequestBody assignmentperiod);

    @Headers("Content-Type: application/json")
    @POST(NOTIFICATION)
    Call<ResponseBody> notification(@Header("Authorization") String token, @Body RequestBody body);

    @Headers("Content-Type: application/json")
    @POST(UPDATENOTIFICATION)
    Call<ResponseBody> updatenotification(@Header("Authorization") String token, @Body RequestBody body);

}