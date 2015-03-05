package es.molestudio.photochop.controller;

import android.content.Context;

/**
 * Created by Chus on 18/01/15.
 */
public class DataBaseManagerWrap {

    public static DataBaseManager getDataBaseManager(Context context) {
        return new DataBaseManagerWithSQLite(context);
    }

}
