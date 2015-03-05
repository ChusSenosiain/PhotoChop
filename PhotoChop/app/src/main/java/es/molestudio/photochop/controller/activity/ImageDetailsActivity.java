package es.molestudio.photochop.controller.activity;

import android.location.Location;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;

import es.molestudio.photochop.R;
import es.molestudio.photochop.View.AppEditText;
import es.molestudio.photochop.View.AppTextView;
import es.molestudio.photochop.controller.DataBaseManagerWrap;
import es.molestudio.photochop.controller.location.GetAddressFromGPSLocationTask;
import es.molestudio.photochop.controller.util.AppUtils;
import es.molestudio.photochop.controller.util.ImageLoader;
import es.molestudio.photochop.model.Image;

public class ImageDetailsActivity extends ActionBarActivity {

    public static final String EXTRA_IMAGE_ID = "es.molestudio.photochop.controller.activity.ImageDetailsActivity.IMAGE_ID";
    public static final String IMAGE_UPDATED = "es.molestudio.photochop.controller.activity.ImageDetailsActivity.IMAGE_UPDATED";
    public static final int RQ_IMAGE_UPDATED = 1005;

    private Integer mImageID;
    private Image mImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_details);

        mImageID = getIntent().getIntExtra(EXTRA_IMAGE_ID, 0);

        // Set up toolbar as actionbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.tb_toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);



        ImageView ivImage = (ImageView) findViewById(R.id.iv_image);
        AppEditText etImageName = (AppEditText) findViewById(R.id.et_image_name);
        AppTextView tvImageDate = (AppTextView) findViewById(R.id.tv_image_date);
        AppTextView tvImageTime = (AppTextView) findViewById(R.id.tv_image_time);
        final AppTextView tvAddress = (AppTextView) findViewById(R.id.tv_address);

        // Load image from BD
        mImage = DataBaseManagerWrap.getDataBaseManager(this).selectImage(mImageID);
        String[] imageDate = AppUtils.dateToStringInLetters(mImage.getImageDate());
        etImageName.setText(mImage.getImageName());
        tvImageDate.setText(imageDate[0]);
        tvImageTime.setText(imageDate[1]);

        // Show image
        new ImageLoader(this).displayImage(ivImage, mImage.getImageUri());

        // Get the image address (Reverse Localization)
        Location location = new Location("dummyProvider");
        location.setLatitude(mImage.getImageLatitude());
        location.setLongitude(mImage.getImageLongitude());

        new GetAddressFromGPSLocationTask(this, new GetAddressFromGPSLocationTask.GetAddressTaskListener() {
            @Override
            public void onDone(String address, Exception error) {
                tvAddress.setText(address);
            }
        }).execute(location);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                finish();
        }

        return super.onOptionsItemSelected(item);
    }

}
