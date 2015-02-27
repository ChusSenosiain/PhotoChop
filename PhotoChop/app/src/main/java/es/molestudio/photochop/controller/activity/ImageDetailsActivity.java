package es.molestudio.photochop.controller.activity;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import es.molestudio.photochop.R;
import es.molestudio.photochop.View.AppEditText;
import es.molestudio.photochop.View.AppTextView;
import es.molestudio.photochop.controller.DataStorage;
import es.molestudio.photochop.model.Constants;
import es.molestudio.photochop.model.Image;

public class ImageDetailsActivity extends ActionBarActivity {

    public static final String EXTRA_IMAGE_ID = "es.molestudio.photochop.controller.activity.ImageDetailsActivity.IMAGE_ID";

    private Integer mImageID;
    private Image mImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_details);

        mImageID = getIntent().getIntExtra(EXTRA_IMAGE_ID, 0);

        ImageView ivImage = (ImageView) findViewById(R.id.iv_image);
        AppEditText etImageName = (AppEditText) findViewById(R.id.et_image_name);
        AppTextView tvImageDate = (AppTextView) findViewById(R.id.tv_image_date);
        AppEditText etCategory = (AppEditText) findViewById(R.id.et_category);
        AppEditText etSubCagegory = (AppEditText) findViewById(R.id.et_subcategory);
        AppTextView tvAddress = (AppTextView) findViewById(R.id.tv_address);

        // Load image from BD
        mImage = DataStorage.getDataStorage(this).selectImage(mImageID);


        // Show image on fields

        ImageLoader.getInstance().displayImage(mImage.getImageUri().toString(), ivImage, Constants.UIL_DISPLAY_IMAGE_OPTIONS);

        etImageName.setText(mImage.getImageName());







    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_image_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
