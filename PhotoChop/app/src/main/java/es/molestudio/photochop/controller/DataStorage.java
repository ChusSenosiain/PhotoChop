package es.molestudio.photochop.controller;

import android.content.Context;

/**
 * Created by Chus on 18/01/15.
 */
public class DataStorage {

    public static IDataManager getDataStorage(Context context) {
        return new SQLiteManager(context);
    }

}
