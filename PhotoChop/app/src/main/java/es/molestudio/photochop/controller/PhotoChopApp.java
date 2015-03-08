package es.molestudio.photochop.controller;

import android.app.Application;

import com.parse.Parse;

import es.molestudio.photochop.controller.util.AppUtils;

/**
 * Created by Chus on 03/01/15.
 */
public class PhotoChopApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        AppUtils.getAppKey(this);

        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);

        Parse.initialize(this, "applicationid", "clientkey");

        Images.getInstance(this);

    }




}
