package es.molestudio.photochop.controller;

import android.content.Context;

/**
 * Created by Chus on 04/03/15.
 */
public class LoginManagerWrap {

    public static LoginManager getLoginManager(Context context) {
        return new LoginManagerWithParse(context);
    }

}
