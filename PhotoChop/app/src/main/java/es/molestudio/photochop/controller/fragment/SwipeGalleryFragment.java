package es.molestudio.photochop.controller.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;

import es.molestudio.photochop.R;
import es.molestudio.photochop.View.AppTextView;
import es.molestudio.photochop.controller.DataBaseManagerWrap;
import es.molestudio.photochop.controller.Images;
import es.molestudio.photochop.controller.activity.ImageDetailsActivity;
import es.molestudio.photochop.controller.activity.MapActivity;
import es.molestudio.photochop.controller.util.AppUtils;
import es.molestudio.photochop.model.Image;


public class SwipeGalleryFragment extends Fragment implements View.OnClickListener,
        ViewPager.OnPageChangeListener {

    public interface ChangeImageListener {
        public void onImageChange();
    }

    public static final String ARG_IMAGE_POSITION = "es.molestudio.photochop.controller.activity.GalleryActivity.EXTRA_IMAGE_POSITION";

    private Integer mImagePosition;
    private ArrayList<Image> mImages = new ArrayList<Image>();

    private ImagesPagerAdapter mImagesPagerAdapter;
    private ViewPager mImagesViewPager;
    private ViewGroup mImageOptions;
    private ImageView mIvFavorite;
    private Image mImage;
    private AppTextView mTvImageName;
    private AppTextView mTvImageDate;
    private ActionBar mActionBar;
    private ChangeImageListener mChangeImageListener;

    public static Fragment newInstance(Bundle args) {
        SwipeGalleryFragment fragment = new SwipeGalleryFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public SwipeGalleryFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        if (getArguments() != null) {
            mImagePosition = getArguments().getInt(ARG_IMAGE_POSITION, 0);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View root = inflater.inflate(R.layout.fragment_swipe_gallery, container, false);

        mImageOptions = (LinearLayout) root.findViewById(R.id.image_data_holder);
        mImagesViewPager = (ViewPager) root.findViewById(R.id.vp_images);
        mImageOptions = (LinearLayout) root.findViewById(R.id.image_data_holder);
        mIvFavorite = (ImageView) root.findViewById(R.id.iv_favorite);

        mTvImageName = (AppTextView) root.findViewById(R.id.image_name);
        mTvImageDate = (AppTextView) root.findViewById(R.id.image_date);
        ImageView ivDelete = (ImageView) root.findViewById(R.id.iv_delete);
        ImageView ivEdit = (ImageView) root.findViewById(R.id.iv_edit);
        ImageView ivLocalize = (ImageView) root.findViewById(R.id.iv_localize);

        mActionBar = ((ActionBarActivity) getActivity()).getSupportActionBar();
        mActionBar.setShowHideAnimationEnabled(true);

        mImageOptions.setOnClickListener(this);
        mIvFavorite.setOnClickListener(this);
        ivDelete.setOnClickListener(this);
        ivEdit.setOnClickListener(this);
        ivLocalize.setOnClickListener(this);

        final GestureDetectorCompat tapGestureDetector = new GestureDetectorCompat(getActivity(), new TapGestureListener());

        mImagesViewPager.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                tapGestureDetector.onTouchEvent(event);
                return false;
            }
        });

        mImages = Images.getInstance(getActivity());

        mImage = mImages.get(mImagePosition);

        mImagesPagerAdapter = new ImagesPagerAdapter(getChildFragmentManager());
        mImagesViewPager.setAdapter(mImagesPagerAdapter);

        mImagesViewPager.setCurrentItem(mImagePosition);
        mImagesViewPager.setOnPageChangeListener(this);


        return root;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.iv_delete:
                showDeleteImageDialog();
                break;
            case R.id.iv_favorite:
                tagImageAsFavorite();
                break;
            case R.id.iv_edit:
                editImage();
                break;
            case R.id.iv_localize:
                showMap();
                break;
        }

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

    private void editImage() {

        Intent imageDetails = new Intent(getActivity(), ImageDetailsActivity.class);
        imageDetails.putExtra(ImageDetailsActivity.EXTRA_IMAGE_ID, mImage.getImageId());
        startActivityForResult(imageDetails, ImageDetailsActivity.RQ_IMAGE_UPDATED);

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

        int result = DataBaseManagerWrap.getDataBaseManager(getActivity()).deleteImage(mImage);
        if (result > 0) {
            sendResultToListener();
            mImages.remove(mImagePosition);
            if (mImagePosition != 0) {
                mImagesViewPager.setCurrentItem(mImagePosition - 1, true);
            } else {
                mImagesViewPager.setCurrentItem(0);
            }
        }

    }

    /**
     * Update the image's favorite field on bd and update the view
     */
    private void tagImageAsFavorite() {

        mImage.setFavorite(!mImage.isFavorite());

        int result = DataBaseManagerWrap.getDataBaseManager(getActivity()).updateImage(mImage);
        // If the result is > 0, update the view
        if (result > 0) {
            checkAsFavorite();
            sendResultToListener();
        // If the result is <= 0 (update failed) return image to the older state
        } else {
            mImage.setFavorite(!mImage.isFavorite());
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        mImagePosition = position;
        mImage = mImages.get(position);
        checkAsFavorite();
        String timeDate[] = AppUtils.dateToStringInLetters(mImage.getImageDate());
        mTvImageDate.setText(timeDate[0] + "\n" + timeDate[1]);
        mTvImageName.setText(mImage.getImageName());

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    public class ImagesPagerAdapter extends FragmentStatePagerAdapter {

        public ImagesPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Bundle fragmentArgs = new Bundle();
            fragmentArgs.putInt(ImageFragment.ARG_IMAGE_ID, mImages.get(position).getImageId());
            return ImageFragment.newInstance(fragmentArgs);
        }

        @Override
        public int getCount() {
            return mImages.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return null;
        }

        @Override
        public int getItemPosition(Object object){
            return PagerAdapter.POSITION_NONE;
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                sendResult();
                getActivity().finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }



    private void sendResult() {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("UPDATE_IMAGES", true);
        getActivity().setResult(Activity.RESULT_OK, returnIntent);
    }



    /**
     * Hide and show action bar and image options
     */
    private void showActions() {


        // Hide
        if (mActionBar.isShowing()) {
            mActionBar.hide();
            Animation hideImageOptions = AnimationUtils.loadAnimation(getActivity(), R.anim.bottom_down);
            mImageOptions.startAnimation(hideImageOptions);
            mImageOptions.setVisibility(View.GONE);
        // Show
        } else {
            mActionBar.show();
            Animation showImageOptions = AnimationUtils.loadAnimation(getActivity(), R.anim.bottom_up);
            mImageOptions.startAnimation(showImageOptions);
            mImageOptions.setVisibility(View.VISIBLE);
        }
    }


    /**
     * Color the heart with red if the image is checked as favorite
     * or leave the heart with its original color if not
     */
    private void checkAsFavorite() {

        if (mImage.isFavorite()) {
           int resource = R.drawable.ic_favorite_white_24dp;

            Drawable selectedDrawable = getResources().getDrawable(resource).getConstantState().newDrawable();
            selectedDrawable.mutate();
            selectedDrawable.setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);

            mIvFavorite.setImageDrawable(selectedDrawable);

        } else {
            mIvFavorite.setImageResource(R.drawable.ic_favorite_white_24dp);
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
                int result = DataBaseManagerWrap.getDataBaseManager(getActivity()).updateImage(mImage);
                if (result > 0) {
                    sendResultToListener();
                }
            } else if (requestCode == ImageDetailsActivity.RQ_IMAGE_UPDATED) {
                mImage = (Image) data.getSerializableExtra(ImageDetailsActivity.IMAGE_UPDATED);
                int result = DataBaseManagerWrap.getDataBaseManager(getActivity()).updateImage(mImage);
            }
        }
    }

    class TapGestureListener extends GestureDetector.SimpleOnGestureListener{

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            showActions();
            return true;
        }
    }

    private void sendResultToListener() {
        if (mChangeImageListener != null) {
            mChangeImageListener.onImageChange();
        }
    }

    public void setChangeImageListener(ChangeImageListener listener) {
        mChangeImageListener = listener;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mChangeImageListener = (ChangeImageListener) activity;
        } catch (ClassCastException e) {}
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mChangeImageListener = null;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mChangeImageListener = null;
    }



}
