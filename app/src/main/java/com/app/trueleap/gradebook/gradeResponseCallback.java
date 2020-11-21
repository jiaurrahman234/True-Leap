package com.app.trueleap.gradebook;

import androidx.annotation.NonNull;

import com.app.trueleap.MessagingModule.model.messageModel;
import com.app.trueleap.gradebook.model.GradeItem;

import java.util.ArrayList;

public interface gradeResponseCallback {
     void onSuccesGrade(@NonNull ArrayList<GradeItem> value);
}
