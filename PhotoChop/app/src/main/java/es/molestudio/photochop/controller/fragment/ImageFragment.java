package es.molestudio.photochop.controller.fragment;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import es.molestudio.photochop.R;
import es.molestudio.photochop.controller.util.AppUtils;
import es.molestudio.photochop.model.Image;

/**
 * Created by Chus on 31/12/14.
 */
public class ImageFragment extends Fragment {


    private DisplayImageOptions mDisplayImageOptions = new DisplayImageOptions.Builder()
            .showImageOnLoading(null)
            .showImageForEmptyUri(null)
            .showImageOnFail(null)
            .cacheInMemory(false)
            .cacheOnDisk(true)
            .considerExifParams(true)
            .imageScaleType(ImageScaleType.EXACTLY)
            .build();


    public static final String ARG_IMAGE = "IMAGE";

    private Image mImage;


    public static Fragment newInstance(Bundle args) {

        ImageFragment fragment = new ImageFragment();
        fragment.setArguments(args);
        return fragment;
    }


    public ImageFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mImage = (Image) getArguments().getSerializable(ARG_IMAGE);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View root = inflater.inflate(R.layout.fragment_image, container, false);

        final ImageView ivImage = (ImageView) root.findViewById(R.id.image);
        TextView tvImageName = (TextView) root.findViewById(R.id.image_name);
        TextView tvImageDate = (TextView) root.findViewById(R.id.image_date);

        tvImageDate.setText(AppUtils.getStringFormatDate(mImage.getImageDate()));

        Cursor cursor =  getActivity().getContentResolver().query(mImage.getImageUri(), null, null, null, null);
        int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
        cursor.moveToFirst();
        tvImageName.setText(cursor.getString(nameIndex));
        cursor.close();

        ImageLoader.getInstance().displayImage(mImage.getImageUri().toString(), ivImage, mDisplayImageOptions, new SimpleImageLoadingListener() {
            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                super.onLoadingComplete(imageUri, view, loadedImage);
                Animation anim = AnimationUtils.loadAnimation(getActivity(), R.anim.abc_fade_in);
                ivImage.setAnimation(anim);
                anim.start();
            }
        });


        /*

        try {
            ivImage.setImageBitmap(reduceAndRotate(mImage.getImageUri()));
        } catch (Exception e) {
            e.printStackTrace();
        }*/


        return root;
    }


    private Bitmap reduceAndRotate(Uri imageUri) throws Exception{

        // Reducir el tamaño del bitmap a tratar, puede ser enorme
        // y tira la aplicación al cargarlo

        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);

        // Leer dimensiones y tipo
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(imageUri), null, options);

        // Le doy las dimensiones en función de la pantalla del dispositivo / 2
        options.inSampleSize = calculateInSampleSize(options, metrics.widthPixels / 2, metrics.heightPixels / 2);
        options.inJustDecodeBounds = false;

        Bitmap bitmap =  BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(imageUri), null, options);

        // Se rota la imagen
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        Matrix mtx = new Matrix();
        mtx.postRotate(getOrientation(imageUri));

        bitmap = Bitmap.createBitmap(bitmap,
                0, 0, w, h, mtx, true);

        return bitmap;
    }


    /**
     * Calcula la escala de la imagen en función del with y heigth requeridos
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return inSampleSize
     */
    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

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


    public int getOrientation(Uri imageUri) {

        Cursor cursor = getActivity().getContentResolver().query(imageUri,
                new String[] { MediaStore.Images.ImageColumns.ORIENTATION }, null, null, null);

        if (cursor.getCount() != 1) {
            return -1;
        }

        cursor.moveToFirst();
        return cursor.getInt(0);
    }






}
