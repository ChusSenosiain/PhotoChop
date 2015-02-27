package es.molestudio.photochop.controller;

import java.util.ArrayList;

import es.molestudio.photochop.model.Category;
import es.molestudio.photochop.model.Image;
import es.molestudio.photochop.model.SubCategory;

/**
 * Created by Chus on 18/01/15.
 */
public interface IDataManager {


    // Image
    public long insertImage(Image image);

    public ArrayList<Image> insertImages(ArrayList<Image> images);

    public ArrayList<Image> getImages();

    public ArrayList<Integer> getImagesIds();

    public int updateImage(Image image);

    public int deleteImage(Image image);

    public Image selectImage(Integer imageId);


    // Category
    public long insertCategory(Category category);

    public ArrayList<Category> getCategories();

    public int updateCategory(Category category);

    public int deleteCategory(Category category);

    public Category selectCategory(Integer categoryId);


    // Subcategory

    public long insertSubCategory(SubCategory subCategory);

    public ArrayList<SubCategory> getSubCategories();

    public ArrayList<SubCategory> getSubCategories(Integer categoryId);

    public int updateSubCategory(SubCategory subCategory);

    public int deleteSubCategory(SubCategory subCategory);

    public Category selectSubCategory(Integer subCategoryId);





}
