package com.app.trueleap.home.subject.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Calendar;

public class CalendarModel  implements Parcelable {
    String date;
    String periodId;

    public CalendarModel(String date, String periodId) {
        this.date = date;
        this.periodId = periodId;
    }

    protected CalendarModel(Parcel in) {
        date = in.readString();
        periodId = in.readString();
    }

    public static final Creator<CalendarModel> CREATOR = new Creator<CalendarModel>() {
        @Override
        public CalendarModel createFromParcel(Parcel in) {
            return new CalendarModel(in);
        }

        @Override
        public CalendarModel[] newArray(int size) {
            return new CalendarModel[size];
        }
    };

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPeriodId() {
        return periodId;
    }

    public void setPeriodId(String periodId) {
        this.periodId = periodId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(date);
        dest.writeString(periodId);
    }
}
