package es.molestudio.photochop.controller;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.OpenableColumns;

import com.google.common.io.Files;

import com.parse.CountCallback;
import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.List;

import es.molestudio.photochop.model.Image;

/**
 * Created by Chus on 04/03/15.
 */
public class BackendManagerWithParse implements BackendManager {

    private Context mContext;

    public BackendManagerWithParse(Context context) {
        mContext = context;
    }

    @Override
    public void saveImage(final Image image, final BackendImageListener listener) {

        try {
            final ParseObject parseImage = new ParseObject("Image");
            // 1. Verify if the image exist
            countImageWithId(image, new ImageCountListener() {
                @Override
                public void onDone(Integer count, Exception err) {

                    // The image does't exist
                    if (err == null && count == 0) {
                        try {

                            // Save the image

                            // Get the image file
                            byte[] fileData = Files.toByteArray(new File(image.getImageUri().getPath()));
                            ParseFile parseFile = new ParseFile("image", fileData);

                            parseImage.put("image", parseFile);
                            parseImage.put("userId", ParseUser.getCurrentUser().getObjectId());
                            parseImage.put("internalId", image.getImageInternalId());

                            parseImage.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {

                                    if (e != null) {
                                        image.setImageBackendId(parseImage.getObjectId());
                                        listener.onDone(image, null);
                                    } else {
                                        listener.onDone(null, e);
                                    }

                                }
                            });
                        } catch (Exception e) {
                            listener.onDone(null, e);
                        }
                    } else {

                        if (err == null) {
                            err = new Exception("The image already exists!");
                        }

                        listener.onDone(null, err);
                    }
                }
            });

        } catch (Exception e) {
            listener.onDone(null, e);
        }
    }


    @Override
    public void deleteImage(Image image, BackendImageListener listener) {
        // TODO: IMPLEMENTAR Y OJO, HAY QUE USAR LA API RES PARA ELIMINAR EL FICHERO
    }

    @Override
    public void getImage(final Image image, final BackendImageListener listener) {

        try {
            ParseQuery<ParseObject> getImage = ParseQuery.getQuery("Image");
            getImage.whereEqualTo("internalId", image.getImageInternalId());
            getImage.whereEqualTo("userId", ParseUser.getCurrentUser().getObjectId());

            getImage.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> parseObjects, ParseException e) {

                    if (e != null) {
                        for (int i = 0; i < parseObjects.size(); i++) {
                            downloadImage(parseObjects.get(i), listener);
                        }
                    } else {
                        listener.onDone(null, e);
                    }

                }
            });

        } catch (Exception e) {
            listener.onDone(null, e);
        }
    }


    @Override
    public void countUserImages(final ImageCountListener listener) {

        try {
            ParseQuery<ParseObject> imageCounter = ParseQuery.getQuery("Image");
            imageCounter.whereEqualTo("userId", ParseUser.getCurrentUser().getObjectId());

            imageCounter.countInBackground(new CountCallback() {
                @Override
                public void done(int i, ParseException e) {
                    listener.onDone(i,e);
                }
            });

        } catch (Exception e) {
            listener.onDone(null, e);
        }
    }


    @Override
    public void countImageWithId(Image image, final ImageCountListener listener) {

        try {

            ParseQuery<ParseObject> imageCounter = ParseQuery.getQuery("Image");
            imageCounter.whereEqualTo("userId", ParseUser.getCurrentUser().getObjectId());
            imageCounter.whereEqualTo("InternalId", image.getImageInternalId());

            imageCounter.countInBackground(new CountCallback() {
                @Override
                public void done(int i, ParseException e) {
                    listener.onDone(i, e);
                }
            });

        } catch (Exception e) {
            listener.onDone(null, e);
        }

    }

    private void downloadImage(ParseObject parseImage, final BackendImageListener listener) {

        try {

            ParseFile parseFile = (ParseFile) parseImage.get("image");
            parseFile.getDataInBackground(new GetDataCallback() {
                @Override
                public void done(byte[] bytes, ParseException e) {
                    if (e != null) {
                        Image image = saveImageFile(bytes);
                        if (image.getImageInternalId() > 0) {
                            listener.onDone(image, null);
                        } else {
                            listener.onDone(null, new Exception("Cant' create the image!"));
                        }

                    } else {
                        listener.onDone(null, e);
                    }
                }
            });

        } catch (Exception e) {
            listener.onDone(null, e);
        }

    }


    private Image saveImageFile(byte[] bytes) {

        Image image = null;

        try {
            File sdCardDirectory = Environment.getExternalStorageDirectory();
            File file = new File(sdCardDirectory, new Date().toString() + ".jpg");
            FileOutputStream fos = new FileOutputStream(file.getPath());
            fos.write(bytes);
            fos.close();


            // Obtengo el nombre de la imagen
            // SD Card

            String[] mColumns = {MediaStore.Images.Media._ID};
            Long id = null;
            Cursor result = mContext.getContentResolver().query(Uri.fromFile(file), mColumns, null, null, null);
            if (result.moveToFirst()) {
                do {
                    id = result.getLong(result.getColumnIndex(mColumns[0]));
                } while (result.moveToNext());
            }
            result.close();


            // Obtener uri y nuevo internal id

            image = new Image();
            image.setHidden(false);
            image.setImageUri(Uri.fromFile(file));
            image.setImageInternalId(id);


        } catch (Exception e) {}

        return image;

    }

}
