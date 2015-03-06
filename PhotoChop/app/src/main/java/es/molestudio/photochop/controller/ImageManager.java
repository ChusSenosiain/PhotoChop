package es.molestudio.photochop.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.location.Location;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.format.DateFormat;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;

import es.molestudio.photochop.controller.util.Log;
import es.molestudio.photochop.model.Image;

/**
 * Created by Chus on 05/03/15.
 */
public interface ImageManager {

    public interface ImageManagerListener {
        public void onFinish(Image image, Exception err);
    }

    public void hideImage(Image image, ImageManagerListener listener);

    public void showImage(Image image, ImageManagerListener listener);

    public long saveNewImageOnBD(Uri imageUri, Location location);

}
