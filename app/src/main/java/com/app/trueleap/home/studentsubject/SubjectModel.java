package com.app.trueleap.home.studentsubject;

public class SubjectModel {
    String id;
    String subject_code;
    String subject_title;

    public SubjectModel(String id, String subject_code, String subject_title) {
        this.id = id;
        this.subject_code = subject_code;
        this.subject_title = subject_title;
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
}
