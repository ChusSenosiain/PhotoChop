package es.molestudio.photochop.controller.util;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;

import java.io.File;
import java.sql.Date;
import java.util.ArrayList;

import es.molestudio.photochop.model.Image;

/**
 * Created by Chus on 27/02/15.
 */
public class GetImagesFromGalleryTask extends AsyncTask<Void, ArrayList<Image>, ArrayList<Image>> {

    public interface ImageReaderListener {
        public void onProgressUpdate(ArrayList<Image> image);
        public void onFinish(ArrayList<Image> image);
    }


    private Context mContext;
    private ImageReaderListener mImageReaderListener;
    private String[] mColumns = {MediaStore.Images.Media.DATA,
                                MediaStore.Images.Media.DISPLAY_NAME,
                                MediaStore.Images.Media.DATE_TAKEN,
                                MediaStore.Images.Media.LATITUDE,
                                MediaStore.Images.Media.LONGITUDE};

    public GetImagesFromGalleryTask(Context context, ImageReaderListener listener) {
        mContext = context;
        mImageReaderListener = listener;
    }


    @Override
    protected ArrayList<Image> doInBackground(Void... params) {

        // externa e internal


        ArrayList<Image> images = new ArrayList<>();

        final String orderBy = MediaStore.Images.Media.DATE_TAKEN;
        int imageCount = 0;

        // SD Card
        Cursor result = mContext.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, this.mColumns, null, null, orderBy + " DESC");
        if (result.moveToFirst()) {
            do {
                images.add(createImage(result));
            } while (result.moveToNext());
        }
        result.close();

        // Internal storage
        /*
        result = mContext.getContentResolver().query(MediaStore.Images.Media.INTERNAL_CONTENT_URI, this.mColumns, null, null, orderBy + " DESC");
        if (result.moveToFirst()) {
            do {
                images.add(createImage(result));
            } while (result.moveToNext());
        }
        result.close();*/


        return images;
    }


    @Override
    protected void onPostExecute(ArrayList<Image> images) {
        super.onPostExecute(images);
        mImageReaderListener.onFinish(images);
    }

    @Override
    protected void onProgressUpdate(ArrayList<Image>... values) {
        super.onProgressUpdate(values);
    }


    private Image createImage(Cursor result) {

        String path;
        String name;
        String date;
        Double lat;
        Double lon;

        path = result.getString(result.getColumnIndex(mColumns[0]));
        name = result.getString(result.getColumnIndex(mColumns[1]));
        date = result.getString(result.getColumnIndex(mColumns[2]));
        lat  = result.getDouble(result.getColumnIndex(mColumns[3]));
        lon  = result.getDouble(result.getColumnIndex(mColumns[4]));

        Image image = new Image();
        image.setImageUri(Uri.fromFile(new File(path)));
        image.setImageName(name);
        image.setImageLatitude(lat);
        image.setImageLongitude(lon);

        try {
            image.setImageDate(new Date(Integer.valueOf(date)));
        } catch (Exception e) {}

        return image;

    }
}
