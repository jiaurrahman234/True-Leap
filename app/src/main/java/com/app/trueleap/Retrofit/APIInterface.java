package com.app.trueleap.Retrofit;

import com.app.trueleap.auth.LoginModel;
import com.app.trueleap.home.studentsubject.Subject;

import io.reactivex.Single;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

import static com.app.trueleap.Retrofit.APIConstants.LOGIN;
import static com.app.trueleap.Retrofit.APIConstants.SUBJECTS;

public interface APIInterface {

    @Headers("Content-Type: application/json")
    @POST(LOGIN)
    Single<LoginModel> loginUser(@Body RequestBody body);

    @GET(SUBJECTS)
    Call<ResponseBody> getSubjects(@Header("Authorization") String token, @Query("ismobile") boolean ismobile);

    @GET(SUBJECTS)
    Single<Subject> getSubjectsData(@Header("Authorization") String token , @Query("ismobile") boolean ismobile);


    @GET("version")
    Call<ResponseBody> getVersion();

}
