package com.app.trueleap.Assignmentmodule.model;
import android.os.Parcel;
import android.os.Parcelable;

public class AssignmentModel implements  Parcelable{
    String id;
    String note_title;
    String note_text;
    String uploaded_date;
    String note_doc_file;
    String doc_type;

    public AssignmentModel(String id, String note_title, String note_text, String uploaded_date, String note_doc_file, String doc_type, String doc_path) {
        this.id = id;
        this.note_title = note_title;
        this.note_text = note_text;
        this.uploaded_date = uploaded_date;
        this.note_doc_file = note_doc_file;
        this.doc_type = doc_type;
    }

    protected AssignmentModel(Parcel in) {
        id = in.readString();
        note_title = in.readString();
        note_text = in.readString();
        uploaded_date = in.readString();
        note_doc_file = in.readString();
        doc_type = in.readString();
    }

        public static final Parcelable.Creator<AssignmentModel> CREATOR = new Parcelable.Creator<AssignmentModel>() {
        @Override
        public AssignmentModel createFromParcel(Parcel in) {
            return new AssignmentModel(in);
        }

        @Override
        public AssignmentModel[] newArray(int size) {
            return new AssignmentModel[size];
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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(note_title);
        dest.writeString(note_text);
        dest.writeString(uploaded_date);
        dest.writeString(note_doc_file);
        dest.writeString(doc_type);
    }
}
