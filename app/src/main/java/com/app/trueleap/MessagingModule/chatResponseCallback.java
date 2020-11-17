package com.app.trueleap.MessagingModule;

import androidx.annotation.NonNull;
import com.app.trueleap.MessagingModule.model.messageModel;
import java.util.ArrayList;

public interface chatResponseCallback {
     void onSucceschat(@NonNull ArrayList<messageModel> value);
}
