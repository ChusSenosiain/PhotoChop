package es.molestudio.photochop.controller.util;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;

import es.molestudio.photochop.model.Constants;

/**
 * Created by Chus on 24/02/15.
 */
public class CreateThumbnailFromImageTask extends AsyncTask<String, Void, Bitmap> {

    private Integer mWidth = Constants.THUMBNAIL_DEFAULT_WIDTH;
    private Integer mHeigth = Constants.THUMBNAIL_DEFAULT_HEIGTH;
    private ThumbCreationListener mListener;
    private Exception mException = null;
    private Context mContext;

    public interface ThumbCreationListener {
        public void onImageCreated(Bitmap bitmap, Exception error);
    }

    public CreateThumbnailFromImageTask(Context context, ThumbCreationListener listener) {
        mListener = listener;
        mContext = context;
    }

    public CreateThumbnailFromImageTask(Context context, Integer width, Integer heigth, ThumbCreationListener listener) {
        mWidth = width;
        mHeigth = heigth;
        mListener = listener;
        mContext = context;
    }

    @Override
    protected Bitmap doInBackground(String... params) {

        Bitmap roundThumbnail = null;

        try {
            String imageUri = params[0];
            Uri uri = Uri.parse(imageUri);

            String result;
            Cursor cursor = mContext.getContentResolver().query(uri, null, null, null, null);
            if (cursor == null) { // Source is Dropbox or other similar local file path
                result = uri.getPath();
            } else {
                cursor.moveToFirst();
                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                result = cursor.getString(idx);
            }

            cursor.close();

            // Obtengo el thumnail
            Bitmap thumbnail = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(result), mWidth, mHeigth);

            // Lo modifico para que sea redondo
            Bitmap roundedThumbnail = Bitmap.createBitmap(thumbnail.getWidth(),
                    thumbnail.getHeight(), Bitmap.Config.ARGB_8888);

            Canvas canvas = new Canvas(roundedThumbnail);
            final int color = 0xff424242;
            final Paint paint = new Paint();

            final Rect rect = new Rect(0, 0, thumbnail.getWidth(), thumbnail.getHeight());

            paint.setAntiAlias(true);
            canvas.drawARGB(0, 0, 0, 0);
            paint.setColor(color);

            canvas.drawCircle(thumbnail.getWidth() / 2, thumbnail.getHeight() / 2,
                    thumbnail.getWidth() / 2, paint);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

            canvas.drawBitmap(thumbnail, rect, rect, paint);

            // Le añado un marco
            int w = roundedThumbnail.getWidth();
            int h = roundedThumbnail.getHeight();

            int radius = Math.min(h / 2, w / 2);
            Bitmap roundedFrameBitmap = Bitmap.createBitmap(w + 8, h + 8, Bitmap.Config.ARGB_8888);

            Paint p = new Paint();
            p.setAntiAlias(true);

            Canvas c = new Canvas(roundedFrameBitmap);
            c.drawARGB(0, 0, 0, 0);
            p.setStyle(Paint.Style.FILL);

            c.drawCircle((w / 2) + 4, (h / 2) + 4, radius, p);

            p.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

            c.drawBitmap(roundedThumbnail, 4, 4, p);
            p.setXfermode(null);
            p.setStyle(Paint.Style.STROKE);
            p.setColor(Color.WHITE);
            p.setStrokeWidth(5);
            c.drawCircle((w / 2) + 4, (h / 2) + 4, radius, p);


            roundThumbnail = roundedFrameBitmap;


        } catch (Exception e) {
            mException = e;
        }

        return roundThumbnail;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        mListener.onImageCreated(bitmap, mException);
    }
}
