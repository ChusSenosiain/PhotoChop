package es.molestudio.photochop.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import es.molestudio.photochop.model.Category;
import es.molestudio.photochop.model.Image;
import es.molestudio.photochop.model.SubCategory;

/**
 * Created by Chus on 31/12/14.
 */
public class DBHelper extends SQLiteOpenHelper {


    private static SQLiteDatabase sDb;
    private static final String TAG = "DBHelper";
    private static final String DB_NAME = "images.sqlite";


    private static final int DATABASE_VERSION = 1;

    public static synchronized SQLiteDatabase getInstance(Context context) {
        // Usa always the context of the app and never an activity context
        // See this article for more information: http://bit.ly/6LRzfx
        if (sDb == null) {
            Log.d(TAG, "La BD no esta instanciada");
            sDb = new DBHelper(context.getApplicationContext()).getWritableDatabase();
        }

        return sDb;
    }

    /**
     * Constructor should be private to prevent direct instantiation.
     * make call to static factory method "getInstance()" instead.
     */
    private DBHelper(Context context) {
        super(context, DB_NAME, null, DATABASE_VERSION);

    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        Log.d(TAG, "DB doesn't exist, create it!");

        db.beginTransaction();

        db.execSQL("CREATE TABLE \"image\" (\"imageId\" INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL  UNIQUE , \"name\" TEXT, \"description\" TEXT, \"uri\" TEXT, \"date\" DATETIME, \"latitude\" DOUBLE, \"longitude\" DOUBLE, \"categoryId\" INTEGER NOT NULL  DEFAULT 0, \"subcategoryId\" INTEGER NOT NULL  DEFAULT 0, \"favorite\" BOOL NOT NULL  DEFAULT false)");
        db.execSQL("CREATE TABLE \"category\" (\"categoryId\" INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL  UNIQUE , \"name\" TEXT, \"description\" TEXT)");
        db.execSQL("CREATE TABLE \"subcategory\" (\"subcategoryId\" INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL  UNIQUE , \"categoryId\" INTEGER NOT NULL , \"name\" TEXT NOT NULL , \"description\" TEXT)");

        db.setTransactionSuccessful();
        db.endTransaction();

        Log.d(TAG, "DB created!");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }









}
