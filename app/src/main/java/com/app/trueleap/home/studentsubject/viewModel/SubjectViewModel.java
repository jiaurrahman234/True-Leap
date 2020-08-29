package com.app.trueleap.home.studentsubject.viewModel;

import androidx.lifecycle.MutableLiveData;
import com.app.trueleap.base.BaseViewModel;
import com.app.trueleap.home.studentsubject.Subject;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class SubjectViewModel extends BaseViewModel {
    public MutableLiveData<Subject> subjectData = new MutableLiveData();

    public void subjectData(String token) {
        if (apiInterface == null) getApiImplementation();
        Single<Subject> dataObserve = apiInterface.getSubjectsData(token);
        dataObserve.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Subject>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(Subject data) {
                        subjectData.setValue(data);
                        isApiSuccess.setValue(true);
                    }

                    @Override
                    public void onError(Throwable e) {
                        try{
                            /*ResponseBody responseBody = ((HttpException)e).response().errorBody();
                            Converter<ResponseBody, ErrorResponse> converter = APIClient.getRetrofit().responseBodyConverter(ErrorResponse.class, new Annotation[0]);
                            ErrorResponse errorObject = converter.convert(responseBody);
                            Log.d("TAG","trtrt "+errorObject.toString());
                            errorData.setValue(errorObject);*/

                            isApiSuccess.setValue(true);
                        } catch (Exception e1){
                            e1.printStackTrace();
                        }
                    }
                });
    }
}
