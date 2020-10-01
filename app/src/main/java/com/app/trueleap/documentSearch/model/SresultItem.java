package com.app.trueleap.documentSearch.model;

import static com.app.trueleap.external.Constants.EXCERPT_LENGTH;

public class SresultItem {
    String filename;
    String content;

    public SresultItem(String filename, String content) {
        this.filename = filename;
        this.content = content;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String get_content_exceprt() {
        String d_text = content;
        if (d_text.equals("null") || d_text.isEmpty() || d_text == "") {
            return "No Text";
        } else if (d_text.length() > EXCERPT_LENGTH) {
            return d_text.substring(0, EXCERPT_LENGTH) + " ...";
        } else {
            return d_text;
        }
    }
}


