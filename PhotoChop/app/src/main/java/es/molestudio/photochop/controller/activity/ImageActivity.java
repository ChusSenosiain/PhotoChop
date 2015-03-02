package es.molestudio.photochop.controller.activity;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import es.molestudio.photochop.R;
import es.molestudio.photochop.controller.fragment.ImageFragment;
import es.molestudio.photochop.model.Image;

public class ImageActivity extends ActionBarActivity {

    public static final String EXTRA_IMAGE = "es.molestudio.photochop.controller.activity.ImageActivity.Image";

    private Image mImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);


        // Set up the toolbar as actionbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.tb_toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        mImage = (Image) getIntent().getSerializableExtra(EXTRA_IMAGE);

        FragmentManager manager = getSupportFragmentManager();
        if (manager.findFragmentById(R.id.fragment_holder) == null) {

            Bundle fragmentArgs = new Bundle();
            fragmentArgs.putSerializable(ImageFragment.IMAGE_ID, mImage.getImageId());

            manager.beginTransaction().add(R.id.fragment_holder, ImageFragment.newInstance(fragmentArgs))
                    .commit();

        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_image, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_settings:
                return true;

            case android.R.id.home:
                finish();
                return true;
        }

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
