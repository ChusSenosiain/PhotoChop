package es.molestudio.photochop.controller.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.Window;

import java.util.ArrayList;

import es.molestudio.photochop.R;
import es.molestudio.photochop.controller.Images;
import es.molestudio.photochop.controller.fragment.ImageFragment;
import es.molestudio.photochop.model.Image;
import es.molestudio.photochop.model.enumerator.ActionType;

/**
 * Created by Chus on 31/12/14.
 */
public class SwipeGalleryActivity extends ActionBarActivity implements ImageFragment.OnImageUpdateListener {

    public static final String EXTRA_IMAGE_POSITION = "es.molestudio.photochop.controller.activity.GalleryActivity.EXTRA_IMAGE_POSITION";
    public static final String DELETE_IMAGES_ON_BD = "es.molestudio.photochop.controller.activity.GalleryActivity.DELETE_IMAGES_ON_BD";
    public static final int RQ_CHANGE_IMAGES = 1457;

    private Integer mImagePosition;
    private Integer mImagesOriginalItems;
    private ArrayList<Image> mImages = new ArrayList<Image>();

    private ImagesPagerAdapter mImagesPagerAdapter;
    private ViewPager mImagesViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        mImagePosition = getIntent().getIntExtra(EXTRA_IMAGE_POSITION, 0);

        supportRequestWindowFeature(Window.FEATURE_ACTION_BAR_OVERLAY);

        setContentView(R.layout.activity_swipe_gallery);

        // Set up toolbar as actionbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.tb_toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        mImagesViewPager = (ViewPager) findViewById(R.id.vp_images);
        mImagesPagerAdapter = new ImagesPagerAdapter(getSupportFragmentManager());
        mImagesViewPager.setAdapter(mImagesPagerAdapter);

        mImages = Images.getInstance(this);
        mImagesOriginalItems = mImages.size();


        if (mImagePosition != null) {
            mImagesViewPager.setCurrentItem(mImagePosition);
        }


        mImagesPagerAdapter.notifyDataSetChanged();

    }


    @Override
    public void onImageUpdate(Bundle imageUpdated) {

        int position = imageUpdated.getInt(ImageFragment.POSITION);

        ActionType actionType = (ActionType) imageUpdated.getSerializable(ImageFragment.ACTION_TYPE);
        if (actionType == ActionType.DELETE) {

            mImages.remove(position);
            if (mImages.size() == 0) {
                finish();
                return;
            }

            mImagesPagerAdapter.notifyDataSetChanged();

            if (position != 0) {
                mImagesViewPager.setCurrentItem(position - 1, true);
            } else {
                mImagesViewPager.setCurrentItem(0);
            }
        }

    }


    public class ImagesPagerAdapter extends FragmentStatePagerAdapter {

        public ImagesPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Bundle fragmentArgs = new Bundle();
            fragmentArgs.putInt(ImageFragment.ARG_IMAGE_ID, mImages.get(position).getImageId());
            fragmentArgs.putInt(ImageFragment.ARG_POSITION, position);
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
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        sendResult();
        super.onBackPressed();
    }


    private void sendResult() {

        Intent returnIntent = new Intent();
        if (mImages.size() != mImagesOriginalItems) {
            returnIntent.putExtra(DELETE_IMAGES_ON_BD, true);
        }
        setResult(Activity.RESULT_OK, returnIntent);

    }

}
