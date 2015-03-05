package es.molestudio.photochop.controller;

import android.content.Context;
import android.widget.Toast;

import com.parse.ParseUser;

import es.molestudio.photochop.R;
import es.molestudio.photochop.model.Image;

/**
 * Created by Chus on 05/03/15.
 */
public class ImageManagerImpl implements ImageManager {

    private Context mContext;
    private BackendManager mBackendManager;
    private DataBaseManager mDataBaseManager;
    private Image mImage;

    public ImageManagerImpl(Context context) {
        mContext = context;
        mBackendManager = BackendManagerWrap.getBackendManager(mContext);
        mDataBaseManager = DataBaseManagerWrap.getDataBaseManager(mContext);
    }


    @Override
    public void hideImage(Image image, final ImageManagerListener listener) {

        mImage = image;

        if (image.isHidden()) {
            // TODO: codificar error
            listener.onFinish(null, new Exception("The image is already hidden!"));
            return;
        }

        try {
            // 1. Save image on backend
            mBackendManager.saveImage(image, new BackendManager.BackendImageListener() {
                @Override
                public void onDone(Image image, Exception err) {
                    if (err == null && image != null) {
                        image.setHidden(true);
                        // 2. Update the image on DB
                        int result = mDataBaseManager.updateImage(image);
                        if (result > 0) {
                            listener.onFinish(image, null);
                        } else {
                            listener.onFinish(null, null);
                        }
                    } else {
                        listener.onFinish(null, err);
                    }
                }
            });
        } catch (Exception e) {
            listener.onFinish(null, e);
        }

    }

    @Override
    public void showImage(Image image, final ImageManagerListener listener) {

        if (!image.isHidden()) {
            // TODO: codificar error
            listener.onFinish(null, new Exception("The image is already in your phone!"));
            return;
        }

        try {
            // 1. Get the image from backend
            mBackendManager.getImage(image, new BackendManager.BackendImageListener() {
                @Override
                public void onDone(Image image, Exception err) {

                    if (err == null && image != null) {
                        // 2. Update the image on DB
                        mImage.setHidden(false);
                        mImage.setImageInternalId(image.getImageInternalId());
                        mImage.setImageUri(image.getImageUri());

                        int result = mDataBaseManager.updateImage(image);
                        if (result > 0) {
                            listener.onFinish(mImage, null);
                        } else {
                            listener.onFinish(null, null);
                        }
                    } else {
                        listener.onFinish(null, err);
                    }
                }
            });
        } catch (Exception e) {
            listener.onFinish(null, e);
        }


    }
}
