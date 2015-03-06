package es.molestudio.photochop.controller;

import android.app.Activity;
import android.content.Context;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;
import com.parse.RequestPasswordResetCallback;
import com.parse.SignUpCallback;

import java.util.Arrays;
import java.util.List;
import es.molestudio.photochop.controller.util.Log;
import es.molestudio.photochop.model.User;

/**
 * Created by Chus on 04/03/15.
 */
public class LoginManagerWithParse implements LoginManager {

    public static final int RQ_SIGNIN_FACEBOOK = 25365;


    private LoginActionListener mLoginActionListener;
    private Context mContext;

    public LoginManagerWithParse(Context context) {
        mContext = context;
    }

    @Override
    public void signInWithEmail(final User user, LoginActionListener listener) {

        mLoginActionListener = listener;

        try {

            ParseUser.logInInBackground(user.getUserEmail().trim(), user.getUserPassword().trim(), new LogInCallback() {
                @Override
                public void done(ParseUser parseUser, ParseException e) {

                    if (e == null) {
                        if (parseUser.get("emailVerified") == null || parseUser.getBoolean("emailVerified")) {
                            user.setUserId(parseUser.getObjectId());
                            mLoginActionListener.onDone(user, null);
                            Log.d("User logged: " + parseUser.getUsername());
                        } else {
                            mLoginActionListener.onDone(null, parseException(e));
                            Log.d("User not logged: the user must verify his email");
                        }
                    } else {
                        mLoginActionListener.onDone(null, parseException(e));
                        Log.d(parseException(e) + ": " + e.getMessage());
                    }
                }
            });

        } catch (Exception e) {
            mLoginActionListener.onDone(null, e);
            Log.d("Login error: " + e.getMessage(), e);
        }


    }

    @Override
    public void signUpWithEmail(final User user, LoginActionListener listener) {

        mLoginActionListener = listener;

        ParseUser parseUser = new ParseUser();
        parseUser.setEmail(user.getUserEmail());
        parseUser.setUsername(user.getUserEmail());
        parseUser.setPassword(user.getUserPassword());
        parseUser.put("nickname", user.getUserNickName());

        try {
            parseUser.signUpInBackground(new SignUpCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        mLoginActionListener.onDone(user, null);
                        Log.d("Sign Up OK!" + user.getUserEmail());
                    } else {
                        mLoginActionListener.onDone(null, parseException(e));
                        Log.d("Sign Up error: " + e.getMessage());
                    }
                }
            });

        } catch (Exception e) {
            mLoginActionListener.onDone(null, e);
            Log.d("Sign Up error: " + e.getMessage());
        }
    }

    @Override
    public void signInWithFacebook(LoginActionListener listener) {

        mLoginActionListener = listener;
        List<String> permissions = Arrays.asList("public_profile", "user_friends", "user_about_me",
                "user_relationships", "user_birthday", "user_location");

        try {
            ParseFacebookUtils.logIn(permissions, (Activity) mContext, RQ_SIGNIN_FACEBOOK, new LogInCallback() {
                @Override
                public void done(ParseUser parseUser, ParseException e) {

                    if (parseUser != null) {
                        User user = new User();
                        user.setUserId(parseUser.getObjectId());
                        user.setUserEmail(parseUser.getEmail());
                        user.setUserNickName(parseUser.getUsername());

                        if (parseUser.isNew()) {
                            Log.d("New User! " + parseUser.getUsername());
                        } else {
                            Log.d("Log in user:" + parseUser.getUsername());
                        }

                        mLoginActionListener.onDone(user, null);

                    } else {
                        mLoginActionListener.onDone(null, parseException(e));
                    }


                }
            });
        } catch (Exception e) {
            mLoginActionListener.onDone(null, e);
            Log.d("Facebook login error: " + e.getMessage(), e);
        }

    }

    @Override
    public void signOut(LoginActionListener listener) {

        mLoginActionListener = listener;

        try {
            ParseUser.logOut();
            mLoginActionListener.onDone(null, null);
            Log.d("SignOut OK");
        } catch (Exception e) {
            mLoginActionListener.onDone(null, e);
            Log.d("SignOut ERROR: " + e.getMessage());
        }
    }

    @Override
    public void resetPassword(final User user, LoginActionListener listener) {

        mLoginActionListener = listener;

        try {
            ParseUser.requestPasswordResetInBackground(user.getUserEmail(), new RequestPasswordResetCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        mLoginActionListener.onDone(user, null);
                        Log.d("Change Password OK");
                    } else {
                        mLoginActionListener.onDone(null, parseException(e));
                        Log.d("Change Password ERROR: " + e.getMessage());
                    }
                }
            });
        } catch (Exception e) {
            mLoginActionListener.onDone(null, e);
            Log.d("Change Password ERROR: " + e.getMessage());
        }


    }

    private Exception parseException(ParseException e) {
        Exception ex = e;
        return ex;
    }
}
