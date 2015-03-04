package es.molestudio.photochop.controller;

import android.content.Context;

/**
 * Created by Chus on 04/03/15.
 */
public class ImageManager {

    public static IImageManager getImageManager(Context context) {
        return new ParseImageManager(context);
    }
}
