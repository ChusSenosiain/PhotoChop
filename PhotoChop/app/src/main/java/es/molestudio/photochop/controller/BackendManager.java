package es.molestudio.photochop.controller;

import es.molestudio.photochop.model.Image;

/**
 * Created by Chus on 04/03/15.
 */
public interface BackendManager {

    public interface BackendImageListener {
        public void onDone(Image image, Exception err);
    }

    public interface ImageCountListener {
        public void onDone(Integer count, Exception err);
    }


    public void saveImage(Image image, BackendImageListener listener);

    public void deleteImage(Image image, BackendImageListener listener);

    public void getImage(Image image, BackendImageListener listener);

    public void countUserImages(ImageCountListener listener);

    public void countImageWithId(Image image, ImageCountListener listener);


}
