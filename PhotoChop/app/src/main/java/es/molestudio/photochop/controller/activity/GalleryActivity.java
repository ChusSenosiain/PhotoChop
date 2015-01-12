package es.molestudio.photochop.controller.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import es.molestudio.photochop.R;
import es.molestudio.photochop.controller.DBManager;
import es.molestudio.photochop.controller.fragment.ImageFragment;
import es.molestudio.photochop.model.Image;
import es.molestudio.photochop.model.enumerator.ActionType;

/**
 * Created by Chus on 31/12/14.
 */
public class GalleryActivity extends ActionBarActivity implements ImageFragment.OnImageUpdateListener {

    private ArrayList<Integer> mImageIds = new ArrayList<Integer>();
    private ImagesPagerAdapter mImagesPagerAdapter;
    private ViewPager mImagesViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        supportRequestWindowFeature(Window.FEATURE_ACTION_BAR_OVERLAY);


        setContentView(R.layout.activity_gallery);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        mImagesViewPager = (ViewPager) findViewById(R.id.vp_images);
        mImagesPagerAdapter = new ImagesPagerAdapter(getSupportFragmentManager());
        mImagesViewPager.setAdapter(mImagesPagerAdapter);

        mImageIds = new DBManager(this).getImagesIds();
        mImagesPagerAdapter.notifyDataSetChanged();

    }


    @Override
    public void onImageUpdate(Bundle imageUpdated) {

        int position = imageUpdated.getInt(ImageFragment.POSITION);

        ActionType actionType = (ActionType) imageUpdated.getSerializable(ImageFragment.ACTION_TYPE);
        if (actionType == ActionType.DELETE) {

            mImageIds.remove(position);

            if (mImageIds.size() == 0) {
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
            fragmentArgs.putInt(ImageFragment.ARG_IMAGE_ID, mImageIds.get(position));
            fragmentArgs.putInt(ImageFragment.ARG_POSITION, position);
            return ImageFragment.newInstance(fragmentArgs);
        }

        @Override
        public int getCount() {
            return mImageIds.size();
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

        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
