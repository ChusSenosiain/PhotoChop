package es.molestudio.photochop.controller;

import android.content.Context;
import android.database.Cursor;
import android.location.Location;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.OpenableColumns;

import java.io.File;
import java.util.Date;

import es.molestudio.photochop.controller.util.Log;
import es.molestudio.photochop.model.Image;

/**
 * Created by Chus on 05/03/15.
 */
public class ImageManagerImpl implements ImageManager {

    private Context mContext;
    private BackendManager mBackendManager;
    private DataBaseManager mDataBaseManager;
    private Image mImage;

    public ImageManagerImpl(Context context) {
        mContext = context;
        mBackendManager = BackendManagerWrap.getBackendManager(mContext);
        mDataBaseManager = DataBaseManagerWrap.getDataBaseManager(mContext);
    }


    @Override
    public void hideImage(Image image, final ImageManagerListener listener) {

        mImage = image;

        if (image.isHidden()) {
            // TODO: codificar error
            listener.onFinish(null, new Exception("The image is already hidden!"));
            return;
        }

        try {
            // 1. Save image on backend
            mBackendManager.saveImage(image, new BackendManager.BackendImageListener() {
                @Override
                public void onDone(Image image, Exception err) {

                    boolean ok = false;

                    if (err == null && image != null) {
                        image.setHidden(true);
                        // 2. Update the image on DB
                        int result = mDataBaseManager.updateImage(image);
                        if (result > 0) {
                            // 3. DELETE IMAGE ON PHONE!!
                            ok = deleteImageFromGallery(image);
                        }
                    }

                    if (ok) {
                        listener.onFinish(image, null);
                    } else {
                        listener.onFinish(null, err);
                    }

                }
            });
        } catch (Exception e) {
            listener.onFinish(null, e);
        }

    }

    @Override
    public void showImage(Image image, final ImageManagerListener listener) {

        mImage = image;

        if (!image.isHidden()) {
            // TODO: codificar error
            listener.onFinish(null, new Exception("The image is already in your phone!"));
            return;
        }

        try {
            // 1. Get the image from backend
            mBackendManager.getImage(image, new BackendManager.BackendImageListener() {
                @Override
                public void onDone(Image image, Exception err) {

                    if (err == null && image != null) {
                        Log.d("Finish getImage from backend with output ok");
                        // 2. Update the image on DB
                        mImage.setHidden(false);
                        mImage.setImageInternalId(image.getImageInternalId());
                        mImage.setImageUri(image.getImageUri());

                        Log.d("Saving image on DB...");
                        int result = mDataBaseManager.updateImage(mImage);
                        if (result > 0) {
                            Log.d("Image saved!!");
                            listener.onFinish(mImage, null);
                        } else {
                            listener.onFinish(null, null);
                            Log.d("ERROR on updateImage on BD!");
                        }
                    } else {
                        Log.d("Finish getImage from backend with output ERROR");
                        listener.onFinish(null, err);
                    }
                }
            });
        } catch (Exception e) {
            listener.onFinish(null, e);
        }
    }


    private boolean deleteImageFromGallery(Image image) {

        boolean deleted = false;

        try {

            mContext.getContentResolver().delete(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    MediaStore.Images.Media.DATA
                            + "='"
                            + image.getImageUri().getPath()
                            + "'", null);

            File file = new File(mImage.getImageUri().getPath());
            deleted = file.delete();

        } catch (Exception e) {
            e.printStackTrace();

        }

        return deleted;

    }


    @Override
    public long saveNewImageOnBD(Uri imageUri, Location location) {

        long imageId = 0;

        Image image = null;

        try {

            String[] colums = {MediaStore.Images.Media.DATA,
                    MediaStore.Images.Media.DISPLAY_NAME,
                    MediaStore.Images.Media.DATE_TAKEN,
                    MediaStore.Images.Media.LATITUDE,
                    MediaStore.Images.Media.LONGITUDE,
                    MediaStore.Images.Media._ID};


            // Get the real path from uri
            String path = "";

            Cursor cursor = null;
            String[] columnsForPath = { MediaStore.Images.Media.DATA };
            cursor = mContext.getContentResolver().query(imageUri,  columnsForPath, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            path =  cursor.getString(column_index);
            cursor.close();

            // Get the file data
            Cursor result = mContext.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, colums, MediaStore.Images.Media.DATA + "=?", new String[] {path}, null);
            result.moveToFirst();
            image = createImage(result, colums);
            result.close();

            if (location != null) {
                image.setImageLatitude(location.getLatitude());
                image.setImageLongitude(location.getLongitude());
            } else {
                image.setImageLatitude(0.0);
                image.setImageLongitude(0.0);
            }

            imageId = DataBaseManagerWrap.getDataBaseManager(mContext).insertImage(image);

        } catch (Exception e) {
            Log.d("Cant't save the new image on BD!");
        }

        return imageId;
    }

    private Image createImage(Cursor result, String[] mColumns) {

        String path;
        String name;
        String date;
        Double lat;
        Double lon;
        Long id;

        path = result.getString(result.getColumnIndex(mColumns[0]));
        name = result.getString(result.getColumnIndex(mColumns[1]));
        date = result.getString(result.getColumnIndex(mColumns[2]));
        lat  = result.getDouble(result.getColumnIndex(mColumns[3]));
        lon  = result.getDouble(result.getColumnIndex(mColumns[4]));
        id   = result.getLong(result.getColumnIndex(mColumns[5]));

        Image image = new Image();
        image.setImageUri(Uri.fromFile(new File(path)));
        image.setImageName(name);
        image.setImageLatitude(lat);
        image.setImageLongitude(lon);
        image.setImageInternalId(id);

        try {
            image.setImageDate(new java.sql.Date(Long.valueOf(date)));
        } catch (Exception e) {}

        return image;

    }
}
