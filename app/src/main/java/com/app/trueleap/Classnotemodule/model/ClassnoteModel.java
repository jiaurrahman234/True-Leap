package com.app.trueleap.Classnotemodule.model;

public class ClassnoteModel {
    String id;
    String note_title;
    String note_text;
    String uploaded_date;
    String note_doc_file;
    String doc_type;
    String doc_path;

    public ClassnoteModel(String id, String note_title, String note_text, String uploaded_date, String note_doc_file, String doc_type, String doc_path) {
        this.id = id;
        this.note_title = note_title;
        this.note_text = note_text;
        this.uploaded_date = uploaded_date;
        this.note_doc_file = note_doc_file;
        this.doc_type = doc_type;
        this.doc_path = doc_path;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNote_title() {
        return note_title;
    }

    public void setNote_title(String note_title) {
        this.note_title = note_title;
    }

    public String getNote_text() {
        return note_text;
    }

    public void setNote_text(String note_text) {
        this.note_text = note_text;
    }

    public String getUploaded_date() {
        return uploaded_date;
    }

    public void setUploaded_date(String uploaded_date) {
        this.uploaded_date = uploaded_date;
    }

    public String getNote_doc_file() {
        return note_doc_file;
    }

    public void setNote_doc_file(String note_doc_file) {
        this.note_doc_file = note_doc_file;
    }

    public String getDoc_type() {
        return doc_type;
    }

    public void setDoc_type(String doc_type) {
        this.doc_type = doc_type;
    }

    public String getDoc_path() {
        return doc_path;
    }

    public void setDoc_path(String doc_path) {
        this.doc_path = doc_path;
    }
}
