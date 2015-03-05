package es.molestudio.photochop.controller;

import android.content.Context;

/**
 * Created by Chus on 04/03/15.
 */
public class BackendManagerWrap {

    public static BackendManager getBackendManager(Context context) {
        return new BackendManagerWithParse(context);
    }
}
