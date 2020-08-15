package com.app.trueleap.base;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.app.trueleap.Retrofit.APIClient;
import com.app.trueleap.Retrofit.APIInterface;
import com.app.trueleap.model.ErrorResponse;

public class BaseViewModel extends ViewModel {
    public APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);

    public MutableLiveData<Boolean> isApiSuccess = new MutableLiveData();
    public MutableLiveData<ErrorResponse> errorData = new MutableLiveData();

    public void getApiImplementation() {
        apiInterface = APIClient.getClient().create(APIInterface.class);
    }
}