package es.molestudio.photochop.model;

/**
 * Created by Chus on 04/03/15.
 */
public class User {


    private String mUserId;
    private String mUserNickName;
    private String mUserEmail;
    private String mUserPassword;

    public User() {

    }

    public String getUserId() {
        return mUserId;
    }

    public void setUserId(String userId) {
        mUserId = userId;
    }

    public String getUserNickName() {
        return mUserNickName;
    }

    public void setUserNickName(String userNickName) {
        mUserNickName = userNickName;
    }

    public String getUserEmail() {
        return mUserEmail;
    }

    public void setUserEmail(String userEmail) {
        mUserEmail = userEmail;
    }


    public String getUserPassword() {
        return mUserPassword;
    }

    public void setUserPassword(String userPassword) {
        mUserPassword = userPassword;
    }
}
