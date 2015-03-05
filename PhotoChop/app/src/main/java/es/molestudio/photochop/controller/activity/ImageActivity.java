package es.molestudio.photochop.controller.activity;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;

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

        mImage = (Image) getIntent().getSerializableExtra(EXTRA_IMAGE);

        FragmentManager manager = getSupportFragmentManager();
        if (manager.findFragmentById(R.id.fragment_holder) == null) {

            Bundle fragmentArgs = new Bundle();
            fragmentArgs.putSerializable(ImageFragment.ARG_IMAGE, mImage);

            manager.beginTransaction().add(R.id.fragment_holder, ImageFragment.newInstance(fragmentArgs))
                    .commit();
        }
    }


}
