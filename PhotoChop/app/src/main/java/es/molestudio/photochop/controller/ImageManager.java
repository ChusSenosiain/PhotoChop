package es.molestudio.photochop.controller;

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
}
