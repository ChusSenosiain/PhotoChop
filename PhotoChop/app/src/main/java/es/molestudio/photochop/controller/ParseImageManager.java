package es.molestudio.photochop.controller;

import android.content.Context;

import com.google.common.io.Files;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;

import es.molestudio.photochop.model.Image;

/**
 * Created by Chus on 04/03/15.
 */
public class ParseImageManager implements IImageManager {

    private Context mContext;

    public ParseImageManager(Context context) {
        mContext = context;
    }


    @Override
    public void saveImage(final Image image, final ImageListener listener) {

        try {
            final ParseObject parseImage = new ParseObject("Image");
            parseImage.put("User", ParseUser.getCurrentUser());

            // Obtengo el fichero
            byte[] fileData = Files.toByteArray(new File(image.getImageUri().getPath()));
            ParseFile parseFile = new ParseFile("image", fileData);
            parseImage.put("image", parseFile);

            parseImage.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    image.setImageBackendId(parseImage.getObjectId());
                    listener.onDone(image, e);
                }
            });

        } catch (Exception e) {

        }
    }


    @Override
    public void updateImage(Image image, ImageListener listener) {

    }

    @Override
    public void deleteImage(Image image, ImageListener listener) {

    }

    @Override
    public Image getImage(Image image, ImageListener listener) {
        return null;
    }
}
