package com.app.trueleap.MessagingModule.model;
public class TeacherModel {
    String teacher_id, teacher_name ;

    public TeacherModel(String teacher_id, String teacher_name) {
        this.teacher_id = teacher_id;
        this.teacher_name = teacher_name;
    }

    public String getTeacher_id() {
        return teacher_id;
    }

    public void setTeacher_id(String teacher_id) {
        this.teacher_id = teacher_id;
    }

    public String getTeacher_name() {
        return teacher_name;
    }

    public void setTeacher_name(String teacher_name) {
        this.teacher_name = teacher_name;
    }

}