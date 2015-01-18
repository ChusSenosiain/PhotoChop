package es.molestudio.photochop.controller;

import java.util.ArrayList;

import es.molestudio.photochop.model.Image;

/**
 * Created by Chus on 18/01/15.
 */
public interface IDataStorage {

    public long insertImage(Image image);

    public ArrayList<Image> getImages();

    public ArrayList<Integer> getImagesIds();

    public int updateImage(Image image);

    public int deleteImage(Image image);

    public Image selectImage(Integer imageId);

}
