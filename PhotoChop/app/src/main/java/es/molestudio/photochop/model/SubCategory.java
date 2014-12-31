package es.molestudio.photochop.model;

/**
 * Created by Chus on 31/12/14.
 */
public class SubCategory {

    private int mSubCategoryId;
    private int mCategoryId;
    private String mSubCaregoryName;
    private String mSubCategoryDescription;


    public SubCategory(int subCategoryId, int categoryId, String subCaregoryName, String subCategoryDescription) {
        mSubCategoryId = subCategoryId;
        mCategoryId = categoryId;
        mSubCaregoryName = subCaregoryName;
        mSubCategoryDescription = subCategoryDescription;
    }

    public SubCategory() {
    }


    public int getSubCategoryId() {
        return mSubCategoryId;
    }

    public void setSubCategoryId(int subCategoryId) {
        mSubCategoryId = subCategoryId;
    }

    public int getCategoryId() {
        return mCategoryId;
    }

    public void setCategoryId(int categoryId) {
        mCategoryId = categoryId;
    }

    public String getSubCaregoryName() {
        return mSubCaregoryName;
    }

    public void setSubCaregoryName(String subCaregoryName) {
        mSubCaregoryName = subCaregoryName;
    }

    public String getSubCategoryDescription() {
        return mSubCategoryDescription;
    }

    public void setSubCategoryDescription(String subCategoryDescription) {
        mSubCategoryDescription = subCategoryDescription;
    }
}
