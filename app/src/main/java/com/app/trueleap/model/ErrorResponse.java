package com.app.trueleap.model;

import com.google.gson.annotations.SerializedName;

public class ErrorResponse {
    @SerializedName("code")
    public String code;
    @SerializedName("desc")
    public String desc;

    public ErrorResponse(String errorCode_, String errorDesc_) {
        this.code = errorCode_;
        this.desc = errorDesc_;
    }

    public String getError_code() {
        return code;
    }

    public void setError_code(String errorCode_) {
        this.code = errorCode_;
    }

    public String getError_message() {
        return desc;
    }

    public void setError_message(String errorDesc_) {
        this.desc = errorDesc_;
    }
}
