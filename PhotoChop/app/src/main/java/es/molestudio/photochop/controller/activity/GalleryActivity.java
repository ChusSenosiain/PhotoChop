package es.molestudio.photochop.controller.activity;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import es.molestudio.photochop.R;
import es.molestudio.photochop.controller.fragment.GridFragment;

public class GalleryActivity extends ActionBarActivity {

    public static final Integer RQ_SELECT_IMAGE = 1004;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        // Set up toolbar as actionbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.tb_toolbar);
        setSupportActionBar(toolbar);

        // Set up toolbar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(getString(R.string.Gallery_title));
        // enable ActionBar app icon to behave as action to toggle nav drawer
        actionBar.setDisplayHomeAsUpEnabled(true);

        FragmentManager manager = getSupportFragmentManager();

        if (manager.findFragmentById(R.id.fragment_holder) == null) {

            Bundle fragmentArg = new Bundle();
            fragmentArg.putBoolean(GridFragment.ARG_SELECT_FROM_GALLERY, true);

            manager.beginTransaction()
                    .add(R.id.fragment_holder, GridFragment.newInstance(fragmentArg))
                    .commit();
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
