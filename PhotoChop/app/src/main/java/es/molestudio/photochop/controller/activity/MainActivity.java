package es.molestudio.photochop.controller.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.parse.ParseUser;

import java.util.Date;

import es.molestudio.photochop.R;
import es.molestudio.photochop.View.AppTextView;
import es.molestudio.photochop.controller.DataBaseManagerWrap;
import es.molestudio.photochop.controller.LoginManager;
import es.molestudio.photochop.controller.LoginManagerWrap;
import es.molestudio.photochop.controller.adapter.ADPDrawer;
import es.molestudio.photochop.controller.fragment.GridFragment;
import es.molestudio.photochop.controller.fragment.SignInFragment;
import es.molestudio.photochop.controller.location.MyLocation;
import es.molestudio.photochop.controller.util.Log;
import es.molestudio.photochop.model.Image;
import es.molestudio.photochop.model.ObjectDrawerItem;
import es.molestudio.photochop.model.User;

public class MainActivity extends ActionBarActivity
        implements View.OnClickListener,
        ListView.OnItemClickListener,
        MyLocation.ChangeLocationListener,
        LoginManager.LoginActionListener {


    private static final int RQ_CAPTURE_IMAGE = 1001;
    private Location mLocation;
    private MyLocation mMyLocation;


    private DrawerLayout mDrawerLayout;
    private LinearLayout mDrawerLinearLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private FloatingActionsMenu mFloatingActionsMenu;
    private GridFragment mGridFragment;


    private ImageView mIvUserImage;
    private AppTextView mtvUserName;
    private AppTextView mtvSubtitle;


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
        mGridFragment = (GridFragment) manager.findFragmentById(R.id.fragment_holder);

        if (mGridFragment == null) {
            mGridFragment = (GridFragment) GridFragment.newInstance(null);
            manager.beginTransaction()
                    .add(R.id.fragment_holder, mGridFragment)
                    .commit();
        }

        // Set up the drawer

        // Left Drawer items


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

        // User Data frame layout
        mIvUserImage = (ImageView) findViewById(R.id.iv_user_image);
        mtvUserName = (AppTextView) findViewById(R.id.tv_user_nickname);
        mtvSubtitle = (AppTextView) findViewById(R.id.tv_subtitle);

        RelativeLayout rlUserData = (RelativeLayout) findViewById(R.id.user_data_holder);
        rlUserData.setOnClickListener(this);

        loadUserData();


        mFloatingActionsMenu = (FloatingActionsMenu) findViewById(R.id.multiple_actions);
        FloatingActionButton btnCamera = (FloatingActionButton) findViewById(R.id.btn_new_photo);
        FloatingActionButton btnImportFromGallery = (FloatingActionButton) findViewById(R.id.btn_import_from_gallery);


        btnCamera.setOnClickListener(this);
        btnImportFromGallery.setOnClickListener(this);
        mDrawerList.setOnItemClickListener(this);

    }

    private void loadUserData() {

        if (ParseUser.getCurrentUser() != null) {
            mtvUserName.setText(ParseUser.getCurrentUser().getString("nickname"));
            mtvSubtitle.setText("");
        } else {

            mtvUserName.setText(getString(R.string.tv_user_nickname_default));
            mtvSubtitle.setText(getString(R.string.app_name));

            Drawable drawable = getResources().getDrawable(R.drawable.ic_account_circle_white_48dp);
            drawable.setColorFilter(getResources().getColor(R.color.light_primary_color), PorterDuff.Mode.MULTIPLY);
            mIvUserImage.setImageDrawable(drawable);
        }

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

        mDrawerLayout.closeDrawer(mDrawerLinearLayout);

        ParseUser parseUser = ParseUser.getCurrentUser();
        if (parseUser == null) {
            Intent logginIntent = new Intent(MainActivity.this, LogInActivity.class);
            startActivityForResult(logginIntent, LogInActivity.RQ_LOGGIN);
        } else {
            //Intent userInfo = new Intent(MainActivity.this, UserConfigActivity.class);
            //startActivity(userInfo);
        }
    }

    /**
     * Go to gallery: the default intent for choose an image doesn't allow to choose multiple images
     * we have to create a custom selectable gallery
     */
    private void importFromGallery() {
        mFloatingActionsMenu.collapse();
        Intent imageGallery = new Intent(this, GalleryActivity.class);
        startActivityForResult(imageGallery, GalleryActivity.RQ_SELECT_IMAGE);
    }


    /**
     * Take a photo
     */
    private void takePhoto() {
        mFloatingActionsMenu.collapse();
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, RQ_CAPTURE_IMAGE);
        mMyLocation = new MyLocation(this, this);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK && data != null) {
            // Photo
            if (requestCode == RQ_CAPTURE_IMAGE) {
                finishLocationService();
                saveImage(data.getData());
                mGridFragment.updateImagesFromBD(true);
            }
            // Import Image(s) from gallery
            else if (requestCode == GalleryActivity.RQ_SELECT_IMAGE) {
                boolean newImages = data.getBooleanExtra(GridFragment.NEW_IMAGES_ON_BD, false);
                if (newImages){
                    mGridFragment.updateImagesFromBD(true);
                }
            } else if (requestCode == LogInActivity.RQ_LOGGIN) {
                boolean loginOK = data.getBooleanExtra(SignInFragment.LOGIN_OK, false);
                if (loginOK) {
                    loadUserData();
                }
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

        DataBaseManagerWrap.getDataBaseManager(this).insertImage(image);

    }

    @Override
    public void onLocationChanged(Location location) {
        mLocation = location;
        Log.d("Location received " + location.getLatitude() + " " + location.getLongitude());
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
                showAbout();
                break;
            // Logout
            case 2:
                logOut();
                break;
        }
    }

    private void logOut() {

        if (ParseUser.getCurrentUser() != null) {
            LoginManagerWrap.getLoginManager(this).signOut(this);
        } else {
            Toast.makeText(this, getString(R.string.message_logout_with_no_loggin), Toast.LENGTH_SHORT).show();
        }
    }

    private void showAbout() {
        Intent aboutIntent = new Intent(MainActivity.this, AboutActivity.class);
        startActivity(aboutIntent);
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


    @Override
    public void onDone(User user, Exception error) {
        if (error == null) {
            loadUserData();
            Toast.makeText(this, getString(R.string.user_logout_ok), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, getString(R.string.user_logout_error), Toast.LENGTH_SHORT).show();
        }

    }

}
