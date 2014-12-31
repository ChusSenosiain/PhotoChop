package es.molestudio.photochop.controller.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

import es.molestudio.photochop.R;

/**
 * Created by Chus on 31/12/14.
 */
public class GalleryActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_gallery);

        ViewPager vpImages = (ViewPager) findViewById(R.id.vp_images);


    }
}
