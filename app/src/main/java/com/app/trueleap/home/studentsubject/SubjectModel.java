package com.app.trueleap.home.studentsubject;

import java.util.ArrayList;

public class SubjectModel {
    String id;
    String subject_code;
    String subject_title;
    String start_time;
    String end_time;
    ArrayList<ClassDate> classDateArrayList;

    public SubjectModel(String id, String subject_code, String subject_title, String start_time, String end_time, ArrayList<ClassDate> classDateArrayList) {
        this.id = id;
        this.subject_code = subject_code;
        this.subject_title = subject_title;
        this.start_time = start_time;
        this.end_time = end_time;
        this.classDateArrayList = classDateArrayList;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSubject_code() {
        return subject_code;
    }

    public void setSubject_code(String subject_code) {
        this.subject_code = subject_code;
    }

    public String getSubject_title() {
        return subject_title;
    }

    public void setSubject_title(String subject_title) {
        this.subject_title = subject_title;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public ArrayList<ClassDate> getClassDateArrayList() {
        return classDateArrayList;
    }

    public void setClassDateArrayList(ArrayList<ClassDate> classDateArrayList) {
        this.classDateArrayList = classDateArrayList;
    }
}


