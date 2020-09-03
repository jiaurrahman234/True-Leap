package com.app.trueleap.auth;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.app.trueleap.Retrofit.APIClient;
import com.app.trueleap.base.BaseViewModel;
import com.app.trueleap.model.ErrorResponse;
import java.lang.annotation.Annotation;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.HttpException;


public class AuthViewModel extends BaseViewModel {
    public MutableLiveData<LoginModel> userData = new MutableLiveData();

    public void loginUser(RequestBody data) {
        if (apiInterface == null) getApiImplementation();
        Single<LoginModel> dataObserve = apiInterface.loginUser(data);
        dataObserve.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<LoginModel>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }
                    @Override
                    public void onSuccess(LoginModel loginModel) {
                        userData.setValue(loginModel);
                        isApiSuccess.setValue(true);
                    }
                    @Override
                    public void onError(Throwable e) {
                        try{
                            ResponseBody responseBody = ((HttpException)e).response().errorBody();
                            Converter<ResponseBody, ErrorResponse> converter = APIClient.getRetrofit().responseBodyConverter(ErrorResponse.class, new Annotation[0]);
                            ErrorResponse errorObject = converter.convert(responseBody);
                            Log.d("TAG","trtrt "+errorObject.toString());
                            errorData.setValue(errorObject);
                            isApiSuccess.setValue(true);
                        }catch (Exception e1){
                            e1.printStackTrace();
                        }
                    }
                });
        }
}