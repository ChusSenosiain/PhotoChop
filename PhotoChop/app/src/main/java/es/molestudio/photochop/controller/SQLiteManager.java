package es.molestudio.photochop.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.net.Uri;

import java.util.ArrayList;

import es.molestudio.photochop.controller.util.AppUtils;
import es.molestudio.photochop.model.Category;
import es.molestudio.photochop.model.Image;
import es.molestudio.photochop.model.SubCategory;

/**
 * Created by Chus on 31/12/14.
 */
public class SQLiteManager implements IDataManager {

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

    public SQLiteManager(Context context) {
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
        content.put(CN_CAT, image.getImageCategory().getCagetoryId());
        content.put(CN_SUB_CAT, image.getImageSubCategory().getSubCategoryId());
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


    // ---------------------------------------------------------------- //
    // IMAGE CRUD OPERATIONS
    // ---------------------------------------------------------------- //
    @Override
    public long insertImage(Image image) throws SQLiteException{

        long idImage;
        SQLiteDatabase db = DBHelper.getInstance(mContext);
        db.beginTransaction();

        idImage = db.insert("image", null, createContentImage(image));

        db.setTransactionSuccessful();
        db.endTransaction();

        return idImage;

    }


    @Override
    public ArrayList<Image> insertImages(ArrayList<Image> images) {

        SQLiteDatabase db = DBHelper.getInstance(mContext);
        db.beginTransaction();
        long idImage;

        ArrayList<Image> imagesWithId = new ArrayList<Image>();

        for (Image image: images) {
            idImage = db.insert("image", null, createContentImage(image));
            image.setImageId((int) idImage);
            imagesWithId.add(image);
        }
        db.setTransactionSuccessful();
        db.endTransaction();

        return imagesWithId;
    }

    @Override
    public ArrayList<Image> getImages() {

        String select = "SELECT * FROM image;";
        ArrayList<Image> images = new ArrayList<Image>();

        SQLiteDatabase db = DBHelper.getInstance(mContext);
        Cursor result = db.rawQuery(select, null);

        if (result.moveToFirst()){
            do {
                images.add(createImageFromCursor(result));

            }while(result.moveToNext());
        }

        result.close();

        return images;
    }






    @Override
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

    @Override
    public int updateImage(Image image) {

        SQLiteDatabase db = DBHelper.getInstance(mContext);

        db.beginTransaction();

        int numRows = db.update("image", createContentImage(image), CN_IMAGE_ID + "=" + image.getImageId(), null);

        db.setTransactionSuccessful();
        db.endTransaction();

        return numRows;

    }

    @Override
    public int deleteImage(Image image) {

        SQLiteDatabase db = DBHelper.getInstance(mContext);

        db.beginTransaction();

        int numRows = db.delete("image", CN_IMAGE_ID + "=" + image.getImageId(), null);

        db.setTransactionSuccessful();
        db.endTransaction();

        return numRows;

    }

    @Override
    public Image selectImage(Integer imageId) {

        Image image = null;

        SQLiteDatabase db = DBHelper.getInstance(mContext);

        Cursor result = db.rawQuery("SELECT * FROM image WHERE " + CN_IMAGE_ID + "=" + imageId, null);

        if (result.moveToFirst()) {
            image = createImageFromCursor(result);
        }

        return image;

    }


    // ---------------------------------------------------------------- //
    // CATEGORY CRUD OPERATIONS
    // ---------------------------------------------------------------- //
    @Override
    public long insertCategory(Category category) {

        long idCategory;
        SQLiteDatabase db = DBHelper.getInstance(mContext);
        db.beginTransaction();

        idCategory = db.insert("category", null, createContentCategory(category));

        db.setTransactionSuccessful();
        db.endTransaction();

        return idCategory;
    }

    @Override
    public ArrayList<Category> getCategories() {

        String select = "SELECT * FROM category;";
        ArrayList<Category> categories = new ArrayList<Category>();

        SQLiteDatabase db = DBHelper.getInstance(mContext);
        Cursor result = db.rawQuery(select, null);

        Category category = null;

        if (result.moveToFirst()){
            do {
                category = new Category();

            }while(result.moveToNext());
        }

        result.close();

        return categories;
    }

    @Override
    public int updateCategory(Category category) {
        return 0;
    }

    @Override
    public int deleteCategory(Category category) {
        return 0;
    }

    @Override
    public Category selectCategory(Integer categoryId) {
        return null;
    }


    // ---------------------------------------------------------------- //
    // SUBCATEGORY CRUD OPERATIONS
    // ---------------------------------------------------------------- //
    @Override
    public long insertSubCategory(SubCategory subCategory) {
        return 0;
    }

    @Override
    public ArrayList<SubCategory> getSubCategories() {
        return null;
    }

    @Override
    public ArrayList<SubCategory> getSubCategories(Integer categoryId) {
        return null;
    }

    @Override
    public int updateSubCategory(SubCategory subCategory) {
        return 0;
    }

    @Override
    public int deleteSubCategory(SubCategory subCategory) {
        return 0;
    }

    @Override
    public Category selectSubCategory(Integer subCategoryId) {
        return null;
    }






    private Image createImageFromCursor(Cursor cursor) {

        Image image = new Image();

        image.setImageId(cursor.getInt(cursor.getColumnIndex(CN_IMAGE_ID)));
        image.setImageUri(Uri.parse(cursor.getString(cursor.getColumnIndex(CN_URI))));
        image.setImageDate(AppUtils.getDateFromString(cursor.getString(cursor.getColumnIndex(CN_DATE))));
        image.setImageLatitude(cursor.getDouble(cursor.getColumnIndex(CN_LAT)));
        image.setImageLongitude(cursor.getDouble(cursor.getColumnIndex(CN_LON)));
        image.setFavorite(cursor.getInt(cursor.getColumnIndex(CN_FAVORITE)) == 1);

        return image;
    }

}
