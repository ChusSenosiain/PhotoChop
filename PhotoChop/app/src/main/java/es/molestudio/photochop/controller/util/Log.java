package es.molestudio.photochop.controller.util;

import es.molestudio.photochop.model.Constants;

/**
 * Created by Chus on 23/02/15.
 */
public class Log {

    private static final String TAG = Constants.TAG_DEBUG;
    /**
     * Llama a Log.d y ya incluye el TAG para debug.
     * @param msg mensaje a incluir en el Log.dd
     */
    public static void d(String msg) {
        android.util.Log.d(TAG, msg);
    }

    public static void d(String tag, String msg) {
        android.util.Log.d(tag, msg);
    }

    public static void d(String tag, String msg, Exception e) {
        android.util.Log.d(tag, msg, e);
    }

    public static void d(String msg, Exception e) {
        android.util.Log.d(TAG, msg, e);
    }
}
