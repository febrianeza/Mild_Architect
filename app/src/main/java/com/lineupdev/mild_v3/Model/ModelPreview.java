package com.lineupdev.mild_v3.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ModelPreview {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("credit")
    @Expose
    private String credit;
    @SerializedName("credit_website")
    @Expose
    private String creditWebsite;
    @SerializedName("camera_make")
    @Expose
    private String cameraMake;
    @SerializedName("camera_model")
    @Expose
    private String cameraModel;
    @SerializedName("focal_length")
    @Expose
    private String focalLength;
    @SerializedName("aperture")
    @Expose
    private String aperture;
    @SerializedName("shutter_speed")
    @Expose
    private String shutterSpeed;
    @SerializedName("iso")
    @Expose
    private String iso;
    @SerializedName("dimensions")
    @Expose
    private String dimensions;
    @SerializedName("original_url")
    @Expose
    private String originalUrl;
    @SerializedName("preview_url")
    @Expose
    private String previewUrl;
    @SerializedName("thumbnail_url")
    @Expose
    private String thumbnailUrl;
    @SerializedName("thumbnail_height")
    @Expose
    private String thumbnailHeight;

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getCredit() {
        return credit;
    }

    public String getCreditWebsite() {
        return creditWebsite;
    }

    public String getCameraMake() {
        return cameraMake;
    }

    public String getCameraModel() {
        return cameraModel;
    }

    public String getFocalLength() {
        return focalLength;
    }

    public String getAperture() {
        return aperture;
    }

    public String getShutterSpeed() {
        return shutterSpeed;
    }

    public String getIso() {
        return iso;
    }

    public String getDimensions() {
        return dimensions;
    }

    public String getOriginalUrl() {
        return originalUrl;
    }

    public String getPreviewUrl() {
        return previewUrl;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public String getThumbnailHeight() {
        return thumbnailHeight;
    }
}
