package com.app.trueleap.localization.model;

public class CountryModel {
    int id;
    String base_url;
    String name;

    public CountryModel(int id, String base_url, String name) {
        this.id = id;
        this.base_url = base_url;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getBase_url() {
        return base_url;
    }

    public String getName() {
        return name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setBase_url(String base_url) {
        this.base_url = base_url;
    }

    public void setName(String name) {
        this.name = name;
    }
}
