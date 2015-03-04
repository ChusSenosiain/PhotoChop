package es.molestudio.photochop.controller.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import es.molestudio.photochop.R;
import es.molestudio.photochop.View.AppEditText;
import es.molestudio.photochop.View.AppTextView;
import es.molestudio.photochop.controller.ILoginManager;
import es.molestudio.photochop.controller.LoginManager;
import es.molestudio.photochop.model.User;


/**
 * Created by Chus on 27/11/14.
 */
public class SignInFragment extends Fragment implements ILoginManager.LoginActionListener,
        View.OnClickListener{

    private AppEditText mEmail;
    private AppEditText mPass;
    private ProgressBar mProgressBar;


    public static SignInFragment newInstance() {
        SignInFragment fragment = new SignInFragment();
        return fragment;
    }


    public SignInFragment() {}


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View root = inflater.inflate(R.layout.fragment_sign_in, container, false);

        mEmail = (AppEditText) root.findViewById(R.id.et_email);
        mPass = (AppEditText) root.findViewById(R.id.et_password);
        mProgressBar = (ProgressBar) root.findViewById(R.id.progressBar);

        RelativeLayout btnSignIn = (RelativeLayout) root.findViewById(R.id.btn_login);
        RelativeLayout btnSignInWithFacebook = (RelativeLayout) root.findViewById(R.id.btn_facebook_login);
        AppTextView btnTvForgotPass = (AppTextView) root.findViewById(R.id.tv_forgot_pass);

        btnSignIn.setOnClickListener(this);
        btnSignInWithFacebook.setOnClickListener(this);
        btnTvForgotPass.setOnClickListener(this);

        return root;
    }


    ////////////////////////////////////////////////////////////////////
    // INTERFACE IMPLEMENTATION
    ////////////////////////////////////////////////////////////////////

    // ILoginManager.LoginActionListener implementation
    @Override
    public void onDone(User user, Exception error) {

        mProgressBar.setVisibility(View.GONE);

        if (error == null && user != null) {
            getActivity().finish();
        } else {
            Toast.makeText(getActivity(), getString(R.string.sign_in_error), Toast.LENGTH_LONG).show();
        }
    }

    // View.OnClickListener implementation
    @Override
    public void onClick(View v) {

        mProgressBar.setVisibility(View.VISIBLE);

        switch (v.getId()) {
            case R.id.btn_login:
                loginWithEmail();
                break;

            case R.id.btn_facebook_login:
                loginWithFacebook();
                break;

            case R.id.tv_forgot_pass:
                forgotPass();
                break;
        }
    }

    private void loginWithEmail() {
        User user = new User();
        user.setUserEmail(mEmail.getText().toString());
        user.setUserPassword(mPass.getText().toString());
        LoginManager.getLoginManager(getActivity()).signInWithEmail(user, this);
    }


    private void loginWithFacebook(){
        LoginManager.getLoginManager(getActivity()).signInWithFacebook(this);
    }


    private void forgotPass() {
        User user = new User();
        user.setUserEmail(mEmail.getText().toString());
        LoginManager.getLoginManager(getActivity()).resetPassword(user, this);
    }


}
