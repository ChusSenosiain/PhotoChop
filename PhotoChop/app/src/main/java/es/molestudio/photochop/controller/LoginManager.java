package es.molestudio.photochop.controller;

import android.content.Context;

/**
 * Created by Chus on 04/03/15.
 */
public class LoginManager {

    public static ILoginManager getLoginManager(Context context) {
        return new ParseLoginManager(context);
    }

}
