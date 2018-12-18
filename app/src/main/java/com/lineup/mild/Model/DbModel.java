package com.lineup.mild.Model;

public class DbModel {
    public static final String TABLE_NAME = "mild_architecture";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_IMAGE_ID = "imageId";
    public static final String COLUMN_IMAGE_TITLE = "imageTitle";
    public static final String COLUMN_IMAGE_CREDIT = "imageCredit";
    public static final String COLUMN_IMAGE_CREDIT_WEBSITE = "imageCreditWebsite";
    public static final String COLUMN_IMAGE_DIMENSION = "imageDimension";
    public static final String COLUMN_IMAGE_ORIGINAL_URL = "imageOriginalUrl";
    public static final String COLUMN_IMAGE_PREVIEW_URL = "imagePreviewUrl";
    public static final String COLUMN_IMAGE_THUMBNAIL_URL = "imageThumbnailUrl";

    private String u_id;
    private String title;
    private String credit;
    private String credit_website;
    private String dimensions;
    private String original_url;
    private String preview_url;
    private String thumbnail_url;

    // Create table SQL Query
    public static final String CREATE_TABLE = "CREATE TABLE "
            + TABLE_NAME + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_IMAGE_ID + " TEXT, "
            + COLUMN_IMAGE_TITLE + " TEXT, "
            + COLUMN_IMAGE_CREDIT + " TEXT, "
            + COLUMN_IMAGE_CREDIT_WEBSITE + " TEXT, "
            + COLUMN_IMAGE_DIMENSION + " TEXT, "
            + COLUMN_IMAGE_ORIGINAL_URL + " TEXT, "
            + COLUMN_IMAGE_PREVIEW_URL + " TEXT, "
            + COLUMN_IMAGE_THUMBNAIL_URL + " TEXT"
            + ")";

    public DbModel() {

    }

    public DbModel(String u_id, String title, String credit, String credit_website, String dimensions, String original_url, String preview_url, String thumbnail_url) {
        this.u_id = u_id;
        this.title = title;
        this.credit = credit;
        this.credit_website = credit_website;
        this.dimensions = dimensions;
        this.original_url = original_url;
        this.preview_url = preview_url;
        this.thumbnail_url = thumbnail_url;
    }

    public String getU_id() {
        return u_id;
    }

    public void setU_id(String u_id) {
        this.u_id = u_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCredit() {
        return credit;
    }

    public void setCredit(String credit) {
        this.credit = credit;
    }

    public String getCredit_website() {
        return credit_website;
    }

    public void setCredit_website(String credit_website) {
        this.credit_website = credit_website;
    }

    public String getDimensions() {
        return dimensions;
    }

    public void setDimensions(String dimensions) {
        this.dimensions = dimensions;
    }

    public String getOriginal_url() {
        return original_url;
    }

    public void setOriginal_url(String original_url) {
        this.original_url = original_url;
    }

    public String getPreview_url() {
        return preview_url;
    }

    public void setPreview_url(String preview_url) {
        this.preview_url = preview_url;
    }

    public String getThumbnail_url() {
        return thumbnail_url;
    }

    public void setThumbnail_url(String thumbnail_url) {
        this.thumbnail_url = thumbnail_url;
    }
}
