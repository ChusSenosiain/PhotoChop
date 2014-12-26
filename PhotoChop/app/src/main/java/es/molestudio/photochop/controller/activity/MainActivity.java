package es.molestudio.photochop.controller.activity;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import es.molestudio.photochop.R;


public class MainActivity extends ActionBarActivity implements View.OnClickListener{

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
                break;

            case R.id.btn_my_photos:
                // Gallery intent
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


    }
}
