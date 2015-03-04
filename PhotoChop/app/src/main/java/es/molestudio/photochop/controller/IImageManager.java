package es.molestudio.photochop.controller;

import es.molestudio.photochop.model.Image;

/**
 * Created by Chus on 04/03/15.
 */
public interface IImageManager {

    public interface ImageListener {
        public void onDone(Image image, Exception err);
    }

    public void saveImage(Image image, ImageListener listener);

    public void updateImage(Image image, ImageListener listener);

    public void deleteImage(Image image, ImageListener listener);

    public Image getImage(Image image, ImageListener listener);

}
