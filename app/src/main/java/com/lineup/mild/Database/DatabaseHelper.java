package com.lineup.mild.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.lineup.mild.Model.DbModel;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "mild_db";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(DbModel.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        //Drop older table if exists
        db.execSQL("DROP TABLE IF EXISTS " + DbModel.TABLE_NAME);

        //Ceate table again
        onCreate(db);
    }

    public void favoriteWallpaper(String id, String credit, String credit_website, String dimensions, String original_url, String preview_url, String thumbnail_url) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(DbModel.COLUMN_IMAGE_ID, id);
        contentValues.put(DbModel.COLUMN_IMAGE_CREDIT, credit);
        contentValues.put(DbModel.COLUMN_IMAGE_CREDIT_WEBSITE, credit_website);
        contentValues.put(DbModel.COLUMN_IMAGE_DIMENSION, dimensions);
        contentValues.put(DbModel.COLUMN_IMAGE_ORIGINAL_URL, original_url);
        contentValues.put(DbModel.COLUMN_IMAGE_PREVIEW_URL, preview_url);
        contentValues.put(DbModel.COLUMN_IMAGE_THUMBNAIL_URL, thumbnail_url);

        db.insert(DbModel.TABLE_NAME, null, contentValues);
    }

    public DbModel getFavoriteWallpaper(String id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(DbModel.TABLE_NAME,
                new String[]{
                        DbModel.COLUMN_IMAGE_ID,
                        DbModel.COLUMN_IMAGE_CREDIT,
                        DbModel.COLUMN_IMAGE_CREDIT_WEBSITE,
                        DbModel.COLUMN_IMAGE_DIMENSION,
                        DbModel.COLUMN_IMAGE_ORIGINAL_URL,
                        DbModel.COLUMN_IMAGE_PREVIEW_URL,
                        DbModel.COLUMN_IMAGE_THUMBNAIL_URL
                },
                DbModel.COLUMN_IMAGE_ID + "=?",
                new String[]{id}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        //Prepare object
        DbModel dbModel = new DbModel(
                cursor.getString(cursor.getColumnIndex(DbModel.COLUMN_IMAGE_ID)),
                cursor.getString(cursor.getColumnIndex(DbModel.COLUMN_IMAGE_CREDIT)),
                cursor.getString(cursor.getColumnIndex(DbModel.COLUMN_IMAGE_CREDIT_WEBSITE)),
                cursor.getString(cursor.getColumnIndex(DbModel.COLUMN_IMAGE_DIMENSION)),
                cursor.getString(cursor.getColumnIndex(DbModel.COLUMN_IMAGE_ORIGINAL_URL)),
                cursor.getString(cursor.getColumnIndex(DbModel.COLUMN_IMAGE_PREVIEW_URL)),
                cursor.getString(cursor.getColumnIndex(DbModel.COLUMN_IMAGE_THUMBNAIL_URL))
        );

        cursor.close();

        return dbModel;
    }

    public List<DbModel> getAllFavoriteWallpaper() {
        List<DbModel> dbModels = new ArrayList<>();

        //Select all query
        String selectQuery = "SELECT * FROM " + DbModel.TABLE_NAME + " ORDER BY " + DbModel.COLUMN_ID + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        //looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                DbModel dbModel = new DbModel();

                dbModel.setU_id(cursor.getString(cursor.getColumnIndex(DbModel.COLUMN_IMAGE_ID)));
                dbModel.setCredit(cursor.getString(cursor.getColumnIndex(DbModel.COLUMN_IMAGE_CREDIT)));
                dbModel.setCredit_website(cursor.getString(cursor.getColumnIndex(DbModel.COLUMN_IMAGE_CREDIT_WEBSITE)));
                dbModel.setDimensions(cursor.getString(cursor.getColumnIndex(DbModel.COLUMN_IMAGE_DIMENSION)));
                dbModel.setOriginal_url(cursor.getString(cursor.getColumnIndex(DbModel.COLUMN_IMAGE_ORIGINAL_URL)));
                dbModel.setPreview_url(cursor.getString(cursor.getColumnIndex(dbModel.COLUMN_IMAGE_PREVIEW_URL)));
                dbModel.setThumbnail_url(cursor.getString(cursor.getColumnIndex(DbModel.COLUMN_IMAGE_THUMBNAIL_URL)));

                dbModels.add(dbModel);
            } while (cursor.moveToNext());
        }

        db.close();

        return dbModels;
    }

    public int getFavoriteWallpaperCount() {
        String countQuery = "SELECT  * FROM " + DbModel.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();


        // return count
        return count;
    }

    public void deleteFavoriteWallpaper(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DbModel.TABLE_NAME, DbModel.COLUMN_IMAGE_ID + " = ?",
                new String[]{id});
        db.close();
    }

    public boolean dataIsExists(String id) {

        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + DbModel.TABLE_NAME
                + " WHERE " + DbModel.COLUMN_IMAGE_ID + " =?";

        Cursor cursor = db.rawQuery(selectQuery, new String[]{id});
        if (cursor.getCount() <= 0) {
            cursor.close();
            db.close();
            return false;
        }

        cursor.close();
        db.close();
        return true;
    }
}
