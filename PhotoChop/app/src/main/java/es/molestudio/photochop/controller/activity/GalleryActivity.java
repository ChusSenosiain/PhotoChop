package es.molestudio.photochop.controller.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;

import java.util.ArrayList;
import java.util.Locale;

import es.molestudio.photochop.R;
import es.molestudio.photochop.controller.DBManager;
import es.molestudio.photochop.controller.fragment.ImageFragment;
import es.molestudio.photochop.model.Image;

/**
 * Created by Chus on 31/12/14.
 */
public class GalleryActivity extends ActionBarActivity {


    private ArrayList<Image> mImages = new ArrayList<Image>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_gallery);
        ViewPager vpImages = (ViewPager) findViewById(R.id.vp_images);
        ImagesPagerAdapter imagesPagerAdapter = new ImagesPagerAdapter(getSupportFragmentManager());

        mImages = new DBManager(this).getImages();

        vpImages.setAdapter(imagesPagerAdapter);

    }


    public class ImagesPagerAdapter extends FragmentPagerAdapter {

        public ImagesPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
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
    }
}
