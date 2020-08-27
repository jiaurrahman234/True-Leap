package com.app.trueleap.home.studentsubject;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Subject {

    @SerializedName("uniqueperiodid")
    public String uniqueperiodid;
    @SerializedName("teacher")
    @Expose
    private String teacher;
    @SerializedName("uniqueteacherid")
    @Expose
    private String uniqueteacherid;
    @SerializedName("startdate")
    @Expose
    private String startdate;
    @SerializedName("enddate")
    @Expose
    private String enddate;
    @SerializedName("starttime")
    @Expose
    private String starttime;
    @SerializedName("endtime")
    @Expose
    private String endtime;
    @SerializedName("days")
    @Expose
    private ArrayList<String> days = null;
    @SerializedName("class")
    @Expose
    private String _class;
    @SerializedName("section")
    @Expose
    private String section;
    @SerializedName("subject")
    @Expose
    private String subject;
    @SerializedName("documents")
    @Expose
    private ArrayList<Object> documents = null;

}
