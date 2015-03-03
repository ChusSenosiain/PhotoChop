package es.molestudio.photochop.controller.util;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;

/**
 * Created by Chus on 03/03/15.
 */
public class ImageLoader {

    Context mContext;

    public ImageLoader(Context context) {
        mContext = context;
    }

    public void displayImage(final ImageView view, final Uri bitmapUri) {

        AsyncTask displayImage = new AsyncTask<Void, Void, Bitmap>() {
            @Override
            protected Bitmap doInBackground(Void... params) {
                Bitmap bitmap = reduceAndRotate(bitmapUri);
                return bitmap;
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                super.onPostExecute(bitmap);
                animateView(bitmap, view);
            }

        }.execute();

    }

    public void displayThumbnailImage(final ImageView view, final Long imageID) {

        AsyncTask displayThumbNail = new AsyncTask<Void, Void, Bitmap>() {
            @Override
            protected Bitmap doInBackground(Void... params) {
                Bitmap bitmap = getThumbnail(mContext.getContentResolver(), imageID);
                return bitmap;
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                super.onPostExecute(bitmap);
                animateView(bitmap, view);
            }

        }.execute();

    }

    private void animateView(Bitmap bitmap, ImageView view) {

        view.setImageBitmap(bitmap);
        AlphaAnimation fadeImage = new AlphaAnimation(0, 1);
        fadeImage.setDuration(500);
        fadeImage.setInterpolator(new DecelerateInterpolator());
        view.startAnimation(fadeImage);

    }



    private Bitmap getThumbnail(ContentResolver contentResolver, long id)
    {
        Cursor cursor = contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.Media.DATA}, // Which columns to return
                MediaStore.Images.Media._ID+ "=?",       // Which rows to return
                new String[]{String.valueOf(id)},       // Selection arguments
                null);// order

        if(cursor!=null && cursor.getCount()>0)
        {
            cursor.moveToFirst();
            String filepath = cursor.getString(0);
            cursor.close();

            Bitmap bitmap = MediaStore.Images.Thumbnails.getThumbnail(contentResolver, id, MediaStore.Images.Thumbnails.MICRO_KIND, null);


            int rotation = getRotation(filepath);
            if(rotation != 0)
            {
                Matrix matrix = new Matrix();
                matrix.setRotate(rotation);
                bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            }

            return bitmap;
        }
        else
        {
            return null;
        }
    }


    private int getRotation(String filePath) {

        int rotation = 0;

        try
        {
            ExifInterface exifInterface = new ExifInterface(filePath);
            int exifRotation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,ExifInterface.ORIENTATION_UNDEFINED);

            if(exifRotation!=ExifInterface.ORIENTATION_UNDEFINED)
            {
                switch(exifRotation)
                {
                    case ExifInterface.ORIENTATION_ROTATE_180 :
                        rotation = 180;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_270 :
                        rotation = 270;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_90 :
                        rotation = 90;
                        break;
                }
            }
        }
        catch(IOException e){}

        return rotation;

    }

    /**
     * Hace un resize de la imagen original en función del tamaño de la pantalla
     * del dispositivo y la rota si está tomada en apaisado
     * @param
     */

    private Bitmap reduceAndRotate(Uri imageUri) {

        Bitmap bitmap = null;
        File imageFile = new File(imageUri.getPath());

        try {
            // Reducir el tamaño del bitmap a tratar, puede ser enorme
            // y tira la aplicación al cargarlo

            DisplayMetrics metrics = new DisplayMetrics();
            ((WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(metrics);

            // Leer dimensiones y tipo
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(imageFile.getPath(), options);
            // Le doy las dimensiones en función de la pantalla del dispositivo / 2
            options.inSampleSize = calculateInSampleSize(options, metrics.widthPixels / 2, metrics.heightPixels / 2);

            options.inJustDecodeBounds = false;

            bitmap = BitmapFactory.decodeFile(imageFile.getPath(), options);

            int w = bitmap.getWidth();
            int h = bitmap.getHeight();
            Matrix mtx = new Matrix();

            // Se rota la imagen
            mtx.postRotate(getRotation(imageFile.getAbsolutePath()));
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, true);

        } catch (Exception e) {}

        return bitmap;
    }


    /**
     * Calcula la escala de la imagen en función del with y heigth requeridos
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return inSampleSize
     */
    private static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        Log.d("Anterior " + width + " " + height);

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }

        }

        return  inSampleSize;
    }


}
