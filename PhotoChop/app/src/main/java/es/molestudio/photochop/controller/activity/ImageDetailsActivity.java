package es.molestudio.photochop.controller.activity;

import android.location.Location;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import es.molestudio.photochop.R;
import es.molestudio.photochop.View.AppEditText;
import es.molestudio.photochop.View.AppTextView;
import es.molestudio.photochop.controller.DataStorage;
import es.molestudio.photochop.controller.adapter.ADPSelectable;
import es.molestudio.photochop.controller.location.GetAddressFromGPSLocationTask;
import es.molestudio.photochop.controller.util.AppUtils;
import es.molestudio.photochop.model.Category;
import es.molestudio.photochop.model.Constants;
import es.molestudio.photochop.model.Image;
import es.molestudio.photochop.model.Selectable;
import es.molestudio.photochop.model.SubCategory;

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

        ImageView ivImage = (ImageView) findViewById(R.id.iv_image);
        AppEditText etImageName = (AppEditText) findViewById(R.id.et_image_name);
        AppTextView tvImageDate = (AppTextView) findViewById(R.id.tv_image_date);
        AppTextView tvImageTime = (AppTextView) findViewById(R.id.tv_image_time);
        final AutoCompleteTextView etCategory = (AutoCompleteTextView) findViewById(R.id.et_category);
        AutoCompleteTextView etSubCagegory = (AutoCompleteTextView) findViewById(R.id.et_subcategory);
        final AppTextView tvAddress = (AppTextView) findViewById(R.id.tv_address);

        // Load image from BD
        mImage = DataStorage.getDataStorage(this).selectImage(mImageID);
        String[] imageDate = AppUtils.dateToStringInLetters(mImage.getImageDate());
        etImageName.setText(mImage.getImageName());
        tvImageDate.setText(imageDate[0]);
        tvImageTime.setText(imageDate[1]);
        //etCategory.setText(mImage.getImageCategory().getCategoryName());
        //etSubCagegory.setText(mImage.getImageSubCategory().getSubCaregoryName());

        // Show image
        ImageLoader.getInstance().displayImage(mImage.getImageUri().toString(), ivImage, Constants.UIL_DISPLAY_IMAGE_OPTIONS);

        // Get the image address (Reverse Localization)
        Location location = new Location("dummyProvider");
        location.setLatitude(mImage.getImageLatitude());
        location.setLongitude(mImage.getImageLongitude());


        // Get the categories

        ArrayList<Category> categories = new ArrayList<Category>();

        Category aa = new Category();
        aa.setId(1);
        aa.setName("uno");


        Category bb = new Category();
        bb.setId(2);
        bb.setName("dos");

        categories = DataStorage.getDataStorage(this).getCategories();

        categories.add(aa);
        categories.add(bb);
        categories.add(aa);
        categories.add(bb);
        categories.add(aa);
        categories.add(bb);

        ADPSelectable<Selectable> adpCategory = new ADPSelectable(this, R.layout.category_label, categories);
        etCategory.setAdapter(adpCategory);

        etCategory.setThreshold(1);


        etCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(ImageDetailsActivity.this, "selecciona: " + position, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(ImageDetailsActivity.this, "no selecciona: ", Toast.LENGTH_SHORT).show();
            }
        });

        /*
        ArrayList<SubCategory> subcategories = new ArrayList<SubCategory>();
        ADPSelectable<Selectable> adpSubCategory = new ADPSelectable(this, R.layout.category_label, subcategories);

        etCategory.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                etCategory.showDropDown();
                return false;
            }
        });


        etCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(ImageDetailsActivity.this, "Item click: " + position, Toast.LENGTH_SHORT).show();
            }
        });

        etCategory.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Toast.makeText(ImageDetailsActivity.this, "cambiado" + s.toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });*/


        new GetAddressFromGPSLocationTask(this, new GetAddressFromGPSLocationTask.GetAddressTaskListener() {
            @Override
            public void onDone(String address, Exception error) {
                tvAddress.setText(address);
            }
        }).execute(location);


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
