package es.molestudio.photochop.controller.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.location.Location;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Date;

import es.molestudio.photochop.R;
import es.molestudio.photochop.controller.DataStorage;
import es.molestudio.photochop.controller.adapter.ADPDrawer;
import es.molestudio.photochop.controller.fragment.GridFragment;
import es.molestudio.photochop.controller.location.MyLocation;
import es.molestudio.photochop.controller.util.Log;
import es.molestudio.photochop.model.Image;
import es.molestudio.photochop.model.ObjectDrawerItem;

public class MainActivity extends ActionBarActivity
        implements View.OnClickListener,
        ListView.OnItemClickListener,
        MyLocation.ChangeLocationListener {


    private static final int RQ_CAPTURE_IMAGE = 1001;
    private static final int RQ_SELECT_IMAGE = 1002;
    private Location mLocation;
    private MyLocation mMyLocation;


    private DrawerLayout mDrawerLayout;
    private LinearLayout mDrawerLinearLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private ActionBar mActionBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        // Set up toolbar as actionbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.tb_toolbar);
        setSupportActionBar(toolbar);

        // Set up toolbar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("");
        // enable ActionBar app icon to behave as action to toggle nav drawer
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);

        View view = getLayoutInflater().inflate(R.layout.action_bar_title_center, null);
        TextView tvTitle = (TextView) view.findViewById(R.id.tv_title);
        tvTitle.setText(getString(R.string.title_main_action_bar));

        actionBar.setCustomView(view);

        // Show the grid
        FragmentManager manager = getSupportFragmentManager();
        if (manager.findFragmentById(R.id.fragment_holder) == null) {
            manager.beginTransaction()
                    .add(R.id.fragment_holder, GridFragment.newInstance(null))
                    .commit();
        }

        // Set up the drawer


        // Left Drawer items

        // User item
        RelativeLayout rlUserData = (RelativeLayout) findViewById(R.id.user_data_holder);
        rlUserData.setOnClickListener(this);

        // List items
        String[] drawerMenuItems = getResources().getStringArray(R.array.drawer_main_items);
        ObjectDrawerItem[] drawerItems = new ObjectDrawerItem[drawerMenuItems.length];
        drawerItems[0] = new ObjectDrawerItem(R.drawable.ic_settings_white_24dp, drawerMenuItems[0]);
        drawerItems[1] = new ObjectDrawerItem(R.drawable.ic_help_white_24dp, drawerMenuItems[1]);
        drawerItems[2] = new ObjectDrawerItem(R.drawable.ic_exit_to_app_white_24dp, drawerMenuItems[2]);

        ADPDrawer adpDrawer = new ADPDrawer(this, drawerItems);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLinearLayout = (LinearLayout) findViewById(R.id.left_drawer);
        mDrawerList = (ListView) findViewById(R.id.list_view_drawer);
        // set a custom shadow that overlays the main content when the drawer opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        // set up the drawer's list view with items and click listener
        mDrawerList.setAdapter(adpDrawer);


        mDrawerToggle = new ActionBarDrawerToggle(this,
                mDrawerLayout,
                R.string.drawer_open,
                R.string.drawer_close) {

            public void onDrawerClosed(View view) {
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                invalidateOptionsMenu();
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);


        FloatingActionButton btnCamera = (FloatingActionButton) findViewById(R.id.btn_new_photo);
        FloatingActionButton btnImportFromGallery = (FloatingActionButton) findViewById(R.id.btn_import_from_gallery);

        btnCamera.setOnClickListener(this);
        btnImportFromGallery.setOnClickListener(this);
        mDrawerList.setOnItemClickListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.d("On resume del activity");


    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_new_photo:
                // Camera intent
                takePhoto();
                break;

            case R.id.btn_import_from_gallery:
                // Gallery intent
                importFromGallery();
                break;

            case R.id.user_data_holder:
                // User config or login
                userConfig();
                break;
        }
    }

    /**
     * If the user is logged, go to user config.
     * If the user isn't logged, go to loggin page
     */
    private void userConfig() {

        ParseUser parseUser = ParseUser.getCurrentUser();
        if (parseUser == null) {
            Intent logginIntent = new Intent(MainActivity.this, LogInActivity.class);
            startActivityForResult(logginIntent, LogInActivity.RQ_LOGGIN);
        }
    }

    /**
     * Go to gallery: the default intent for choose an image doesn't allow to choose multiple images
     * we have to create a custom selectable gallery
     */
    private void importFromGallery() {

        Intent imageGallery = new Intent(this, GalleryActivity.class);
        startActivityForResult(imageGallery, GalleryActivity.RQ_SELECT_IMAGE);
    }


    /**
     * Take a photo
     */
    private void takePhoto() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, RQ_CAPTURE_IMAGE);
        mMyLocation = new MyLocation(this, this);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            // Photo
            if (requestCode == RQ_CAPTURE_IMAGE) {
                finishLocationService();
                saveImage(data.getData());
            }
            // Image(s) from gallery
            else if (requestCode == RQ_SELECT_IMAGE) {
                saveImages((ArrayList<Image>) data.getSerializableExtra(""));
            }
        }

    }


    /**
     * Save the image on BD
     * @param imageUri
     */
    private void saveImage(Uri imageUri) {

        Image image = new Image();
        image.setImageUri(imageUri);
        image.setImageDate(new Date());

        // Obtengo el nombre de la imagen
        Cursor cursor = this.getContentResolver().query(image.getImageUri(), null, null, null, null);
        cursor.moveToFirst();
        image.setImageName(cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)));
        cursor.close();


        if (mLocation != null) {
            image.setImageLatitude(mLocation.getLatitude());
            image.setImageLongitude(mLocation.getLongitude());
        } else {
            image.setImageLatitude(0.0);
            image.setImageLongitude(0.0);
        }

        DataStorage.getDataStorage(this).insertImage(image);

    }

    private void saveImages(ArrayList<Image> images) {
        DataStorage.getDataStorage(this).insertImages(images);
    }



    @Override
    public void onLocationChanged(Location location) {
        mLocation = location;
        Log.d("Location received " + location.getLatitude() + " " + location.getLongitude());

        // The first one is the last know location, the second location received
        // is the actual location
        /*if (mLocationCounter == Constants.MAX_LOCATION_INTENTS) {
            finishLocationService();
        }*/
    }

    private void finishLocationService() {
        if (mMyLocation != null) {
            mMyLocation.stopLocationService();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finishLocationService();
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        selectItem(position);
        switch (position) {
            // TODO: Preferences
            case 0:
                break;
            // About
            case 1:
                // TODO: create about dialog
                break;
            // Logout
            case 2:
                // TODO: logout need login :P
                /*UserManager.logout(MainActivity.this);
                Intent userLogginIntent = new Intent(MainActivity.this, SignActivity.class);
                startActivity(userLogginIntent);
                finish();*/
                break;
        }
    }

    private void selectItem(int position) {
        mDrawerList.setItemChecked(position, true);
        mDrawerLayout.closeDrawer(mDrawerLinearLayout);
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // The action bar home/up action should open or close the drawer.
        // ActionBarDrawerToggle will take care of this.
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



}
