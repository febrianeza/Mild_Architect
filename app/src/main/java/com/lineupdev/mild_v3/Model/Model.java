package com.lineupdev.mild_v3.Model;

public class Model {

    String id;
    String thumbnail_url;

    public Model(String id, String thumbnail_url) {
        this.id = id;
        this.thumbnail_url = thumbnail_url;
    }

    public String getId() {
        return id;
    }

    public String getThumbnail_url() {
        return thumbnail_url;
    }
}
