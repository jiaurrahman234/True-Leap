package com.app.trueleap.notification;
import android.os.Parcel;
import android.os.Parcelable;

import static com.app.trueleap.external.Constants.EXCERPT_LENGTH;
import static com.app.trueleap.external.Constants.NOTIF_EXCERPT_LENGTH;

public class NotificationModel implements Parcelable {
    String notificationid, note, document, sent_by, date;
    Boolean viewed;

    public NotificationModel(String notificationid, String note, String document, String sent_by, String date, Boolean viewed) {
        this.notificationid = notificationid;
        this.note = note;
        this.document = document;
        this.sent_by = sent_by;
        this.date = date;
        this.viewed = viewed;
    }

    protected NotificationModel(Parcel in) {
        notificationid = in.readString();
        note = in.readString();
        document = in.readString();
        sent_by = in.readString();
        date = in.readString();
        viewed = in.readBoolean();
    }

    public static final Parcelable.Creator<NotificationModel> CREATOR = new Parcelable.Creator<NotificationModel>() {
        @Override
        public NotificationModel createFromParcel(Parcel in) {
            return new NotificationModel(in);
        }

        @Override
        public NotificationModel[] newArray(int size) {
            return new NotificationModel[size];
        }
    };

    public String getNotificationid() {
        return notificationid;
    }

    public void setNotificationid(String notificationid) {
        this.notificationid = notificationid;
    }

    public String getNote() {
        return note;
    }

    public String get_note_exceprt() {
        String d_text = note;
        if (d_text.equals("null") || d_text.isEmpty() || d_text == "") {
            return "Empty Message";
        } else if (d_text.length() > NOTIF_EXCERPT_LENGTH) {
            return d_text.substring(0, NOTIF_EXCERPT_LENGTH) + " ..";
        } else {
            return d_text;
        }
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
    }

    public String getSent_by() {
        return sent_by;
    }

    public void setSent_by(String sent_by) {
        this.sent_by = sent_by;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Boolean getViewed() {
        return viewed;
    }

    public void setViewed(Boolean viewed) {
        this.viewed = viewed;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(notificationid);
        dest.writeString(note);
        dest.writeString(document);
        dest.writeString(sent_by);
        dest.writeString(date);
        dest.writeBoolean(viewed);
    }
}
