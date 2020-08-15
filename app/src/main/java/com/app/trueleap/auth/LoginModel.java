package com.app.trueleap.auth;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class LoginModel {

    @SerializedName("token")
    public String token;
    @SerializedName("id")
    public String id;
    @SerializedName("profile")
    public ProfileData profile;
    @SerializedName("status")
    public Status status;
    @SerializedName("category")
    public String category;

    public class ProfileData{
        @SerializedName("address")
        public AddressData address;
        public class AddressData{
            @SerializedName("zipcode")
            public String zipcode;
            @SerializedName("address")
            public String address;
            @SerializedName("state")
            public String state;
        }

        @SerializedName("class")
        public ArrayList<ClassData> classDataArrayList = null;
        public class ClassData{
            @SerializedName("classid")
            public String classid;
            @SerializedName("section")
            public String section;
        }

        @SerializedName("period")
        public ArrayList<PeriodData> periodDataArrayList = null;
        public class PeriodData{

        }

        @SerializedName("rollNumber")
        public String rollNumber;
        @SerializedName("phoneNumber")
        public String phoneNumber;
    }

    public class Status{
        @SerializedName("profilestat")
        public String profilestat;
    }



}
