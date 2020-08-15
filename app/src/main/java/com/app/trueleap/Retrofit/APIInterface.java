package com.app.trueleap.Retrofit;

import com.app.trueleap.auth.LoginModel;

import io.reactivex.Single;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

import static com.app.trueleap.Retrofit.APIConstants.LOGIN;

public interface APIInterface {

    @Headers("Content-Type: application/json")
    @POST(LOGIN)
    Single<LoginModel> loginUser(@Body RequestBody body);



    @GET("version")
    Call<ResponseBody> getVersion();

}
