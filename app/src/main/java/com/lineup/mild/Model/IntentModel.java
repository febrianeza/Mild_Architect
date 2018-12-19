package com.lineup.mild.Model;

public class IntentModel {
    private String imageId;
    private String imageCredit;
    private String imageCreditWebsite;
    private String imageDimension;
    private String imageOriginalUrl;
    private String imagePreviewUrl;
    private String imageThumbnailUrl;

    public IntentModel(String imageId, String imageCredit, String imageCreditWebsite, String imageDimension, String imageOriginalUrl, String imagePreviewUrl, String imageThumbnailUrl) {
        this.imageId = imageId;
        this.imageCredit = imageCredit;
        this.imageCreditWebsite = imageCreditWebsite;
        this.imageDimension = imageDimension;
        this.imageOriginalUrl = imageOriginalUrl;
        this.imagePreviewUrl = imagePreviewUrl;
        this.imageThumbnailUrl = imageThumbnailUrl;
    }

    public String getImageId() {
        return imageId;
    }

    public String getImageCredit() {
        return imageCredit;
    }

    public String getImageCreditWebsite() {
        return imageCreditWebsite;
    }

    public String getImageDimension() {
        return imageDimension;
    }

    public String getImageOriginalUrl() {
        return imageOriginalUrl;
    }

    public String getImagePreviewUrl() {
        return imagePreviewUrl;
    }

    public String getImageThumbnailUrl() {
        return imageThumbnailUrl;
    }
}
