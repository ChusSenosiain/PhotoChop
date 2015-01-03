package es.molestudio.photochop.model;

import android.net.Uri;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Chus on 31/12/14.
 */
public class Image implements Serializable {

    private int mImageId;
    private String mImageName;
    private String mImageDescription;
    private String mImageUri;
    private Date mImageDate;
    private Double mImageLatitude;
    private Double mImageLongitude;
    private int mImageCategory;
    private int mImageSubCategory;

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

    public int getImageCategory() {
        return mImageCategory;
    }

    public void setImageCategory(int imageCategory) {
        mImageCategory = imageCategory;
    }

    public int getImageSubCategory() {
        return mImageSubCategory;
    }

    public void setImageSubCategory(int imageSubCategory) {
        mImageSubCategory = imageSubCategory;
    }
}
