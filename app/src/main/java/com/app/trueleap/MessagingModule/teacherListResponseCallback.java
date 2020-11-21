package com.app.trueleap.MessagingModule;

import androidx.annotation.NonNull;

import com.app.trueleap.MessagingModule.model.TeacherModel;
import com.app.trueleap.MessagingModule.model.messageModel;

import java.util.ArrayList;

public interface teacherListResponseCallback {
     void onSuccessteacherList(@NonNull ArrayList<TeacherModel> value);
}
