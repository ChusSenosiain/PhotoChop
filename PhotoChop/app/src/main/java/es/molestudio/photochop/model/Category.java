package es.molestudio.photochop.model;

/**
 * Created by Chus on 31/12/14.
 */
public class Category {

    private int mCagetoryId;
    private String mCategoryName;
    private String mCategoryDescription;

    public Category(int cagetoryId, String categoryName, String categoryDescription) {
        mCagetoryId = cagetoryId;
        mCategoryName = categoryName;
        mCategoryDescription = categoryDescription;
    }

    public Category() {}

    public int getCagetoryId() {
        return mCagetoryId;
    }

    public void setCagetoryId(int cagetoryId) {
        mCagetoryId = cagetoryId;
    }

    public String getCategoryName() {
        return mCategoryName;
    }

    public void setCategoryName(String categoryName) {
        mCategoryName = categoryName;
    }

    public String getCategoryDescription() {
        return mCategoryDescription;
    }

    public void setCategoryDescription(String categoryDescription) {
        mCategoryDescription = categoryDescription;
    }
}
