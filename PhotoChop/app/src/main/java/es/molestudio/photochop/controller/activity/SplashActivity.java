package es.molestudio.photochop.controller.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import es.molestudio.photochop.R;
import es.molestudio.photochop.controller.util.AppUtils;
import es.molestudio.photochop.model.Constants;

public class SplashActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        AsyncTask showSplash = new AsyncTask() {

            @Override
            protected Object doInBackground(Object[] objects) {
                try {
                    //Show the splash one second
                    Thread.sleep(Constants.SPLASH_DISPLAY_LENGTH);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {

                super.onPostExecute(o);

                Intent mainIntent = null;

                /*
                if (UserManager.getUserLogged(SplashActivity.this) != null) {
                    mainIntent = new Intent(SplashActivity.this, MainActivity.class);
                } else {
                    mainIntent = new Intent(SplashActivity.this, SignActivity.class);
                }*/

                mainIntent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(mainIntent);
                SplashActivity.this.finish();
            }
        };

        showSplash.execute();
    }



}
