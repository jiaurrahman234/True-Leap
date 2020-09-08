package com.app.trueleap.home.studentsubject;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Calendar;

public class CalendarModel  implements Parcelable {
    int id;
    String periodId;

    public CalendarModel(int id, String periodId) {
        this.id = id;
        this.periodId = periodId;
    }

    protected CalendarModel(Parcel in) {
        id = in.readInt();
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
        dest.writeInt(id);
        dest.writeString(periodId);
    }
}
