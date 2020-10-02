package com.app.trueleap.localization.model;

public class LanguageModel {
    int lang_id;
    String lang_name;
    String lang_code;
    int lang_type;

    public LanguageModel(int lang_id, String lang_name, String lang_code, int lang_type) {
        this.lang_id = lang_id;
        this.lang_name = lang_name;
        this.lang_code = lang_code;
        this.lang_type = lang_type;
    }

    public int getLang_id() {
        return lang_id;
    }

    public void setLang_id(int lang_id) {
        this.lang_id = lang_id;
    }

    public String getLang_name() {
        return lang_name;
    }

    public void setLang_name(String lang_name) {
        this.lang_name = lang_name;
    }

    public String getLang_code() {
        return lang_code;
    }

    public void setLang_code(String lang_code) {
        this.lang_code = lang_code;
    }

    public int getLang_type() {
        return lang_type;
    }

    public void setLang_type(int lang_type) {
        this.lang_type = lang_type;
    }
}
