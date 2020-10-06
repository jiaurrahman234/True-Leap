package com.app.trueleap.home.studentsubject.model;

import android.os.Parcel;
import android.os.Parcelable;

public class DocumentsModel implements Parcelable {
    String id,filename,type,title,file_url,note, validupto ;

    public DocumentsModel(String id, String filename, String type, String title, String file_url, String note, String validupto) {
        this.id = id;
        this.filename = filename;
        this.type = type;
        this.title = title;
        this.file_url = file_url;
        this.note = note;
        this.validupto = validupto;
    }

    protected DocumentsModel(Parcel in) {
        id = in.readString();
        filename = in.readString();
        type = in.readString();
        title = in.readString();
        file_url = in.readString();
        note = in.readString();
        validupto = in.readString();
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

    public String getFile_url() {
        return file_url;
    }

    public void setFile_url(String file_url) {
        this.file_url = file_url;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getValidupto() {
        return validupto;
    }

    public void setValidupto(String validupto) {
        this.validupto = validupto;
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
        dest.writeString(file_url);
        dest.writeString(note);
        dest.writeString(validupto);
    }
}
