package com.app.trueleap.MessagingModule.model;

public class messageModel {
    String user, ChatText;

    public messageModel(String user, String chatText) {
        this.user = user;
        ChatText = chatText;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getChatText() {
        return ChatText;
    }

    public void setChatText(String chatText) {
        ChatText = chatText;
    }
}


