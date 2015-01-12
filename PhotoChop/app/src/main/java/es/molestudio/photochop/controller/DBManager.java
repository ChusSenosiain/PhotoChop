package es.molestudio.photochop.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.net.Uri;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import es.molestudio.photochop.controller.util.AppUtils;
import es.molestudio.photochop.model.Category;
import es.molestudio.photochop.model.Image;
import es.molestudio.photochop.model.SubCategory;

/**
 * Created by Chus on 31/12/14.
 */
public class DBManager {

    ///////////////////////////////////////////////////////////////////////////////
    /////                   CRUD OPERATIONS                                  /////
    ///////////////////////////////////////////////////////////////////////////////


    // Image table
    private static final String CN_IMAGE_ID = "imageId";
    private static final String CN_NAME = "name";
    private static final String CN_DESCRIPTION = "description";
    private static final String CN_URI = "uri";
    private static final String CN_DATE = "date";
    private static final String CN_LAT = "latitude";
    private static final String CN_LON = "longitude";
    private static final String CN_CAT = "categoryId";
    private static final String CN_SUB_CAT = "subcategoryId";
    private static final String CN_FAVORITE = "favorite";
    // Category table: the rest of the fiels have the same names than CN_NAME and CN_DESCRIPTION
    private static final String CN_CATEGORY_ID = "categoryId";
    // Subcategory table: the rest of the fields have the same names than CN_NAME, CN_DESCRIPTION and CN_CATEGORY_ID
    private static final String CN_SUBCATEGORY_ID = "subcategoryId";


    private Context mContext;

    public DBManager(Context context) {
        mContext = context;
    }



    // Content values from objects

    // Image
    private ContentValues createContentImage(Image image) {

        ContentValues content = new ContentValues();
        content.put(CN_NAME, image.getImageName());
        content.put(CN_DESCRIPTION, image.getImageDescription());
        content.put(CN_URI, image.getImageUri().toString());
        content.put(CN_DATE, AppUtils.getStringFormatDate(image.getImageDate()));
        content.put(CN_LAT, image.getImageLatitude());
        content.put(CN_LON, image.getImageLongitude());
        content.put(CN_CAT, image.getImageCategory());
        content.put(CN_SUB_CAT, image.getImageSubCategory());
        content.put(CN_FAVORITE, image.isFavorite());

        return content;

    }


    // Category
    private ContentValues createContentCategory(Category category) {

        ContentValues content = new ContentValues();
        content.put(CN_CATEGORY_ID, category.getCagetoryId());
        content.put(CN_NAME, category.getCategoryName());
        content.put(CN_DESCRIPTION, category.getCategoryDescription());

        return content;

    }

    // Subcategory
    private ContentValues createContentSubCategory(SubCategory subCategory) {

        ContentValues content = new ContentValues();
        content.put(CN_SUBCATEGORY_ID, subCategory.getSubCategoryId());
        content.put(CN_CATEGORY_ID, subCategory.getCategoryId());
        content.put(CN_NAME, subCategory.getSubCaregoryName());
        content.put(CN_DESCRIPTION, subCategory.getSubCategoryDescription());

        return content;
    }


    public long insertImage(Image image) throws SQLiteException{

        Long idImage;
        SQLiteDatabase db = DBHelper.getInstance(mContext);
        db.beginTransaction();

        idImage = db.insert("image", null, createContentImage(image));

        db.setTransactionSuccessful();
        db.endTransaction();

        return idImage;

    }


    public ArrayList<Image> getImages() {

        String select = "SELECT * FROM image;";
        ArrayList<Image> images = new ArrayList<Image>();

        SQLiteDatabase db = DBHelper.getInstance(mContext);
        Cursor result = db.rawQuery(select, null);

        Image image = null;

        if (result.moveToFirst()){
            do {

                image = new Image();

                image.setImageId(result.getInt(result.getColumnIndex(CN_IMAGE_ID)));
                image.setImageUri(Uri.parse(result.getString(result.getColumnIndex(CN_URI))));
                image.setImageDate(AppUtils.getDateFromString(result.getString(result.getColumnIndex(CN_DATE))));
                image.setFavorite(result.getInt(result.getColumnIndex(CN_FAVORITE)) == 1);

                images.add(image);

            }while(result.moveToNext());
        }

        result.close();

        return images;
    }


    public ArrayList<Integer> getImagesIds() {
        ArrayList<Integer> imageIds = new ArrayList<Integer>();

        SQLiteDatabase db = DBHelper.getInstance(mContext);

        Cursor result = db.rawQuery("SELECT " + CN_IMAGE_ID + " FROM image;", null);

        if (result.moveToFirst()) {
            do {
                imageIds.add(result.getInt(result.getColumnIndex(CN_IMAGE_ID)));
            } while (result.moveToNext());

        }
        result.close();


        return imageIds;
    }


    public int updateImage(Image image) {

        SQLiteDatabase db = DBHelper.getInstance(mContext);

        db.beginTransaction();

        int numRows = db.update("image", createContentImage(image), CN_IMAGE_ID + "=" + image.getImageId(), null);

        db.setTransactionSuccessful();
        db.endTransaction();

        return numRows;

    }


    public int deleteImage(Image image) {

        SQLiteDatabase db = DBHelper.getInstance(mContext);

        db.beginTransaction();

        int numRows = db.delete("image", CN_IMAGE_ID + "=" + image.getImageId(), null);

        db.setTransactionSuccessful();
        db.endTransaction();

        return numRows;

    }

    public Image selectImage(Integer imageId) {

        Image image = null;

        SQLiteDatabase db = DBHelper.getInstance(mContext);

        Cursor result = db.rawQuery("SELECT * FROM image WHERE " + CN_IMAGE_ID + "=" + imageId, null);

        if (result.moveToFirst()) {

            image = new Image();

            image.setImageId(result.getInt(result.getColumnIndex(CN_IMAGE_ID)));
            image.setImageUri(Uri.parse(result.getString(result.getColumnIndex(CN_URI))));
            image.setImageDate(AppUtils.getDateFromString(result.getString(result.getColumnIndex(CN_DATE))));
            image.setFavorite(result.getInt(result.getColumnIndex(CN_FAVORITE)) == 1);
        }

        return image;


    }








}
