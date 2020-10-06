package com.app.trueleap.Classnotemodule.model;

import android.os.Parcel;
import android.os.Parcelable;

import static com.app.trueleap.external.Constants.EXCERPT_LENGTH;

public class ClassnoteModel implements Parcelable {
    String id;
    String note_title,file_url;
    String note_text;
    String uploaded_date;
    String note_doc_file;
    String doc_type;
    String validupto;

    public ClassnoteModel(String id, String note_title,String file_url, String note_text, String uploaded_date, String note_doc_file, String doc_type, String validupto) {
        this.id = id;
        this.note_title = note_title;
        this.file_url = file_url;
        this.note_text = note_text;
        this.uploaded_date = uploaded_date;
        this.note_doc_file = note_doc_file;
        this.doc_type = doc_type;
        this.validupto = validupto;
    }

    protected ClassnoteModel(Parcel in) {
        id = in.readString();
        note_title = in.readString();
        file_url = in.readString();
        note_text = in.readString();
        uploaded_date = in.readString();
        note_doc_file = in.readString();
        doc_type = in.readString();
        validupto = in.readString();
    }

    public static final Creator<ClassnoteModel> CREATOR = new Creator<ClassnoteModel>() {
        @Override
        public ClassnoteModel createFromParcel(Parcel in) {
            return new ClassnoteModel(in);
        }

        @Override
        public ClassnoteModel[] newArray(int size) {
            return new ClassnoteModel[size];
        }
    };

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

    public String getFile_url() {
        return file_url;
    }

    public void setFile_url(String file_url) {
        this.file_url = file_url;
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

    public String getValidupto() {
        return validupto;
    }


    public void setValidupto(String validupto) {
        this.validupto = validupto;
    }

    public String get_doucument_exceprt() {
        String d_text = note_text;
        if (d_text.equals("null") || d_text.isEmpty() || d_text == "") {
            return "No Text";
        } else if (d_text.length() > EXCERPT_LENGTH) {
            return d_text.substring(0, EXCERPT_LENGTH) + " ...";
        } else {
            return d_text;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(note_title);
        dest.writeString(file_url);
        dest.writeString(note_text);
        dest.writeString(uploaded_date);
        dest.writeString(note_doc_file);
        dest.writeString(doc_type);
        dest.writeString(validupto);
    }
}