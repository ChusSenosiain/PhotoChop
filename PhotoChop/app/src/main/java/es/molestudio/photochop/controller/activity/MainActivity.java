package es.molestudio.photochop.controller.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import es.molestudio.photochop.R;
import es.molestudio.photochop.controller.DBManager;
import es.molestudio.photochop.model.Image;


public class MainActivity extends ActionBarActivity implements View.OnClickListener{


    private static final int CAPTURE_IMAGE_REQUEST_CODE = 1001;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Button btnCamera = (Button) findViewById(R.id.btn_camera);
        Button btnMyPhotos = (Button) findViewById(R.id.btn_my_photos);

        btnCamera.setOnClickListener(this);
        btnMyPhotos.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_camera:
                // Camera intent
                takePhoto();
                break;

            case R.id.btn_my_photos:
                // Gallery intent
                viewGallery();
                break;
        }
    }


    /**
     * Go to gallery
     */
    private void viewGallery() {
        Intent galleryIntent = new Intent(this, GalleryActivity.class);
        startActivity(galleryIntent);
    }


    /**
     * Take a photo
     */
    private void takePhoto() {


        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAPTURE_IMAGE_REQUEST_CODE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                saveImage(data.getData());
            } else if (resultCode != RESULT_CANCELED) {
                // Image capture failed, advise user
            } else {

            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
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
        new DBManager(this).insertImage(image);
    }



}
