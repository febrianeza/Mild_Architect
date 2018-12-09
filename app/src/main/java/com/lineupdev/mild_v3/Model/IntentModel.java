package com.lineupdev.mild_v3.Model;

public class IntentModel {
    private String imageId;
    private String imageTitle;
    private String imageCredit;
    private String imageCreditWebsite;
    private String imageDimension;
    private String imageOriginalUrl;
    private String imagePreviewUrl;

    public IntentModel(String imageId, String imageTitle, String imageCredit, String imageCreditWebsite, String imageDimension, String imageOriginalUrl, String imagePreviewUrl) {
        this.imageId = imageId;
        this.imageTitle = imageTitle;
        this.imageCredit = imageCredit;
        this.imageCreditWebsite = imageCreditWebsite;
        this.imageDimension = imageDimension;
        this.imageOriginalUrl = imageOriginalUrl;
        this.imagePreviewUrl = imagePreviewUrl;
    }

    public String getImageId() {
        return imageId;
    }

    public String getImageTitle() {
        return imageTitle;
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

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public void setImageTitle(String imageTitle) {
        this.imageTitle = imageTitle;
    }

    public void setImageCredit(String imageCredit) {
        this.imageCredit = imageCredit;
    }

    public void setImageCreditWebsite(String imageCreditWebsite) {
        this.imageCreditWebsite = imageCreditWebsite;
    }

    public void setImageDimension(String imageDimension) {
        this.imageDimension = imageDimension;
    }

    public void setImageOriginalUrl(String imageOriginalUrl) {
        this.imageOriginalUrl = imageOriginalUrl;
    }

    public void setImagePreviewUrl(String imagePreviewUrl) {
        this.imagePreviewUrl = imagePreviewUrl;
    }
}
