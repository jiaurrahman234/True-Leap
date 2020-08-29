package com.app.trueleap.home.studentsubject;

import android.os.Parcel;
import android.os.Parcelable;

public class DocumentsModel implements Parcelable {
    String id,filename,type,title,note;

    public DocumentsModel(String id, String filename, String type, String title, String note) {
        this.id = id;
        this.filename = filename;
        this.type = type;
        this.title = title;
        this.note = note;
    }

    protected DocumentsModel(Parcel in) {
        id = in.readString();
        filename = in.readString();
        type = in.readString();
        title = in.readString();
        note = in.readString();
    }

    public static final Creator<DocumentsModel> CREATOR = new Creator<DocumentsModel>() {
        @Override
        public DocumentsModel createFromParcel(Parcel in) {
            return new DocumentsModel(in);
        }

        @Override
        public DocumentsModel[] newArray(int size) {
            return new DocumentsModel[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(filename);
        dest.writeString(type);
        dest.writeString(title);
        dest.writeString(note);
    }
}
