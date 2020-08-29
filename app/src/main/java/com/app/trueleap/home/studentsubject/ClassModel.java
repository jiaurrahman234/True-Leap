package com.app.trueleap.home.studentsubject;

import java.util.ArrayList;

public class ClassModel {

    String uniqueperiodid;
    String teacher;
    String uniqueteacherid;
    String startdate;
    String enddate;
    String starttime;
    String endtime;
    ArrayList<String> days;
    String classname;
    String section;
    String subject;
    String documents;

    public ClassModel(String uniqueperiodid, String teacher, String uniqueteacherid, String startdate, String enddate, String starttime, String endtime, String subject, String documents) {
        this.uniqueperiodid = uniqueperiodid;
        this.teacher = teacher;
        this.uniqueteacherid = uniqueteacherid;
        this.startdate = startdate;
        this.enddate = enddate;
        this.starttime = starttime;
        this.endtime = endtime;
        this.subject = subject;
        this.documents = documents;
    }

    public ClassModel(String uniqueperiodid, String teacher, String uniqueteacherid, String startdate, String enddate, String starttime, String endtime, ArrayList<String> days, String classname, String section, String subject, String documents) {
        this.uniqueperiodid = uniqueperiodid;
        this.teacher = teacher;
        this.uniqueteacherid = uniqueteacherid;
        this.startdate = startdate;
        this.enddate = enddate;
        this.starttime = starttime;
        this.endtime = endtime;
        this.days = days;
        this.classname = classname;
        this.section = section;
        this.subject = subject;
        this.documents = documents;
    }

    public String getUniqueperiodid() {
        return uniqueperiodid;
    }

    public void setUniqueperiodid(String uniqueperiodid) {
        this.uniqueperiodid = uniqueperiodid;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public String getUniqueteacherid() {
        return uniqueteacherid;
    }

    public void setUniqueteacherid(String uniqueteacherid) {
        this.uniqueteacherid = uniqueteacherid;
    }

    public String getStartdate() {
        return startdate;
    }

    public void setStartdate(String startdate) {
        this.startdate = startdate;
    }

    public String getEnddate() {
        return enddate;
    }

    public void setEnddate(String enddate) {
        this.enddate = enddate;
    }

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    public ArrayList<String> getDays() {
        return days;
    }

    public void setDays(ArrayList<String> days) {
        this.days = days;
    }

    public String getClassname() {
        return classname;
    }

    public void setClassname(String classname) {
        this.classname = classname;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDocuments() {
        return documents;
    }

    public void setDocuments(String documents) {
        this.documents = documents;
    }
}


