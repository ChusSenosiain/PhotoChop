package es.molestudio.photochop.model;

import android.net.Uri;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Chus on 31/12/14.
 */
public class Image implements Serializable {

    private long mImageInternalId;
    private int mImageId;
    private String mImageName;
    private String mImageDescription;
    private String mImageUri;
    private Date mImageDate;
    private Double mImageLatitude;
    private Double mImageLongitude;
    private Category mImageCategory = new Category();
    private SubCategory mImageSubCategory = new SubCategory();
    private boolean mIsFavorite = false;
    private boolean mIsHidden = false;


    public Image() {}

    public int getImageId() {
        return mImageId;
    }

    public void setImageId(int imageId) {
        mImageId = imageId;
    }

    public String getImageName() {
        return mImageName;
    }

    public void setImageName(String imageName) {
        mImageName = imageName;
    }

    public String getImageDescription() {
        return mImageDescription;
    }

    public void setImageDescription(String imageDescription) {
        mImageDescription = imageDescription;
    }

    public Uri getImageUri() {
        return Uri.parse(mImageUri);
    }

    public void setImageUri(Uri imageUri) {
        mImageUri = imageUri.toString();
    }

    public Date getImageDate() {
        return mImageDate;
    }

    public void setImageDate(Date imageDate) {
        mImageDate = imageDate;
    }

    public Double getImageLatitude() {
        return mImageLatitude;
    }

    public void setImageLatitude(Double imageLatitude) {
        mImageLatitude = imageLatitude;
    }

    public Double getImageLongitude() {
        return mImageLongitude;
    }

    public void setImageLongitude(Double imageLongitude) {
        mImageLongitude = imageLongitude;
    }

    public Category getImageCategory() {
        return mImageCategory;
    }

    public void setImageCategory(Category imageCategory) {
        mImageCategory = imageCategory;
    }

    public SubCategory getImageSubCategory() {
        return mImageSubCategory;
    }

    public void setImageSubCategory(SubCategory imageSubCategory) {
        mImageSubCategory = imageSubCategory;
    }

    public long getImageInternalId() {
        return mImageInternalId;
    }

    public void setImageInternalId(long imageInternalId) {
        mImageInternalId = imageInternalId;
    }

    public void setImageUri(String imageUri) {
        mImageUri = imageUri;
    }

    public boolean isFavorite() {
        return mIsFavorite;
    }

    public void setFavorite(boolean favorite) {
        mIsFavorite = favorite;
    }

    public boolean isHidden() {
        return mIsHidden;
    }

    public void setHidden(boolean isHidden) {
        mIsHidden = isHidden;
    }
}
