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
import es.molestudio.photochop.controller.fragment.GridFragment;
import es.molestudio.photochop.controller.fragment.ImageFragment;
import es.molestudio.photochop.controller.fragment.SwipeGalleryFragment;
import es.molestudio.photochop.model.Image;
import es.molestudio.photochop.model.enumerator.ActionType;

/**
 * Created by Chus on 31/12/14.
 */
public class SwipeGalleryActivity extends ActionBarActivity
        implements SwipeGalleryFragment.ChangeImageListener  {

    public static final String EXTRA_IMAGE_POSITION = "es.molestudio.photochop.controller.activity.SwipeGalleryActivity.EXTRA_IMAGE_POSITION";

    public static final int RQ_CHANGE_IMAGES = 1425;
    public static final String IMAGES_CHANGED = "es.molestudio.photochop.controller.activity.SwipeGalleryActivity.IMAGES_CHANGED";

    private Integer mImagePosition;
    private boolean mIsImageChanged = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        supportRequestWindowFeature(Window.FEATURE_ACTION_BAR_OVERLAY);

        setContentView(R.layout.activity_swipe_gallery);

        mImagePosition = getIntent().getIntExtra(EXTRA_IMAGE_POSITION, 0);

        // Set up toolbar as actionbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.tb_toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);



        FragmentManager manager = getSupportFragmentManager();

        SwipeGalleryFragment fragment = (SwipeGalleryFragment) manager.findFragmentById(R.id.fragment_holder);
        if (fragment == null) {
            Bundle fragmentArg = new Bundle();
            fragmentArg.putInt(SwipeGalleryFragment.ARG_IMAGE_POSITION, mImagePosition);
            manager.beginTransaction()
                    .add(R.id.fragment_holder, SwipeGalleryFragment.newInstance(fragmentArg))
                    .commit();
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            sendResult();
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        sendResult();
    }

    @Override
    public void onImageChange() {
        mIsImageChanged = true;
    }


    private void sendResult() {

        if (mIsImageChanged) {
            Intent returnIntent = new Intent();
            returnIntent.putExtra(IMAGES_CHANGED, true);
            setResult(Activity.RESULT_OK, returnIntent);
        }
    }

}
