package com.app.trueleap.home.studentsubject.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class ClassModel  implements Parcelable {

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
    ArrayList<DocumentsModel> documentsModelArrayList;
    ArrayList<DocumentsModel> AssignmentArrayList;

    public ClassModel(String uniqueperiodid, String teacher, String uniqueteacherid, String startdate, String enddate, String starttime, String endtime, String subject, ArrayList<DocumentsModel> documents , ArrayList<DocumentsModel> Assignments) {
        this.uniqueperiodid = uniqueperiodid;
        this.teacher = teacher;
        this.uniqueteacherid = uniqueteacherid;
        this.startdate = startdate;
        this.enddate = enddate;
        this.starttime = starttime;
        this.endtime = endtime;
        this.subject = subject;
        this.documentsModelArrayList = documents;
        this.AssignmentArrayList = Assignments;
    }

    public ClassModel(String uniqueperiodid, String teacher, String uniqueteacherid, String startdate, String enddate, String starttime, String endtime, ArrayList<String> days, String classname, String section, String subject, ArrayList<DocumentsModel> documents,ArrayList<DocumentsModel> Assignments) {
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
        this.documentsModelArrayList = documents;
        this.AssignmentArrayList = Assignments;
    }

    protected ClassModel(Parcel in) {
        uniqueperiodid = in.readString();
        teacher = in.readString();
        uniqueteacherid = in.readString();
        startdate = in.readString();
        enddate = in.readString();
        starttime = in.readString();
        endtime = in.readString();
        days = in.createStringArrayList();
        classname = in.readString();
        section = in.readString();
        subject = in.readString();
    }

    public static final Creator<ClassModel> CREATOR = new Creator<ClassModel>() {
        @Override
        public ClassModel createFromParcel(Parcel in) {
            return new ClassModel(in);
        }

        @Override
        public ClassModel[] newArray(int size) {
            return new ClassModel[size];
        }
    };

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

    public ArrayList<DocumentsModel> getDocumentsModelArrayList() {
        return documentsModelArrayList;
    }

    public void setDocumentsModelArrayList(ArrayList<DocumentsModel> documentsModelArrayList) {
        this.documentsModelArrayList = documentsModelArrayList;
    }

    public ArrayList<DocumentsModel> getAssgnmentModelArrayList() {
        return documentsModelArrayList;
    }

    public void setAssgnmentModelArrayList(ArrayList<DocumentsModel> AssignmentArrayList) {
        this.AssignmentArrayList = AssignmentArrayList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(uniqueperiodid);
        dest.writeString(teacher);
        dest.writeString(uniqueteacherid);
        dest.writeString(startdate);
        dest.writeString(enddate);
        dest.writeString(starttime);
        dest.writeString(endtime);
        dest.writeStringList(days);
        dest.writeString(classname);
        dest.writeString(section);
        dest.writeString(subject);
        dest.writeArray(new ArrayList[]{documentsModelArrayList});
        dest.writeArray(new ArrayList[]{AssignmentArrayList});
    }
}


