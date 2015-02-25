package es.molestudio.photochop.controller.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import es.molestudio.photochop.R;
import es.molestudio.photochop.controller.DataStorage;
import es.molestudio.photochop.controller.activity.MapActivity;
import es.molestudio.photochop.controller.util.AppUtils;
import es.molestudio.photochop.model.Image;
import es.molestudio.photochop.model.enumerator.ActionType;

/**
 * Created by Chus on 31/12/14.
 */
public class ImageFragment extends Fragment implements View.OnClickListener {


    private DisplayImageOptions mDisplayImageOptions = new DisplayImageOptions.Builder()
            .showImageOnLoading(null)
            .showImageForEmptyUri(null)
            .showImageOnFail(null)
            .cacheInMemory(false)
            .cacheOnDisk(true)
            .considerExifParams(true)
            .imageScaleType(ImageScaleType.EXACTLY)
            .build();

    private OnImageUpdateListener mListener;


    public interface OnImageUpdateListener {
        public void onImageUpdate(Bundle imageUpdated);
    }

    // Fragment Arguments
    public static final String ARG_IMAGE_ID = "es.molestudio.photochop.controller.fragment.ImageFragment.IMAGE_ID";
    public static final String ARG_POSITION = "es.molestudio.photochop.controller.fragment.ImageFragment.POSITION";

    // Bundle return value to the OnImageUpdateListener
    public static final String IMAGE_ID = "es.molestudio.photochop.controller.fragment.ImageFragment.IMAGE_ID";
    public static final String POSITION = "es.molestudio.photochop.controller.fragment.ImageFragment.POSITION";
    public static final String ACTION_TYPE = "es.molestudio.photochop.controller.fragment.ImageFragment.ACTION_TYPE";

    private Image mImage;
    private Integer mImageID;
    private Integer mPosition;

    private ImageView mIvFavorite;
    private ViewGroup mImageOptions;

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

        if (getArguments() != null) {
            mImageID = getArguments().getInt(ARG_IMAGE_ID, 0);
            mPosition = getArguments().getInt(ARG_POSITION);
        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        ((ActionBarActivity)getActivity()).getSupportActionBar().hide();

        View root = inflater.inflate(R.layout.fragment_image, container, false);

        ImageView ivImage = (ImageView) root.findViewById(R.id.iv_image);
        TextView tvImageName = (TextView) root.findViewById(R.id.image_name);
        TextView tvImageDate = (TextView) root.findViewById(R.id.image_date);
        mImageOptions = (LinearLayout) root.findViewById(R.id.image_data_holder);
        mIvFavorite = (ImageView) root.findViewById(R.id.iv_favorite);
        ImageView ivDelete = (ImageView) root.findViewById(R.id.iv_delete);
        ImageView ivEdit = (ImageView) root.findViewById(R.id.iv_edit);
        ImageView ivLocalize = (ImageView) root.findViewById(R.id.iv_localize);

        ivImage.setOnClickListener(this);
        mImageOptions.setOnClickListener(this);
        mIvFavorite.setOnClickListener(this);
        ivDelete.setOnClickListener(this);
        ivEdit.setOnClickListener(this);
        ivLocalize.setOnClickListener(this);


        mImage = DataStorage.getDataStorage(getActivity()).selectImage(mImageID);

        if (mImage != null) {
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
                    ((ImageView) view).setAnimation(anim);
                    anim.start();
                }
            });

            checkAsFavorite();

        }

        return root;
    }


    /**
     * Color the heart with red if the image is checked as favorite
     * or leave the heart with its original color if not
     */
    private void checkAsFavorite() {

        if (mImage.isFavorite()) {

            int resource = R.drawable.ic_action_favorite;

            Drawable selectedDrawable = getResources().getDrawable(resource).getConstantState().newDrawable();
            selectedDrawable.mutate();
            selectedDrawable.setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);

            mIvFavorite.setImageDrawable(selectedDrawable);

        } else {
            mIvFavorite.setImageResource(R.drawable.ic_action_favorite);
        }

    }


    /**
     * Hide and show action bar and image options
     */
    private void showActions() {

        ActionBar actionBar = ((ActionBarActivity) getActivity()).getSupportActionBar();

        if (actionBar.isShowing()) {
            actionBar.hide();
            Animation hideImageOptions = AnimationUtils.loadAnimation(getActivity(), R.anim.bottom_down);
            mImageOptions.startAnimation(hideImageOptions);
            mImageOptions.setVisibility(View.GONE);
        } else {
            actionBar.show();
            Animation showImageOptions = AnimationUtils.loadAnimation(getActivity(), R.anim.bottom_up);
            mImageOptions.startAnimation(showImageOptions);
            mImageOptions.setVisibility(View.VISIBLE);
        }
    }



    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.iv_image:
                showActions();
                break;
            case R.id.iv_delete:
                showDeleteImageDialog();
                break;
            case R.id.iv_favorite:
                tagImageAsFavorite();
                break;
            case R.id.iv_edit:
                showEditDialog();
                break;
            case R.id.iv_localize:
                showMap();
                break;

        }
    }

    private void showEditDialog() {

    }


    /**
     *  Shows a question dialog to ask if the user really wants to delete the image
     */
    private void showDeleteImageDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(getActivity().getString(R.string.delete_image_question))
                .setTitle(getActivity().getString(R.string.delete_image));

        builder.setPositiveButton(getActivity().getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteImage();
            }
        });

        builder.setNegativeButton(getActivity().getString(R.string.no), null);

        AlertDialog dialog = builder.create();
        dialog.show();


    }

    private void showMap() {
        Intent activityMap = new Intent(getActivity(), MapActivity.class);
        activityMap.putExtra(MapActivity.IMAGE, mImage);
        startActivityForResult(activityMap, MapActivity.RQ_LOCATION_SELECTED);
    }

    /**
     * Delete image on db and notify to the listener that the image
     * was deleted
     */
    private void deleteImage() {

        int result = DataStorage.getDataStorage(getActivity()).deleteImage(mImage);
        if (result > 0) {
            // Delete the image in the image list of the GalleryActivity
            if (mListener != null) {
                Bundle imageUpdated = new Bundle();
                imageUpdated.putInt(IMAGE_ID, mImage.getImageId());
                imageUpdated.putInt(POSITION, mPosition);
                imageUpdated.putSerializable(ACTION_TYPE, ActionType.DELETE);
                mListener.onImageUpdate(imageUpdated);
            }
        }
    }

    /**
     * Update the image's favorite field on bd and update the view
     */
    private void tagImageAsFavorite() {

        mImage.setFavorite(!mImage.isFavorite());

        int result = DataStorage.getDataStorage(getActivity()).updateImage(mImage);

        // If the result is > 0, update the view
        if (result > 0) {
            checkAsFavorite();
        // If the result is <= 0 (update failed) return image to the older state
        } else {
            mImage.setFavorite(!mImage.isFavorite());
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {

            if (requestCode == MapActivity.RQ_LOCATION_SELECTED) {
                Location location = (Location) data.getSerializableExtra(MapActivity.NEW_LOCATION);
                mImage.setImageLatitude(location.getLatitude());
                mImage.setImageLongitude(location.getLongitude());

                DataStorage.getDataStorage(getActivity()).updateImage(mImage);

            }
        }

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnImageUpdateListener) activity;

        } catch (Exception e) {}
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


}
