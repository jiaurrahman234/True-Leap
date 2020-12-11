package com.app.trueleap.home.subject.interfaces;

import androidx.annotation.NonNull;
import com.app.trueleap.home.subject.model.ClassModel;
import java.util.ArrayList;

public interface subjectResponseCallback {
     void onSuccesSubject(@NonNull ArrayList<ClassModel> value);
}
