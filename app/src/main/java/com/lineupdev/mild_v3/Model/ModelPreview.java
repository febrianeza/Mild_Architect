package com.lineupdev.mild_v3.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ModelPreview {
    String id;
    String title;
    String credit;
    String credit_website;
    String dimensions;
    String original_url;
    String preview_url;
    String thumbnail_url;

    public ModelPreview(String id, String title, String credit, String credit_website, String dimensions, String original_url, String preview_url, String thumbnail_url) {
        this.id = id;
        this.title = title;
        this.credit = credit;
        this.credit_website = credit_website;
        this.dimensions = dimensions;
        this.original_url = original_url;
        this.preview_url = preview_url;
        this.thumbnail_url = thumbnail_url;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getCredit() {
        return credit;
    }

    public String getCredit_website() {
        return credit_website;
    }

    public String getDimensions() {
        return dimensions;
    }

    public String getOriginal_url() {
        return original_url;
    }

    public String getPreview_url() {
        return preview_url;
    }

    public String getThumbnail_url() {
        return thumbnail_url;
    }
}
