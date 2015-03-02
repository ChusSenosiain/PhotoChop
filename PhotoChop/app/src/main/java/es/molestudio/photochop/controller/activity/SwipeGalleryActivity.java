package es.molestudio.photochop.controller.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.SparseArray;
import android.view.MenuItem;
import android.view.Window;

import java.util.ArrayList;

import es.molestudio.photochop.R;
import es.molestudio.photochop.controller.DataStorage;
import es.molestudio.photochop.controller.fragment.ImageFragment;
import es.molestudio.photochop.model.enumerator.ActionType;

/**
 * Created by Chus on 31/12/14.
 */
public class SwipeGalleryActivity extends ActionBarActivity implements ImageFragment.OnImageUpdateListener {

    public static final String EXTRA_IMAGE_ID = "es.molestudio.photochop.controller.activity.GalleryActivity.image_id";

    private Integer mImageID;
    private ArrayList<Integer> mImageIds = new ArrayList<Integer>();
    private SparseArray<Integer> mImages = new SparseArray<>();

    private ImagesPagerAdapter mImagesPagerAdapter;
    private ViewPager mImagesViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        mImageID = getIntent().getIntExtra(EXTRA_IMAGE_ID, 0);

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

        mImageIds = DataStorage.getDataStorage(this).getImagesIds();

        for (int i = 0; i < mImageIds.size(); i++) {
            mImages.append(mImageIds.get(i), i);
        }

        if (mImageID != null) {
            mImagesViewPager.setCurrentItem(mImages.get(mImageID));
        }


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

        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
