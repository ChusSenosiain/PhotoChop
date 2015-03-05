package es.molestudio.photochop.controller;

import android.content.Context;

import java.util.ArrayList;

import es.molestudio.photochop.model.Image;

/**
 * Created by Chus on 04/03/15.
 */
public class Images {

    private static ArrayList<Image> mImages;

    public static synchronized ArrayList<Image> getInstance(Context context) {

        if (mImages == null) {
            mImages = DataBaseManagerWrap.getDataBaseManager(context).getImages();
        }

        return mImages;
    }

    public static synchronized ArrayList<Image> reloadImagesFromBD(Context context) {
        mImages = DataBaseManagerWrap.getDataBaseManager(context).getImages();
        return mImages;
    }

    public static synchronized void reloadImages(ArrayList<Image> images) {
        mImages = images;
    }

}
