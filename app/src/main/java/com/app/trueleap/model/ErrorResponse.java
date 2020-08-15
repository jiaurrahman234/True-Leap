package com.app.trueleap.model;

import com.google.gson.annotations.SerializedName;

public class ErrorResponse {
    @SerializedName("errorCode_")
    public String errorCode_;
    @SerializedName("errorDesc_")
    public String errorDesc_;

    public ErrorResponse(String errorCode_, String errorDesc_) {
        this.errorCode_ = errorCode_;
        this.errorDesc_ = errorDesc_;
    }

    public String getError_code() {
        return errorCode_;
    }

    public void setError_code(String errorCode_) {
        this.errorCode_ = errorCode_;
    }

    public String getError_message() {
        return errorDesc_;
    }

    public void setError_message(String errorDesc_) {
        this.errorDesc_ = errorDesc_;
    }
}
