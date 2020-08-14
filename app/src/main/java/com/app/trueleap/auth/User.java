package com.app.trueleap.auth;


public class User {
    String id;
    String name;
    String email;
    String mobile;
    String device_id;
    String referral_code;

    public User() {
    }

    public User(String id, String name, String email, String mobile, String device_id , String referral_code) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.mobile = mobile;
        this.device_id = device_id;
        this.referral_code = referral_code;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public String getReferral_code() {
        return referral_code;
    }

    public void setReferral_code(String referral_code) {
        this.referral_code = referral_code;
    }
}
