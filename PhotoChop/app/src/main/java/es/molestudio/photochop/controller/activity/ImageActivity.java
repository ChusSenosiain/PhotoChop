package es.molestudio.photochop.controller.activity;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import es.molestudio.photochop.R;
import es.molestudio.photochop.controller.fragment.ImageFragment;
import es.molestudio.photochop.model.Image;

/*
   SHOW A SINGLE VIEW OF AN IMAGE
 */
public class ImageActivity extends ActionBarActivity {

    public static final String EXTRA_IMAGE = "es.molestudio.photochop.controller.activity.ImageActivity.Image";

    private Image mImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        // Set up toolbar as actionbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.tb_toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(getString(R.string.app_name));
        actionBar.setDisplayHomeAsUpEnabled(true);

        mImage = (Image) getIntent().getSerializableExtra(EXTRA_IMAGE);

        FragmentManager manager = getSupportFragmentManager();
        if (manager.findFragmentById(R.id.fragment_holder) == null) {

            Bundle fragmentArgs = new Bundle();
            fragmentArgs.putSerializable(ImageFragment.ARG_IMAGE, mImage);

            manager.beginTransaction().add(R.id.fragment_holder, ImageFragment.newInstance(fragmentArgs))
                    .commit();
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
