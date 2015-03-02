package es.molestudio.photochop.model;

import java.io.Serializable;

/**
 * Created by Chus on 31/12/14.
 */
public class SubCategory extends Selectable implements Serializable {

    private int mCategoryId;

    public SubCategory(int subCategoryId, int categoryId, String subCaregoryName, String subCategoryDescription) {
        super(subCategoryId, subCaregoryName, subCategoryDescription);
        mCategoryId = categoryId;
    }

    public SubCategory() {
        super();
    }

    public int getCategoryId() {
        return mCategoryId;
    }

    public void setCategoryId(int categoryId) {
        mCategoryId = categoryId;
    }

}
