package com.app.trueleap.Retrofit;

import com.app.trueleap.auth.LoginModel;
import com.app.trueleap.home.studentsubject.Subject;
import io.reactivex.Single;
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
import static com.app.trueleap.Retrofit.APIConstants.LOGIN;
import static com.app.trueleap.Retrofit.APIConstants.SUBJECTS;
import static com.app.trueleap.Retrofit.APIConstants.UPLOAD;

public interface APIInterface {

    @Headers("Content-Type: application/json")
    @POST(LOGIN)
    Call<ResponseBody> loginUser(@Body RequestBody body);

    @GET(SUBJECTS)
    Call<ResponseBody> getSubjects(@Header("Authorization") String token, @Query("ismobile") boolean ismobile);

   /* @GET(SUBJECTS)
    Single<Subject> getSubjectsData(@Header("Authorization") String token , @Query("ismobile") boolean ismobile);*/

   /* @GET(DOCUMENT)
    Single<Subject> getDocument(@Header("Authorization") String token , @Query("periodid")  String periodid , @Query("documentid") String documentid );*/

    @GET(DOCUMENT)
    Call<ResponseBody> getDocument(@Header("Authorization") String token, @Query("periodid")  String periodid , @Query("documentid") String documentid);

    @Multipart
    @POST(UPLOAD)
    Call<ResponseBody> uploadDoc(@Header("Authorization") String token,
                                 @Part MultipartBody.Part file,
                                // @Part("file\"; filename=\"pp.png\" ") RequestBody file,
                                /* @Query("title") String title,
                                 @Query("note") String note,
                                 @Query("uploadparam") String uploadparam,*/
                                 @Part("title") RequestBody title,
                                 @Part("note") RequestBody note,
                                 @Part("uploadparam") RequestBody uploadparam);

    @GET("version")
    Call<ResponseBody> getVersion();

}
