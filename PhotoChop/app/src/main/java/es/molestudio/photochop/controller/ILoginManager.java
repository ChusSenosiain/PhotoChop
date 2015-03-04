package es.molestudio.photochop.controller;

import es.molestudio.photochop.model.User;

/**
 * Created by Chus on 04/03/15.
 */
public interface ILoginManager {


    public interface LoginActionListener {
        public void onDone(User user, Exception error);
    }

    public void signInWithEmail(User user, LoginActionListener listener);

    public void signUpWithEmail(User user, LoginActionListener listener);

    public void signInWithFacebook(LoginActionListener listener);

    public void signOut(LoginActionListener listener);

    public void resetPassword(User user, LoginActionListener listener);


}
